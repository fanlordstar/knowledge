package org.lordstar.learning.mybatis.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.lordstar.learning.mybatis.javabean.Student;

import java.io.InputStream;
import java.util.List;

public class IbatisTest {
    private static Logger logger = Logger.getLogger(IbatisTest.class);
    private static SqlSession sqlSession;
    static {
        String config = "mybatis/mybatissetting.xml";
        InputStream inputStream = null;
        inputStream = IbatisTest.class.getClassLoader().getResourceAsStream(config);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();

    }

    @Test
    public void testFindStudentById() {
        Student student = sqlSession.selectOne("test.findStudentById", 8);
        logger.info(student);
    }

    @Test
    public void testFindStudentByAge() {
        List<Student> lists = sqlSession.selectList("test.findStudentByAge", 10);
        logger.info(lists);
    }
}
