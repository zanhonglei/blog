package com.zweb.blog.multidatasource.aspect;

import com.zweb.blog.multidatasource.core.DataSourceEnum;
import com.zweb.blog.multidatasource.annotation.DataSource;
import com.zweb.blog.multidatasource.core.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 通过注解的形式切换数据源
 */
@Component
@Aspect
public class DataSourceAspect {
    @Pointcut("execution(* com.zweb.blog.service.impl..*(..)) " +
            "&& @annotation(com.zweb.blog.multidatasource.annotation.DataSource)")
    public void dataSourcePointcut() {
    }

    @Around("dataSourcePointcut()")
    public Object doAround(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        DataSourceEnum sourceEnum = dataSource.value();

        if (sourceEnum == DataSourceEnum.master) {
            DataSourceContextHolder.setDataSourceType(DataSourceEnum.master);
        } else if (sourceEnum == DataSourceEnum.slaver) {
            DataSourceContextHolder.setDataSourceType(DataSourceEnum.slaver);
        }

        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            DataSourceContextHolder.resetDataSourceType();
        }

        return result;
    }

}
