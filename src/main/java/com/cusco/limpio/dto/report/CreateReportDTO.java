package com.cusco.limpio.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateReportDTO(
    @NotBlank String title,
    String description,
    @NotBlank String category,
    @NotNull Double latitude,
    @NotNull Double longitude,
    String address,
    String district,
    List<String> photoUrls
) {}