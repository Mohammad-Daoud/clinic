package com.project.clinic.integration.reopsitories;
import com.project.clinic.models.Client;
import com.project.clinic.models.Exam;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.repositories.ExamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ExamRepository examRepository;

    @Test
    @DisplayName("Test Search by phone number with existing results")
    public void testSearchByPhoneNumberExist() {
        // Given
        Client client1 = new Client();
        client1.setFirstName("Mohammad");
        client1.setLastName("Daoud");
        client1.setPhoneNumber("123456789");

        clientRepository.saveAll(Arrays.asList(client1));

        // When
        Pageable pageable = PageRequest.of(0, 10);
        var result = clientRepository.searchByPhoneNumber("123456789", pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(client1.getPhoneNumber(), result.getContent().get(0).getPhoneNumber());
    }


    @Test
    @DisplayName("Test Search By first and last name with ignore cases")
    public void testSearchByFirstAndLastName() {
        // Given
        Client client1 = new Client();
        client1.setFirstName("Mohammad");
        client1.setLastName("Daoud");

        clientRepository.save(client1);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        var result = clientRepository.searchByFirstAndLastName("Mohammad", "daoud", pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(client1.getFirstName(), result.getContent().get(0).getFirstName());
        assertEquals(client1.getLastName(), result.getContent().get(0).getLastName());
    }

    @Test
    @DisplayName("Test Search By today created client")
    public void testFindClientsCreatedToday() {
        // Given
        Client client1 = new Client();
        client1.setFirstName("Alice");
        client1.setLastName("Johnson");
        client1.setDateOfCreation(LocalDate.now());  // Created today
        clientRepository.save(client1);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        var result = clientRepository.findClientsWithExaminationsOrCreatedToday(LocalDate.now(), pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(client1.getFirstName(), result.getContent().get(0).getFirstName());
    }


    @Test
    @DisplayName("Test Search By today examination date")
    public void testFindClientsThatHaveTodayExamination() {
        // Given
        Client client1 = new Client();
        client1.setFirstName("Alice");
        client1.setLastName("Johnson");
        client1.setDateOfCreation(LocalDate.now().minusDays(1));
        clientRepository.save(client1);

        Exam exam = new Exam();
        exam.setClient(client1);
        exam.setDateLastExam(LocalDate.now().minusDays(2));
        exam.setDateOfReExamination(LocalDate.now());
        examRepository.save(exam);


        // When
        Pageable pageable = PageRequest.of(0, 10);
        var result = clientRepository.findClientsWithExaminationsOrCreatedToday(LocalDate.now(), pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(client1.getFirstName(), result.getContent().get(0).getFirstName());
    }

    @Test
    @DisplayName("Test default search")
    public void testDefaultSearch() {
        // Given
        Client client1 = new Client();
        client1.setFirstName("Bob");
        client1.setLastName("Marley");
        client1.setPhoneNumber("555123456");
        clientRepository.save(client1);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        var result = clientRepository.defaultSearch("Bob", pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(client1.getFirstName(), result.getContent().get(0).getFirstName());
    }
}
