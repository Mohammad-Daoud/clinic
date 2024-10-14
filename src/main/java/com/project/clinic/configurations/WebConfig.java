package com.project.clinic.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String IMAGE_DIR = "file:upload/images/";
    private static final String IMAGE_URL_PATTERN = "/images/**";
    private static final String FAV_ICON= "/favicon.ico";
    private static final String FAV_ICON_LOCATION = "file:img/favicon.ico";



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(IMAGE_URL_PATTERN)
                .addResourceLocations(IMAGE_DIR);

        registry.addResourceHandler(FAV_ICON)
                .addResourceLocations(FAV_ICON_LOCATION);
    }


}
