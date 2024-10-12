package com.project.clinic.controllers;

import com.project.clinic.models.Exam;
import com.project.clinic.services.ClientService;
import com.project.clinic.services.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/exams")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ExamController {

    private final ExamService examService;
    private final ClientService clientService;

    @Autowired
    public ExamController(ExamService examService, ClientService clientService) {
        this.examService = examService;
        this.clientService = clientService;
    }

    @GetMapping("/add")
    public String showAddExamForm(@RequestParam("id") Long clientId, Model model) {
        model.addAttribute("client", clientService.getClientById(clientId));
        model.addAttribute("exam", new Exam());
        return "add-exam";
    }

    @PostMapping("/add")
    public String addExam(@RequestParam("id") Long clientId,
                          @ModelAttribute("exam") @Valid Exam exam,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("client", clientService.getClientById(clientId));
            return "add-exam";
        }
        examService.addExam(exam, clientId);
        return "redirect:/clients/view?id=" + clientId;
    }

    @GetMapping("/edit")
    public String showEditExamForm(@RequestParam("id") Long examId, Model model) {
        Exam exam = examService.getExamById(examId);
        model.addAttribute("exam", exam);
        model.addAttribute("client",clientService.getClientById(examService.getClientIdByExamId(examId)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        if (exam.getDateOfReExamination() != null) {
            model.addAttribute("formattedDateOfReExamination", exam.getDateOfReExamination().format(formatter));
        }
        return "edit-exam";
    }

    @PostMapping("/edit")
    public String editExam(@ModelAttribute("exam") @Valid Exam exam, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-exam";
        }
        examService.updateExam(exam);
        return "redirect:/clients/view?id=" + examService.getExamById(exam.getId()).getClient().getId();
    }

    @GetMapping("/delete")
    public String deleteExam(@RequestParam("id") Long examId) {
        long clientId = examService.getClientIdByExamId(examId);
        examService.deleteExamById(examId);
        return "redirect:/clients/view?id=" + clientId;
    }
}
