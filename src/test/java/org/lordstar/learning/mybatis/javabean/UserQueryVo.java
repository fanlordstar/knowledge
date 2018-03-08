package org.lordstar.learning.mybatis.javabean;

/**
 * 包装类，里面可能会有多种查询条件
 */
public class UserQueryVo {
    private StudentCustom student;

    public StudentCustom getStudent() {
        return student;
    }

    public void setStudent(StudentCustom student) {
        this.student = student;
    }
}
