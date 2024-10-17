package com.project.clinic.controllers.rest;

import com.project.clinic.models.Exam;
import com.project.clinic.services.ClientService;
import com.project.clinic.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@RestController
@RequestMapping("/api/clients")
public class RestClientController {


    private final ClientService clientService;
    private final ExamService examService;
    private final SpringTemplateEngine templateEngine;

    public RestClientController(ClientService clientService, ExamService examService, SpringTemplateEngine templateEngine) {
        this.clientService = clientService;
        this.examService = examService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/check-client-name")
    public ResponseEntity<Boolean> checkClientName(
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName,
            @RequestParam("thirdName") String thirdName,
            @RequestParam("lastName") String lastName) {

        boolean exists = clientService.clientExists(firstName, secondName, thirdName, lastName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/prescription")
    @ResponseBody
    public String getPrescriptionHtml(@RequestParam("id") Long id) {
        Exam exam = examService.getExamById(id);
        Context context = new Context();
        context.setVariable("exam", exam);
        return templateEngine.process("prescription", context);
    }
}