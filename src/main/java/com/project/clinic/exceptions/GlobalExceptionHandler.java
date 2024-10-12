package com.project.clinic.exceptions;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // This catches all exceptions
    public ModelAndView handleException(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage()); // Pass the exception message to the view
        mav.setViewName("error"); // This is the error page
        return mav;
    }


    @ExceptionHandler(AuthorizationDeniedException.class) // This catches all exceptions
    public ModelAndView handleAccessDeniedException() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("access-denied"); // This is the error page
        return mav;
    }

    @ExceptionHandler({ClientNotFoundException.class})
    public ModelAndView handleClientNotFoundException() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("client-not-found");
        return mav;
    }
}
