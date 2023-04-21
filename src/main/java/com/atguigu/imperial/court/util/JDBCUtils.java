package com.atguigu.imperial.court.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author small black
 * @creat 2023-04-13 17:43
 */
public class JDBCUtils {
    // 将数据源对象设置为静态属性，保证大对象的单一实例
    private static DataSource dataSource;
    // 由于 ThreadLocal 对象需要作为绑定数据时 k-v 对中的 key，所以要保证唯一性
    // 加 static 声明为静态资源即可保证唯一性
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    static {

        // 1.创建一个用于存储外部属性文件信息的Properties对象
        Properties properties = new Properties();

        // 2.使用当前类的类加载器加载外部属性文件：jdbc.properties
        InputStream inputStream = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");

        try {

            // 3.将外部属性文件jdbc.properties中的数据加载到properties对象中
            properties.load(inputStream);

            // 4.创建数据源对象
            dataSource = DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    public static Connection getConnection(){
        Connection connection=null;
        try{
            connection=threadLocal.get();
            if(connection==null){
                connection=dataSource.getConnection();
                threadLocal.set(connection );
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return connection;

    }
    public static void releaseConnecion(Connection connection){
        if(connection!=null){
            try{
                connection.close();
                threadLocal.remove();
            }catch(SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
