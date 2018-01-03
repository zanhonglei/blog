package com.zweb.blog.multidatasource.core;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * spring中的一个类，AbstractRoutingDataSource，可以创建多个数据库，并在几个数据库中进行切换
 * 使用DatabaseContextHolder获取当前线程的DataSoureEnum
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private Logger log = Logger.getLogger(this.getClass());


    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}