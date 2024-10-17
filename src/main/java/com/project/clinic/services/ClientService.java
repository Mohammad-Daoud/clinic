package com.project.clinic.services;

import com.project.clinic.exceptions.ClientNotFoundException;
import com.project.clinic.models.Client;
import com.project.clinic.models.ClientStatus;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.utils.ClientsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientService {


    private final ClientRepository clientRepository;


    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public boolean clientExists(String firstName, String secondName, String thirdName, String lastName) {
        List<Client> similarClients = clientRepository.findSimilarClients(
                firstName.trim(),
                lastName.trim());

        return !similarClients.isEmpty();
    }
    public void addClient(Client client) {
        client.setDateOfCreation(LocalDate.now());
        client.setStatus(ClientStatus.OPEN);
        clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(()-> new ClientNotFoundException(id));
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(Client client) {
        Client existingClient = clientRepository.findById(client.getId())
                .orElseThrow(() -> new ClientNotFoundException(client.getId()));

        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setPhoneNumber(client.getPhoneNumber());
        existingClient.setAge(client.getAge());
        existingClient.setImageUrls(client.getImageUrls());
        existingClient.setStatus(ClientsUtils.calculatePatientStatus(existingClient));
        clientRepository.save(existingClient);
    }

    public Page<Client> getClientsWithReExaminationToday(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return clientRepository.findClientsWithExaminationsOrCreatedToday(today, pageable);
    }

    public String nameContainsSpaces(String ... names) {
        for (String name : names) {
            if (name != null && name.contains(" ")) {
                return name;
            }
        }
        return null;
    }
}
