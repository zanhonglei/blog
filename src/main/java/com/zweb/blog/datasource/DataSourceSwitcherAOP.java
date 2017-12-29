package com.zweb.blog.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 通过注解的形式切换数据源
 */
@Aspect
@Component
public class DataSourceSwitcherAOP {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.zweb.blog.service.*Service.*(..))")
    public void scan() {
    }

    @Before("scan()")
    public void before(JoinPoint joinPoint) {
        Method[] methods = joinPoint.getSignature().getDeclaringType().getMethods();
        for (Method method : methods) {
            DataSource dataSource = method.getAnnotation(DataSource.class);
            if(dataSource!=null)
                logger.info(dataSource.value());
        }
    }

    @After("scan()")
    public void after(JoinPoint joinPoint) {

    }

    @AfterThrowing("scan()")
    public void afterThrowing() {

    }
}
