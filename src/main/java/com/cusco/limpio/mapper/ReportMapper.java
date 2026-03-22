package com.cusco.limpio.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cusco.limpio.domain.model.Location;
import com.cusco.limpio.domain.model.Report;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.user.UserDTO;

@Component
public class ReportMapper {

    private final UserMapper userMapper;

    public ReportMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ReportDTO toDTO(Report report) {
        if (report == null) {
            return null;
        }

        UserDTO userDTO = report.getUser() == null ? null : userMapper.toDTO(report.getUser());

        return new ReportDTO(
                report.getId(),
                report.getTitle(),
                report.getDescription(),
                report.getStatus(),
                report.getCategory(),
                report.getPhotoUrls(),
                userDTO,
                toLocationDTO(report.getLocation()),
                report.getCreatedAt(),
                report.getUpdatedAt(),
                toStatusHistoryDTOs(report));
    }

    private ReportDTO.LocationDTO toLocationDTO(Location location) {
        if (location == null) {
            return null;
        }

        return new ReportDTO.LocationDTO(
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getDistrict());
    }

    private List<ReportDTO.StatusHistoryDTO> toStatusHistoryDTOs(Report report) {
        if (report.getStatusHistory() == null) {
            return List.of();
        }

        return report.getStatusHistory().stream()
                .map(history -> new ReportDTO.StatusHistoryDTO(
                        history.getStatus(),
                        history.getTimestamp(),
                        history.getNotes()))
                .toList();
    }
}
