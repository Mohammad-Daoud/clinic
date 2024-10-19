package com.project.clinic.acs;

import jakarta.validation.constraints.NotNull;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MachineIdentifier {

    @NotNull
    public static String getMacAddress() throws SocketException {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                byte[] mac = network.getHardwareAddress();

                if (mac != null) {
                    StringBuilder macAddress = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        macAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return macAddress.toString();
                }
            }

        return "null";
    }
}
