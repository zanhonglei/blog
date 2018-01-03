package com.zweb.blog.multidatasource.config;

import com.zweb.blog.multidatasource.core.DataSourceEnum;
import com.zweb.blog.multidatasource.core.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * 通过读取application.properties文件生成两个数据源（masterDataSource，slaverDataSource）
 * 使用以上生成的两个数据源构造动态数据源dataSource
 *
 * @Primary：指定在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@Autowire注解报错（一般用于多数据源的情况下）
 * @Qualifier：指定名称的注入，当一个接口有多个实现类的时候使用 （在本例中，有三个DataSource类型的实例，DynamicDataSource也是一种DataSource，需要指定名称注入）
 * @Bean：生成的bean实例的名称是方法名 （例如上边的@Qualifier注解中使用的名称是前边两个数据源的方法名，而这两个数据源也是使用@Bean注解进行注入的）
 * 通过动态数据源构造SqlSessionFactory和事务管理器（如果不需要事务，后者可以去掉）
 */
@Configuration
@MapperScan(basePackages = "com.zweb.blog.dao")
public class MyBatisConfig {

    /**
     * @return
     * @throws Exception
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     */
    @Primary
    @Bean("masterDataSource")
    @ConfigurationProperties(prefix = "datasource.master")
    public DataSource masterDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }

    @Bean("slaverDataSource")
    @ConfigurationProperties(prefix = "datasource.slaver")
    public DataSource slaverDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }

    /**
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                               @Qualifier("slaverDataSource") DataSource slaverDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataSourceEnum.master, masterDataSource);
        targetDataSources.put(DataSourceEnum.slaver, slaverDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(masterDataSource);// 默认的datasource设置为myTestDbDataSource

        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource,
                                               @Value("com.zweb.blog.entity") String typeAliasesPackage,
                                               @Value("classpath:mapping/*.xml") String mapperLocations) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        factoryBean.setTypeAliasesPackage(typeAliasesPackage);// 指定基包
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));//

        return factoryBean.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}