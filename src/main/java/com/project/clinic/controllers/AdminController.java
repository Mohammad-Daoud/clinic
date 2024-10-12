package com.project.clinic.controllers;

import com.project.clinic.models.User;
import com.project.clinic.services.DatabaseService;
import com.project.clinic.services.FileService;

import com.project.clinic.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {


    private final FileService fileService;
    private final DatabaseService databaseService;
    private final UserService userService;



    public AdminController(FileService fileService, DatabaseService databaseService, UserService userService) {
        this.fileService = fileService;
        this.databaseService = databaseService;
        this.userService = userService;
    }


    @PostMapping("/change-admin-password")
    public String changeAdminPassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {
        String currentUser = userService.getCurrentUsername();

        if (!userService.isPasswordValid(currentUser, currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
            return "redirect:/admin-actions";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match");
            return "redirect:/admin-actions";
        }

        userService.updatePassword(currentUser, newPassword);
        redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        return "redirect:/admin-actions";
    }


    @PostMapping("/backup-db")
    @ResponseBody
    public ResponseEntity<Resource> backupDatabase() {
        try {
            Path backupFile = databaseService.performBackup();
            HttpHeaders headers = fileService.prepareDownloadHeaders("clinicdb-backup.zip");
            Resource fileResource = fileService.loadFileAsResource(backupFile);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/restore-db")
    public String restoreDatabase(@RequestParam("dbFile") MultipartFile dbFile, Model model) {
        try {
            databaseService.restoreDatabase(dbFile);
            model.addAttribute("restoreSuccess", true);
            System.exit(0); // Simulate application restart
        } catch (IOException e) {
            model.addAttribute("error", "Failed to restore database.");
        }
        return "redirect:/admin-actions?restoreSuccess=true";
    }

    @GetMapping("/admin-actions")
    public String showAdminActions() {
        return "admin-actions";
    }

    @GetMapping("/exit")
    public void exit(HttpSession session) {
        session.invalidate();
        System.exit(0);
    }


    @GetMapping("/manage-users")
    public String showUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "manage-users"; // Name of the Thymeleaf template
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("username") String username,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/manage-users";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
            return "redirect:/manage-users";
        }

        user.setPassword(newPassword);
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Password updated successfully for user: " + username);
        return "redirect:/manage-users";
    }
}
