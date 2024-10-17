package com.project.clinic.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
