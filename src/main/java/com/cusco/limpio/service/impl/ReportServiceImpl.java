package com.cusco.limpio.service.impl;

import com.cusco.limpio.domain.enums.ReportStatus;
import com.cusco.limpio.domain.model.Report;
import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.report.CreateReportDTO;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.report.UpdateStatusDTO;
import com.cusco.limpio.exception.ResourceNotFoundException;
import com.cusco.limpio.repository.LocationRepository;
import com.cusco.limpio.repository.ReportRepository;
import com.cusco.limpio.repository.UserRepository;
import com.cusco.limpio.service.ReportService;
import com.cusco.limpio.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportDTO createReport(CreateReportDTO createReportDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        var location = com.cusco.limpio.domain.model.Location.builder()
                .latitude(createReportDTO.latitude())
                .longitude(createReportDTO.longitude())
                .address(createReportDTO.address())
                .district(createReportDTO.district())
                .build();

        location = locationRepository.save(location);

        Report report = Report.builder()
                .title(createReportDTO.title())
                .description(createReportDTO.description())
                .category(createReportDTO.category())
                .photoUrls(createReportDTO.photoUrls() == null ? List.of() : createReportDTO.photoUrls())
                .user(user)
                .location(location)
                .status(ReportStatus.PENDIENTE)
                .createdAt(LocalDateTime.now())
                .build();

        Report saved = reportRepository.save(report);
        return reportMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return reportMapper.toDTO(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAllOrderByCreatedAtDesc().stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByUser(Long userId) {
        return reportRepository.findByUserId(userId).stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByStatus(String status) {
        ReportStatus rs;
        try {
            rs = ReportStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        return reportRepository.findByStatus(rs).stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByDistrict(String district) {
        return reportRepository.findByDistrict(district).stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public ReportDTO updateReportStatus(Long id, UpdateStatusDTO updateStatusDTO) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        report.setStatus(updateStatusDTO.status());
        report.setUpdatedAt(LocalDateTime.now());

        report.getStatusHistory().add(
                Report.StatusHistory.builder()
                        .status(updateStatusDTO.status())
                        .timestamp(LocalDateTime.now())
                        .notes(updateStatusDTO.notes())
                        .build()
        );

        Report updated = reportRepository.save(report);
        return reportMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }
}
