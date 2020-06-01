package com.sunjet.frontend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomConfig {
    @Getter
    @Value("${custom.file-path}")
    public String filePath;
}
