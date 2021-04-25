package com.offcn.project.config;


import com.offcn.dycommon.utils.OssTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProjectConfig {
    @ConfigurationProperties(prefix="oss") //yml配置文件中以oss开头的属性注入到OssTemplate对象中
    @Bean
    public OssTemplate ossTemplate(){
        return new OssTemplate();
    }

}
