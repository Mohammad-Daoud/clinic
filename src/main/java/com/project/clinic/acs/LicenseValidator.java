package com.project.clinic.acs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LicenseValidator {
    public static boolean validateLicense() {
        try {
            String macAddress = MachineIdentifier.getMacAddress();
            String generatedLicenseKey = LicenseGenerator.generateLicenseKey(macAddress);
            String storedLicenseKey = new String(Files.readAllBytes(Paths.get(LicenseManager.LICENSE_FILE_PATH)));
            String rootLicenseKey = new String(Files.readAllBytes(Paths.get(LicenseManager.LICENSE_FILE_NAME)));
            return generatedLicenseKey.equals(storedLicenseKey) && generatedLicenseKey.equals(rootLicenseKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
