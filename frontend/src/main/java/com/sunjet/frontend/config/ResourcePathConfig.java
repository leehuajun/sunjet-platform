package com.sunjet.frontend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourcePathConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private CustomConfig customConfig;

    /**
     * 追加静态资源访问路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( "/files/**").addResourceLocations("file:" + customConfig.getFilePath());
        super.addResourceHandlers(registry);
    }

}