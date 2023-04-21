package com.atguigu.imperial.court.filter;


import com.atguigu.imperial.court.util.JDBCUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author small black
 * @creat 2023-04-18 12:46
 */
public class TransactionFilter implements Filter {
    //声明一个集合保存静态资源的拓展名
    private static Set<String> staticResourceExtNameSet;
    static{
        staticResourceExtNameSet=new HashSet<>();
        staticResourceExtNameSet.add(".pmg");
        staticResourceExtNameSet.add(".jpg");
        staticResourceExtNameSet.add(".css");
        staticResourceExtNameSet.add(".js");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
         //前置操作，对静态资源进行放行
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String servletPath = request.getServletPath();
        if(servletPath.contains(".")){
            String extName=servletPath.substring(servletPath.lastIndexOf("."));
            if(staticResourceExtNameSet.contains(extName)){
                //检测到当前求为静态资源，故直接放行,不做事务操作
                filterChain.doFilter(servletRequest,servletResponse);

                //当前方法立即放回
                return ;
            }
        }
        Connection connection=null;
        try{
            //1获取数据库链接
            connection= JDBCUtils.getConnection();
            //2关闭自动提交功能
            connection.setAutoCommit(false);

            //3核心操作
            filterChain.doFilter(servletRequest,servletResponse);

            //4提交事务
            connection.commit();


        }catch(Exception e){

            // 若发生异常则回滚事务
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String message=e.getMessage();
            //将异常信息存入请求域
            request.setAttribute("systemMessage",message);

            //请求转发
            request.getRequestDispatcher("/").forward(request,servletResponse);

        }
        finally{
            //释放数据库链接
            JDBCUtils.releaseConnecion(connection);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
