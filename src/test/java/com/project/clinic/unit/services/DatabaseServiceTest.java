package com.project.clinic.unit.services;


import com.project.clinic.services.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseServiceTest {

    @InjectMocks
    private DatabaseService databaseService;

    @Mock
    private MultipartFile mockDbFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should perform backup and return backup file path")
    void testPerformBackup() throws SQLException, IOException {
        // Given
        String expectedBackupFilePath = "backup/clinicdb-backup.zip";

        // When
        Path backupPath = databaseService.performBackup();

        // Then
        assertEquals(expectedBackupFilePath, backupPath.toString());
        // Verify that the backup directory is created
        assertTrue(Files.exists(Path.of("backup"))); // Check if the backup directory was created
        // Additional checks on the database backup logic can be added here
    }

    @Test
    @DisplayName("Should restore database from given db file")
    void testRestoreDatabase() throws IOException {
        // Given
        String mockFileName = "mockDatabase.mv.db";
        byte[] mockFileContent = "mock database content".getBytes();
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(mockFileContent);

        when(mockDbFile.getInputStream()).thenReturn(mockInputStream);
        when(mockDbFile.getOriginalFilename()).thenReturn(mockFileName);

        // When
        databaseService.restoreDatabase(mockDbFile);

        // Then
        Path restoredDbPath = Path.of("data/clinicdb.mv.db");
        assertTrue(Files.exists(restoredDbPath)); // Check if the file was created

        // Cleanup: Delete the restored database file after the test
        Files.deleteIfExists(restoredDbPath);
    }

    @Test
    @DisplayName("Should throw IOException when restoring database fails")
    void testRestoreDatabaseIOException() throws IOException {
        // Given
        when(mockDbFile.getInputStream()).thenThrow(new IOException("File not found"));

        // When & Then
        assertThrows(IOException.class, () -> databaseService.restoreDatabase(mockDbFile));
    }
}
