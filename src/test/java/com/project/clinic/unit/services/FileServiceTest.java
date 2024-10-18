package com.project.clinic.unit.services;

import com.project.clinic.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceTest {

    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @Test
    @DisplayName("Should prepare download headers with correct filename")
    void testPrepareDownloadHeaders() {
        // Given
        String filename = "testfile.txt";

        // When
        HttpHeaders headers = fileService.prepareDownloadHeaders(filename);

        // Then
        assertEquals("attachment; filename=testfile.txt", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    @DisplayName("Should load file as resource")
    void testLoadFileAsResource() throws IOException {
        // Given
        Path tempFilePath = Files.createTempFile("testfile", ".txt");
        try {
            Resource resource = fileService.loadFileAsResource(tempFilePath);

            // When
            Path resourcePath = Path.of(((PathResource) resource).getPath());

            // Then
            assertEquals(tempFilePath.toString(), resourcePath.toString());
        } finally {
            // Clean up the temporary file
            Files.deleteIfExists(tempFilePath);
        }
    }
}
