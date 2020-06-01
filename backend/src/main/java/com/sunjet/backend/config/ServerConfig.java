package com.sunjet.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: lhj
 * @create: 2017-07-04 08:38
 * @description: 说明
 */
@Data
@Component
@ConfigurationProperties(prefix = "server")
@PropertySource("classpath:server-config.properties")
public class ServerConfig {
    private String auth;
}
