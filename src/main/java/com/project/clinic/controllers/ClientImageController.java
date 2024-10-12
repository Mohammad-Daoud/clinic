package com.project.clinic.controllers;

import com.project.clinic.models.Client;
import com.project.clinic.services.ClientImageService;
import com.project.clinic.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/clients/images")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ClientImageController {

    private final ClientImageService clientImageService;
    private final ClientService clientService;

    @Autowired
    public ClientImageController(ClientImageService clientImageService, ClientService clientService) {
        this.clientImageService = clientImageService;
        this.clientService = clientService;
    }

    @GetMapping("/edit")
    public String viewEditImagesPage(@RequestParam("id") Long clientId, Model model) {
        Client client = clientService.getClientById(clientId);
        model.addAttribute("client", client);
        return "edit-client-images";
    }

    @PostMapping("/add")
    public String addImages(@RequestParam("id") Long clientId,
                            @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {
        clientImageService.addImages(clientId, imageFiles);
        return "redirect:/clients/images/edit?id=" + clientId;
    }

    @PostMapping("/delete")
    public String deleteImage(@RequestParam("id") Long clientId,
                              @RequestParam("imageUrl") String imageUrl) {
        clientImageService.deleteImage(clientId, imageUrl);
        return "redirect:/clients/images/edit?id=" + clientId;
    }
}
