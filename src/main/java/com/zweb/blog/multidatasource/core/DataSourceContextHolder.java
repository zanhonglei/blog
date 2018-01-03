package com.zweb.blog.multidatasource.core;

/**
 * 构建一个DataSourceEnum容器，并提供了向其中设置和获取DataSorceEnum的方法
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceEnum> CONTEXT_HOLDER = new ThreadLocal<DataSourceEnum>() {

        @Override
        protected DataSourceEnum initialValue() {
            return DataSourceEnum.master;
        }
    };


    public static void setDataSourceType(DataSourceEnum type) {
        CONTEXT_HOLDER.set(type);
    }

    public static DataSourceEnum getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    public static void resetDataSourceType() {
        CONTEXT_HOLDER.set(DataSourceEnum.master);
    }
}