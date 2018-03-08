package org.lordstar.learning.mybatis.dao;

import org.lordstar.learning.mybatis.javabean.Student;

import java.util.List;

public interface StudentDao {
    public Student findStudentById(int id) throws Exception;

    public List<Student> findStudnetByAge(int age) throws Exception;

    public void insertStudent(Student student) throws Exception;

    public void deleteStudent(int id) throws Exception;

    public List<Student> findAllStudent() throws Exception;
}
