package com.project.clinic.unit.services;


import com.project.clinic.exceptions.ClientNotFoundException;
import com.project.clinic.exceptions.ExamNotFoundException;
import com.project.clinic.models.Client;
import com.project.clinic.models.Exam;
import com.project.clinic.models.PaymentType;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.repositories.ExamRepository;
import com.project.clinic.services.ExamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamServiceTest {

    @InjectMocks
    private ExamService examService;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ClientRepository clientRepository;

    private Exam mockExam;
    private Client mockClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockClient = new Client();
        mockClient.setId(1L);
        mockExam = new Exam();
        mockExam.setId(1L);
        mockExam.setClient(mockClient);
    }

    @Test
    @DisplayName("Should return exam by ID when found")
    void testGetExamById_ExamFound() {
        // Given
        when(examRepository.findById(1L)).thenReturn(Optional.of(mockExam));

        // When
        Exam foundExam = examService.getExamById(1L);

        // Then
        assertNotNull(foundExam);
        assertEquals(mockExam.getId(), foundExam.getId());
    }

    @Test
    @DisplayName("Should throw ExamNotFoundException when exam is not found")
    void testGetExamById_ExamNotFound() {
        // Given
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ExamNotFoundException.class, () -> examService.getExamById(1L));
    }

    @Test
    @DisplayName("Should add exam when client is found")
    void testAddExam_ClientFound() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        mockExam.setPrice(new BigDecimal(100));

        // When
        examService.addExam(mockExam, 1L);

        // Then
        verify(examRepository).save(mockExam);
        assertEquals(LocalDate.now(), mockExam.getDateLastExam());
        assertEquals(PaymentType.Cash, mockExam.getPaymentType());
        assertEquals(mockClient, mockExam.getClient());
    }

    @Test
    @DisplayName("Should throw ClientNotFoundException when adding exam with non-existing client")
    void testAddExam_ClientNotFound() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ClientNotFoundException.class, () -> examService.addExam(mockExam, 1L));
    }

    @Test
    @DisplayName("Should update exam details when exam is found")
    void testUpdateExam_ExamFound() {
        // Given
        when(examRepository.findById(1L)).thenReturn(Optional.of(mockExam));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        Exam updatedExam = new Exam();
        updatedExam.setId(1L);
        updatedExam.setPrice(new BigDecimal(150));

        // When
        examService.updateExam(updatedExam);

        // Then
        verify(examRepository).save(mockExam);
        assertEquals(new BigDecimal(150), mockExam.getPrice());
    }

    @Test
    @DisplayName("Should throw ExamNotFoundException when updating non-existing exam")
    void testUpdateExam_ExamNotFound() {
        // Given
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ExamNotFoundException.class, () -> examService.updateExam(mockExam));
    }

    @Test
    @DisplayName("Should delete exam by ID when found")
    void testDeleteExamById_ExamFound() {
        // Given
        when(examRepository.existsById(1L)).thenReturn(true);
        when(examRepository.findById(1L)).thenReturn(Optional.of(mockExam));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        // When
        examService.deleteExamById(1L);

        // Then
        verify(examRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ExamNotFoundException when deleting non-existing exam")
    void testDeleteExamById_ExamNotFound() {
        // Given
        when(examRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(ExamNotFoundException.class, () -> examService.deleteExamById(1L));
    }

    @Test
    @DisplayName("Should find examinations between given dates")
    void testFindExaminationsBetweenDates() {
        // Given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        when(examRepository.findExaminationsBetweenDates(startDate, endDate)).thenReturn(List.of(mockExam));

        // When
        List<Exam> exams = examService.findExaminationsBetweenDates(startDate, endDate);

        // Then
        assertFalse(exams.isEmpty());
        assertEquals(1, exams.size());
    }
}
