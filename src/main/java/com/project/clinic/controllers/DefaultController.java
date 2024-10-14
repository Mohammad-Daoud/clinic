package com.project.clinic.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String defaultRequest(){
        return "redirect:/clients";
    }

    @PostMapping("/setSessionValue")
    @ResponseBody
    public String setDontShowAgain(@RequestBody Map<String, Boolean> payload, HttpSession session) {
        // Set session attribute based on checkbox state
        Boolean dontShowAgain = payload.get("dontShowAgain");
        if (dontShowAgain != null) {
            session.setAttribute("dontShowRescheduledModal", dontShowAgain);
        }
        return "success";
    }

}
