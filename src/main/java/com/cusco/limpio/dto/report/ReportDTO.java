package com.cusco.limpio.dto.report;

import com.cusco.limpio.domain.enums.ReportStatus;
import com.cusco.limpio.dto.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ReportDTO(
    Long id,
    String title,
    String description,
    ReportStatus status,
    String category,
    List<String> photoUrls,
    UserDTO user,
    LocationDTO location,
    LocalDateTime createdAt,
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
        LocalDateTime timestamp,
        String notes
    ) {}
}