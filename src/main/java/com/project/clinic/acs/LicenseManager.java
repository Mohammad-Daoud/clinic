package com.project.clinic.acs;

import java.io.File;
import java.io.FileWriter;

public class LicenseManager {

    public static final String LICENSE_FILE_PATH = System.getProperty("user.dir") + "/app/license.txt";
    public static final  String LICENSE_FILE_NAME = "license.txt";
    private static final String LICENSE_DIR = System.getProperty("user.dir") + "/app";
    public static void createLicenseIfNotExists() {
        File licenseFile = new File(LICENSE_FILE_PATH);
        File licenseDir = new File(LICENSE_DIR);
        if (!licenseFile.exists()) {
            try {
                if (!licenseDir.exists()) {
                    licenseDir.mkdir();
                }
                String macAddress = MachineIdentifier.getMacAddress();
                String licenseKey = LicenseGenerator.generateLicenseKey(macAddress);

                // Write the generated license key to the license file
                FileWriter writer = new FileWriter(licenseFile);
                writer.write(licenseKey);
                writer.close();

                FileWriter dumpwriter = new FileWriter(LICENSE_FILE_NAME);
                dumpwriter.write(licenseKey);
                dumpwriter.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
