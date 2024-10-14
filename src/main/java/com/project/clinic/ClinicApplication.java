package com.project.clinic;

import com.project.clinic.utils.SwingUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClinicApplication {

    public static void main(String[] args) {
        SwingUtils.showLoadingMessage();
        ApplicationContext context = SpringApplication.run(ClinicApplication.class, args);
        SwingUtils.openUIWindow(context);
        SwingUtils.closeLoadingMessage();
    }
}
