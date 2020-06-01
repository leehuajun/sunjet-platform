package com.sunjet.frontend;

import com.sunjet.hessian.PictureUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FrontendApplication {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder.build();
    }

//    @Bean
//    public HessianProxyFactoryBean helloClient() {
//        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
//        factory.setServiceUrl("http://localhost:8090/HelloWorldService");
//        factory.setServiceInterface(PictureUploadService.class);
//        return factory;
//    }
}
