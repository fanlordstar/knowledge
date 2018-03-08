package org.lordstar.learning.mybatis.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.lordstar.learning.mybatis.dao.StudentDao;
import org.lordstar.learning.mybatis.javabean.Student;
import org.lordstar.learning.mybatis.javabean.StudentCustom;
import org.lordstar.learning.mybatis.javabean.UserQueryVo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        student.setName("李四");
        student.setAge(13);
        student.setClassNo("2345234");
        student.setHomeTown("陕西西安");
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

    @Test
    public void testFindStudentByMap() throws Exception {
        Map<String, Object> map = new HashMap();
        map.put("id", 11);
        map.put("age", 12);
        List<Student> list = studentDao.findStudentByMap(map);
        logger.info(list);
    }

    @Test
    public void testFindStudentList() throws Exception {
        UserQueryVo user = new UserQueryVo();
        StudentCustom studentCustom = new StudentCustom();
        studentCustom.setId(11);
        studentCustom.setName("张");
        user.setStudent(studentCustom);
        List<StudentCustom> list = studentDao.findStudentList(user);
        logger.info(list);
    }

    @Test
    public void testFindAllStudentMap() throws Exception {
        List<Map<String, Object>> list = studentDao.findAllStudentMap();
        logger.info(list);
    }

    @Test
    public void testFindStudentResultMap() throws Exception {
        List<Student> list = studentDao.findStudentResultMap();
        logger.info(list);
    }

}
