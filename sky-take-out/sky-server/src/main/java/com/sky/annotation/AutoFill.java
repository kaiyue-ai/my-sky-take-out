package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段填充处理
 */
@Target(ElementType.METHOD)//注解只能加在方法上面
@Retention(RetentionPolicy.RUNTIME)//保留到运行时
public @interface AutoFill {
    //数据库操作类型:UPDATE INSERT
    //OperationType 是参数类型 value是方法名
    OperationType value();
}
