package com.project.clinic.utils;

public class StringUtils {

    public static String capitalizeFirstWordInLine(String input) {
        String[] lines = input.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (!line.isEmpty()) {
                result.append(Character.toUpperCase(line.charAt(0)))
                        .append(line.substring(1))
                        .append("\n");
            }
        }

        return result.toString().trim();  // Trim to remove last extra newline
    }
}
