package com.threeman.servicecore.config.mybatisplus;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author  songjing
 * @date  2021/11/23 19:17
 * @version 1.0
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 插件配置
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页配置
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        /*//乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //动态表名
        interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor());
        //防止全表更新删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //多租户
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor());*/
        return interceptor;
    }

}
