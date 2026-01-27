package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 指定切入点
     * 拦截mapper包下的所有的类的带有注解的所有方法
     * 切面表达式的抽取
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 自定义的前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的自动填充");

        //1.获取房前被拦截的方法上的数据库操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        OperationType operationType = methodSignature.getMethod().getAnnotation(AutoFill.class).value();//获得方法上的注解对象的操作类型
        log.info("数据库操作类型：{}",operationType);
        //2.获取当前被拦截方法的参数，即实体对象
        Object[] args = joinPoint.getArgs();//将实体对象作为第一个参数
        if(args==null || args.length==0){
            return;
        }
        Object object = args[0];//获取第一个参数
        log.info("实体对象：{}",object);
        //3.获取数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //4.根据对应的数据库操作类型，为对应的字段赋值（通过反射）
        if(operationType == OperationType.INSERT){
            //如果是插入操作，四个公共字段都需要赋值
            try {
                //通过反射获取方法
                Method setCreateTime= object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateuser=object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateTime=object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser=object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //通过反射为对象属性进行赋值
                setCreateTime.invoke(object,now);
                setCreateuser.invoke(object,currentId);
                setUpdateTime.invoke(object,now);
                setUpdateUser.invoke(object,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (operationType == OperationType.UPDATE) {
            //如果是操作，只需要为两个公共字段赋值
            try {
                //通过反射获取方法
                Method setUpdateTime=object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser=object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //通过反射为对象属性进行赋值
                setUpdateTime.invoke(object,now);
                setUpdateUser.invoke(object,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
