package com.project.clinic.utils;

import com.project.clinic.models.Client;
import com.project.clinic.models.ClientStatus;
import com.project.clinic.models.Exam;

import java.util.List;

public class ClientsUtils {

    public static ClientStatus calculatePatientStatus(Client client) {
        List<Exam> examinations = client.getExams();

        if (examinations.isEmpty()) {
            // No examinations = Open status
            return ClientStatus.OPEN;
        }

        for (Exam exam : examinations) {
            if (exam.getDateOfReExamination() != null) {
                // If there's a next examination date = In Progress
                return ClientStatus.IN_PROGRESS;
            }
        }

        // If there are examinations but no next examination = Closed
        return ClientStatus.CLOSED;
    }
}
