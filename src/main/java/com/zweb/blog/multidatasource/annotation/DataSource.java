package com.zweb.blog.multidatasource.annotation;

import com.zweb.blog.multidatasource.core.DataSourceEnum;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.master;
}
