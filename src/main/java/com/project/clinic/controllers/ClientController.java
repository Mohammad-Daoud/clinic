package com.project.clinic.controllers;

import com.project.clinic.models.Client;
import com.project.clinic.services.ClientImageService;
import com.project.clinic.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientImageService clientImageService;
    @Autowired
    public ClientController(ClientService clientService, ClientImageService clientImageService) {
        this.clientService = clientService;
        this.clientImageService = clientImageService;
    }

    @GetMapping
    public String viewAllClients(Model model,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Client> clientsPage = clientService.getClientsWithReExaminationToday(PageRequest.of(page, size));
        model.addAttribute("clientsPage", clientsPage);
        model.addAttribute("today",true);
        model.addAttribute("todayDate", LocalDate.now());
        model.addAttribute("clientExistsForDefaultPage",!clientsPage.isEmpty());
        return "clients";
    }

    @GetMapping("/add")
    public String showAddClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "add-client";
    }

    @PostMapping("/add")
    public String addClient(@ModelAttribute("client") @Valid Client client, BindingResult result,
                            @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {

        // Check if a client with the same name combination already exists
     /*   if (clientService.clientExists(client.getFirstName(), null,null, client.getLastName())) {
            result.rejectValue("firstName", "error.client", "A client with a similar name already exists.");
        }*/
        String firstName = client.getFirstName().trim();
        String lastName = client.getLastName().trim();
        String namesError = clientService.nameContainsSpaces(firstName, null, null, lastName);
        if (namesError != null) {
            result.rejectValue("firstName", "error.client", "Input value \""+namesError+"\" should not contain spaces.");
        }

        if (result.hasErrors()) {
            return "add-client";  // Return to the form if validation fails
        }

        clientService.addClient(client);
        clientImageService.addImages(client.getId(), imageFiles);
        return "redirect:/clients";
    }

    @GetMapping("/view")
    public String viewClient(@RequestParam("id") Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "view-client";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteClient(@RequestParam("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients";
    }

    @GetMapping("/edit")
    public String showEditClientForm(@RequestParam("id") Long clientId, Model model) {
        model.addAttribute("client", clientService.getClientById(clientId));
        return "edit-client";
    }

    @PostMapping("/edit")
    public String editClient(@ModelAttribute("client") @Valid Client client,
                             BindingResult result) throws IOException {
        String firstName = client.getFirstName().trim();
        String lastName = client.getLastName().trim();
        String namesError = clientService.nameContainsSpaces(firstName, null, null, lastName);
        if (namesError != null) {
            result.rejectValue("firstName", "error.client", "Input value \""+namesError+"\" should not contain spaces.");
        }
        if (result.hasErrors()) {
            return "edit-client";
        }
        clientService.updateClient(client);
        return "redirect:/clients/view?id=" + client.getId();
    }
}
