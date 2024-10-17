package com.project.clinic.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String IMAGE_DIR = "file:upload/images/";
    private static final String IMAGE_URL_PATTERN = "/images/**";
    private static final String FAV_ICON_PATTERN = "/favicon.ico";
    private static final String STATIC_PATTERN = "/static/img/**";
    private static final String STATIC_LOCATION = "classpath:static/img/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(IMAGE_URL_PATTERN)
                .addResourceLocations(IMAGE_DIR);

        registry.addResourceHandler(FAV_ICON_PATTERN, STATIC_PATTERN)
                .addResourceLocations(STATIC_LOCATION);
    }


}
