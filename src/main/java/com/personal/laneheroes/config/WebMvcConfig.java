package com.personal.laneheroes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${image-dir}")
    private String imageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/hero/**")
                .addResourceLocations("file:///" + imageDir + "hero/");

        registry.addResourceHandler("/images/game/**")
                .addResourceLocations("file:///" + imageDir + "game/");
    }
}