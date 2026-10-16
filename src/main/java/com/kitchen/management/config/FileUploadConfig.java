package com.kitchen.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/managers/**")
                .addResourceLocations("file:uploads/managers/");
        

registry.addResourceHandler("/uploads/meals/**")
        .addResourceLocations("file:uploads/meals/");


        registry.addResourceHandler("/uploads/delivery-persons/**")
                .addResourceLocations("file:uploads/delivery-persons/");
    }
}