package com.yejh.test;/**
 * @author yejh
 * @create 2019-12_18 19:30
 */

import com.yejh.bean.Employee;
import com.yejh.bean.EmployeeExample;
import com.yejh.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description: TODO
 **/
public class MBGTest {
    private SqlSessionFactory sqlSessionFactory = null;

    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    @Before
    public void init() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

    }

    @Test
    public void queryTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.setOrderByClause("id");
        //1、使用example创建一个Criteria（查询准则）
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andEmpnameLike("%男%");

        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void insertBatchTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            Employee employee = new Employee();
            employee.setEmpname(UUID.randomUUID().toString().substring(0, 5));
            employee.setEmail(UUID.randomUUID().toString().substring(0, 5) + "@qq.com");
            employees.add(employee);
        }
        mapper.insertBatch(employees);

    }

    @Test
    public void pageHelperTest(){

    }
}
