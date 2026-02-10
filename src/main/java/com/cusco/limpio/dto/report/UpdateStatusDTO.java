package com.cusco.limpio.dto.report;

import com.cusco.limpio.domain.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateStatusDTO(
    @NotNull(message = "El estado es obligatorio")
    ReportStatus status,
    
    @Size(max = 1000, message = "Las notas no pueden exceder 1000 caracteres")
    String notes
) {}