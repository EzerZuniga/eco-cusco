package com.cusco.limpio.dto.report;

import com.cusco.limpio.domain.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusDTO(
    @NotNull ReportStatus status,
    String notes
) {}