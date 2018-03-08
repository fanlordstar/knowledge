package org.lordstar.learning.mybatis.dao;

import org.lordstar.learning.mybatis.javabean.Student;
import org.lordstar.learning.mybatis.javabean.StudentCustom;
import org.lordstar.learning.mybatis.javabean.UserQueryVo;

import java.util.List;
import java.util.Map;

public interface StudentDao {
    public Student findStudentById(int id) throws Exception;

    public List<Student> findStudnetByAge(int age) throws Exception;

    public void insertStudent(Student student) throws Exception;

    public void deleteStudent(int id) throws Exception;

    public List<Student> findAllStudent() throws Exception;

    public List<Student> findStudentByMap(Map<String, Object> map) throws Exception;

    public List<StudentCustom> findStudentList(UserQueryVo user) throws Exception;

    public List<Map<String, Object>> findAllStudentMap() throws Exception;

    // 根据resultMap查询
    public List<Student> findStudentResultMap() throws Exception;
}
