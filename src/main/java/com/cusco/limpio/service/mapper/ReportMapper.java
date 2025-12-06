package com.cusco.limpio.service.mapper;

import com.cusco.limpio.domain.model.Report;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.user.UserDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReportMapper {

    private final UserMapper userMapper;

    public ReportMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ReportDTO toDTO(Report report) {
        if (report == null) return null;

        ReportDTO.LocationDTO location = new ReportDTO.LocationDTO(
                report.getLocation() == null ? null : report.getLocation().getLatitude(),
                report.getLocation() == null ? null : report.getLocation().getLongitude(),
                report.getLocation() == null ? null : report.getLocation().getAddress(),
                report.getLocation() == null ? null : report.getLocation().getDistrict()
        );

        var statusHistory = report.getStatusHistory() == null ? java.util.List.of() :
                report.getStatusHistory().stream()
                        .map(h -> new ReportDTO.StatusHistoryDTO(h.getStatus(), h.getTimestamp(), h.getNotes()))
                        .collect(Collectors.toList());

        UserDTO userDTO = report.getUser() == null ? null : userMapper.toDTO(report.getUser());

        return new ReportDTO(
                report.getId(),
                report.getTitle(),
                report.getDescription(),
                report.getStatus(),
                report.getCategory(),
                report.getPhotoUrls(),
                userDTO,
                location,
                report.getCreatedAt(),
                report.getUpdatedAt(),
                statusHistory
        );
    }
}
