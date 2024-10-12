package com.project.clinic.services;

import com.project.clinic.models.Client;
import com.project.clinic.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final ClientRepository clientRepository;

    @Autowired
    public SearchService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> searchClients(String query, Pageable pageable) {
        query = query.trim();

        if (isNumeric(query)) {
            // Check if the query is numeric, and based on the length, determine if it's an ID or phone number
            if (query.length() == 10 || query.length() == 11) {
                // Assuming phone number is 10 or 11 digits long
                return clientRepository.searchByPhoneNumber(query, pageable);
            } else {
                // Otherwise, assume it's an ID
                return clientRepository.searchById(Long.valueOf(query), pageable);
            }
        } else {
            // Split the input into words (names)
            String[] nameParts = query.split("\\s+");

            // Search based on how many names are provided
            switch (nameParts.length) {
                case 1:
                    return clientRepository.searchByFirstNameOrLastName(nameParts[0], pageable);
                case 2:
                    return clientRepository.searchByFirstAndLastName(nameParts[0], nameParts[1], pageable);
                default:
                    return clientRepository.defaultSearch(query,pageable); // Default case if query doesn't match criteria
            }
        }
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+"); // Matches any string of digits
    }
}
