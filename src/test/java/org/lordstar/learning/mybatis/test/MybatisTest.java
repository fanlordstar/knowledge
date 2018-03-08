package org.lordstar.learning.mybatis.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.lordstar.learning.mybatis.dao.StudentDao;
import org.lordstar.learning.mybatis.javabean.Student;

import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    private static Logger logger = Logger.getLogger(MybatisTest.class);
    private static SqlSession sqlSession;

    private StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
    static {
        String config = "mybatis/mybatissetting.xml";
        InputStream inputStream = null;
        inputStream = IbatisTest.class.getClassLoader().getResourceAsStream(config);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();

    }

    @Test
    public void testFindStudentById() throws Exception {
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        Student student = studentDao.findStudentById(8);
        logger.info(student);
    }

    @Test
    public void testFindStudentByAge() throws Exception {
        List<Student> list = studentDao.findStudnetByAge(10);
        logger.info(list);
    }

    @Test
    public void testInsertStudent() throws Exception {
        Student student = new Student();
        student.setName("张三");
        student.setAge(12);
        student.setClassNo("123456");
        student.setHomeTown("河南郑州");
        student.setSex("男");
        studentDao.insertStudent(student);
        sqlSession.commit();
        List<Student> list = studentDao.findAllStudent();
        logger.info(list);
    }

    @Test
    public void testDeleteStudentById() throws Exception {
        List<Student> list = studentDao.findStudnetByAge(10);
        logger.info(list);
        studentDao.deleteStudent(4);
        studentDao.deleteStudent(8);
        sqlSession.commit();
        list = studentDao.findAllStudent();
        logger.info(list);
    }

    @Test
    public void testFindAllStudent() throws Exception {
        List<Student> list = studentDao.findAllStudent();
        logger.info(list);
    }

}
