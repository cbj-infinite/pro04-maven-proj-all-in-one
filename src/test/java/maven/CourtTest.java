package maven;

import com.atguigu.imperial.court.dao.BaseDao;
import com.atguigu.imperial.court.entity.Emp;
import com.atguigu.imperial.court.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

/**
 * @author small black
 * @creat 2023-04-18 10:02
 */
public class CourtTest {
    @Test
    public void connnectionTest(){
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
        JDBCUtils.releaseConnecion(connection);

    }
    private BaseDao<Emp> baseDao = new BaseDao<>();

    @Test
    public void testGetSingleBean() {

        String sql = "select emp_id empId,emp_name empName,emp_position empPosition,login_account loginAccount,login_password loginPassword from t_emp where emp_id=?";

        Emp emp = baseDao.getSingleBean(sql, Emp.class, 1);

        System.out.println("emp = " + emp);

    }
    @Test
    public void testGetBeanList() {

        String sql = "select emp_id empId,emp_name empName,emp_position empPosition,login_account loginAccount,login_password loginPassword from t_emp";

        List<Emp> empList = baseDao.getBeanList(sql, Emp.class);

        for (Emp emp : empList) {
            System.out.println("emp = " + emp);
        }

    }

    @Test
    public void testUpdate() {
        //对代码内容进行修改
        //在远端仓库对代码注释进行修改
        //我又在idea修改了一次
        String sql = "update t_emp set emp_position=? where emp_id=?";

        String empPosition = "minister";
        String empId = "3";

        int affectedRowNumber = baseDao.update(sql, empPosition, empId);

        System.out.println("affectedRowNumber = " + affectedRowNumber);

    }
}
