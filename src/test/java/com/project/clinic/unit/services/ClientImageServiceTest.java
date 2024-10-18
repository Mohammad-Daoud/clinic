package com.project.clinic.unit.services;

import com.project.clinic.exceptions.ClientNotFoundException;
import com.project.clinic.models.Client;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.services.ClientImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientImageServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientImageService clientImageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save client image successfully")
    public void testSaveClientImage() throws IOException {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(new byte[0]));

        // When
        String result = clientImageService.saveClientImage(mockFile);

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("/images/"));
        assertTrue(new File("upload/images").exists());
    }

    @Test
    @DisplayName("Should delete image when client is found")
    public void testDeleteImage_ClientFound() throws IOException {
        // Given
        Long clientId = 1L;
        String imageUrl = "/images/test.jpg";

        // Create a modifiable list to hold image URLs
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(imageUrl);

        Client client = new Client();
        client.setId(clientId);
        client.setImageUrls(imageUrls); // Use the modifiable list

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // When
        clientImageService.deleteImage(clientId, imageUrl);

        // Then
        assertTrue(client.getImageUrls().isEmpty());
        verify(clientRepository).save(client);

        // Clean up - delete the mock file if it was created
        Path fileToDelete = Path.of("upload/images/test.jpg");
        Files.deleteIfExists(fileToDelete);

    }


    @Test
    @DisplayName("Should throw ClientNotFoundException when deleting image from non-existent client")
    public void testDeleteImage_ClientNotFound() {
        // Given
        Long clientId = 1L;
        String imageUrl = "/images/test.jpg";

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ClientNotFoundException.class, () -> clientImageService.deleteImage(clientId, imageUrl));
    }

    @Test
    @DisplayName("Should add multiple images to client when client is found")
    public void testAddImages_ClientFound() throws IOException {
        // Given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        // Initialize with an ArrayList to allow modifications
        client.setImageUrls(new ArrayList<>());

        MultipartFile mockFile1 = mock(MultipartFile.class);
        when(mockFile1.getOriginalFilename()).thenReturn("image1.jpg");
        when(mockFile1.isEmpty()).thenReturn(false);
        when(mockFile1.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(new byte[0]));

        MultipartFile mockFile2 = mock(MultipartFile.class);
        when(mockFile2.getOriginalFilename()).thenReturn("image2.jpg");
        when(mockFile2.isEmpty()).thenReturn(false);
        when(mockFile2.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(new byte[0]));

        List<MultipartFile> files = List.of(mockFile1, mockFile2);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // When
        clientImageService.addImages(clientId, files);

        // Then
        assertEquals(2, client.getImageUrls().size());
        assertTrue(client.getImageUrls().get(0).contains("image1.jpg"));
        assertTrue(client.getImageUrls().get(1).contains("image2.jpg"));
        verify(clientRepository).save(client);
    }


    @Test
    @DisplayName("Should throw ClientNotFoundException when adding images to non-existent client")
    public void testAddImages_ClientNotFound() throws IOException {
        // Given
        Long clientId = 1L;
        List<MultipartFile> files = Collections.emptyList();

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ClientNotFoundException.class, () -> clientImageService.addImages(clientId, files));
    }
}

