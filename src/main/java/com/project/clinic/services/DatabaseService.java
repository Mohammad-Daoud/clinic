package com.project.clinic.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class DatabaseService {

    private static final String DB_URL = "jdbc:h2:file:./data/clinicdb;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE";

    public Path performBackup() throws SQLException, IOException {
        String backupFilePath = "backup/clinicdb-backup.zip";
        ensureBackupDirectoryExists();
        try (Connection conn = DriverManager.getConnection(DB_URL, "root", "");
             Statement stmt = conn.createStatement()) {
            stmt.execute("BACKUP TO '" + backupFilePath + "'");
        }
        return Paths.get(backupFilePath);
    }

    public void restoreDatabase(MultipartFile dbFile) throws IOException {
        Path restorePath = Paths.get("data/clinicdb.mv.db");
        Files.copy(dbFile.getInputStream(), restorePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    private void ensureBackupDirectoryExists() throws IOException {
        Path backupDir = Paths.get("backup");
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }
    }
}
