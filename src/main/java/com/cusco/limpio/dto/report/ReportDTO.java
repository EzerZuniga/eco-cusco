package com.cusco.limpio.dto.report;

import com.cusco.limpio.domain.enums.ReportStatus;
import com.cusco.limpio.dto.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReportDTO(
    Long id,
    String title,
    String description,
    ReportStatus status,
    String category,
    List<String> photoUrls,
    UserDTO user,
    LocationDTO location,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt,
    List<StatusHistoryDTO> statusHistory
) {
    public record LocationDTO(
        Double latitude,
        Double longitude,
        String address,
        String district
    ) {}
    
    public record StatusHistoryDTO(
        ReportStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        String notes
    ) {}
}