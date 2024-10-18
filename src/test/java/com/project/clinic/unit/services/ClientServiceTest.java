package com.project.clinic.unit.services;

import com.project.clinic.exceptions.ClientNotFoundException;
import com.project.clinic.models.Client;
import com.project.clinic.models.ClientStatus;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getting all clients")
    public void testGetAllClients() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Mohammad");
        client.setLastName("Daoud");

        Page<Client> clientsPage = new PageImpl<>(Collections.singletonList(client));
        when(clientRepository.findAll(pageable)).thenReturn(clientsPage);

        // When
        Page<Client> result = clientService.getAllClients(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Mohammad", result.getContent().get(0).getFirstName());
    }

    @Test
    @DisplayName("Test client exists")
    public void testClientExists() {
        // Given
        when(clientRepository.findSimilarClients("Mohammad", "Daoud"))
                .thenReturn(Collections.singletonList(new Client()));

        // When
        boolean exists = clientService.clientExists("Mohammad", "", "", "Daoud");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Test adding a new client")
    public void testAddClient() {
        // Given
        Client client = new Client();
        client.setFirstName("Mohammad");
        client.setLastName("Daoud");

        // When
        clientService.addClient(client);

        // Then
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());
        assertEquals("Mohammad", clientCaptor.getValue().getFirstName());
        assertEquals(ClientStatus.OPEN, clientCaptor.getValue().getStatus());
        assertEquals(LocalDate.now(), clientCaptor.getValue().getDateOfCreation());
    }

    @Test
    @DisplayName("Test find client by id FOUND")
    public void testGetClientById_ClientFound() {
        // Given
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // When
        Client result = clientService.getClientById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Test find client by id NOT FOUND")
    public void testGetClientById_ClientNotFound() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    @DisplayName("Test delete client")
    public void testDeleteClientById() {
        // Given
        Long clientId = 1L;

        // When
        clientService.deleteClientById(clientId);

        // Then
        verify(clientRepository).deleteById(clientId);
    }

    @Test
    @DisplayName("Test update client")
    public void testUpdateClient_ClientExists() {
        // Given
        Client existingClient = new Client();
        existingClient.setId(1L);
        existingClient.setFirstName("Jane");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));

        Client updatedClient = new Client();
        updatedClient.setId(1L);
        updatedClient.setFirstName("Mohammad");

        // When
        clientService.updateClient(updatedClient);

        // Then
        verify(clientRepository).save(existingClient);
        assertEquals("Mohammad", existingClient.getFirstName());
    }

    @Test
    @DisplayName("Test update client - CLIENT NOT FOUND")
    public void testUpdateClient_ClientNotFound() {
        // Given
        Client updatedClient = new Client();
        updatedClient.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(updatedClient));
    }

    @Test
    @DisplayName("Test find clients with reexamination today")
    public void testGetClientsWithReExaminationToday() {
        // Given
        Pageable pageable = mock(Pageable.class);
        Client client = new Client();
        client.setId(1L);
        Page<Client> clientsPage = new PageImpl<>(Collections.singletonList(client));
        when(clientRepository.findClientsWithExaminationsOrCreatedToday(LocalDate.now(), pageable))
                .thenReturn(clientsPage);

        // When
        Page<Client> result = clientService.getClientsWithReExaminationToday(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Test name contains spaces")
    public void testNameContainsSpaces() {
        // Given
        String nameWithSpaces = "Mohammad Daoud";
        String nameWithoutSpaces = "Jane";

        // When
        String result = clientService.nameContainsSpaces(nameWithSpaces, nameWithoutSpaces);

        // Then
        assertEquals(nameWithSpaces, result);
    }
}
