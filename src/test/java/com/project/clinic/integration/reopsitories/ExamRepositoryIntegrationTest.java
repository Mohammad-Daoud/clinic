package com.project.clinic.integration.reopsitories;

import com.project.clinic.models.Client;
import com.project.clinic.models.Exam;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.repositories.ExamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")  // Ensure the test profile is used
public class ExamRepositoryIntegrationTest {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ClientRepository clientRepository;


    @Test
    @DisplayName("Test find exams between dates")
    public void testFindExaminationsBetweenDates() {
        // Given: Create and save a few Exam objects with different dates

        Client client1 = new Client();
        client1.setFirstName("Mohammad");
        client1.setLastName("Daoud");
        client1.setPhoneNumber("123456789");
        Exam exam1 = new Exam();
        exam1.setDateLastExam(LocalDate.of(2023, 6, 15));
        exam1.setClient(client1);
        clientRepository.save(client1);
        examRepository.save((exam1));

        // When: Query for exams between certain dates
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 1);
        List<Exam> foundExams = examRepository.findExaminationsBetweenDates(startDate, endDate);

        // Then: Assert that the correct exams were found
        assertNotNull(foundExams);
        assertEquals(1, foundExams.size());  // Only exam2 falls in the date range
        assertEquals(LocalDate.of(2023, 6, 15), foundExams.get(0).getDateLastExam());
    }
}
