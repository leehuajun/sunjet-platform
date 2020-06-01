package com.sunjet.backend;

import com.sunjet.backend.filter.RequestLog;
import com.sunjet.backend.filter.SecurityFilter;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 */
//@EnableSwagger2Doc
@SpringBootApplication
public class BackendApplication extends WebMvcConfigurerAdapter {
    /**
     * 注册权限拦截过滤器 Filter
     * 在1.4.0的时候，包名从 org.springframework.boot.context.embedded.FilterRegistrationBean 变为了
     * org.springframework.boot.web.servlet.FilterRegistrationBean 需要注意一下。
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean securityFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SecurityFilter securityFilter = new SecurityFilter();
        registrationBean.setFilter(securityFilter);
        registrationBean.setOrder(1);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    // 增加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //只做打印请求地址使用，可扩展
        registry.addInterceptor(new RequestLog());
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }


}
