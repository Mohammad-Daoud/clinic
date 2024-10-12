package com.project.clinic;

import com.project.clinic.utils.NetworkUtil;
import com.project.clinic.utils.SwaggerUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClinicApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ClinicApplication.class, args);
        NetworkUtil.openBrowser(context);
        SwaggerUtils.openUIWindow(context);
    }

}
