package com.project.clinic.controllers;

import com.project.clinic.models.Client;
import com.project.clinic.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public String searchClients(@RequestParam("query") String query,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                Model model) {
        Page<Client> clientsPage = searchService.searchClients(query, PageRequest.of(page, size));
        model.addAttribute("clientsPage", clientsPage);
        model.addAttribute("query", query);
        return "clients";
    }
}
