package com.cusco.limpio.dto.report;

import jakarta.validation.constraints.*;

import java.util.List;

/**
 * DTO para creación de reportes con validaciones completas
 */
public record CreateReportDTO(
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 255, message = "El título debe tener entre 5 y 255 caracteres")
    String title,
    
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    String description,
    
    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 150, message = "La categoría no puede exceder 150 caracteres")
    String category,
    
    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
    Double latitude,
    
    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
    Double longitude,
    
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    String address,
    
    @Size(max = 200, message = "El distrito no puede exceder 200 caracteres")
    String district,
    
    List<String> photoUrls
) {}