package com.project.clinic.repositories;

import com.project.clinic.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
        @Query("SELECT e FROM Exam e WHERE e.dateLastExam BETWEEN :startDate AND :endDate")
        List<Exam> findExaminationsBetweenDates(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);
}