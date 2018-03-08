package org.lordstar.learning.mybatis.javabean;

/**
 * student的扩展类
 */
public class StudentCustom extends Student {
    // 里面可能会扩展一些其他的成员变量
    @Override
    public String toString() {
        return "StudentCustom{}" + super.toString();
    }
}
