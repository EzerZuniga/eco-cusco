package com.cusco.limpio.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cusco.limpio.domain.enums.ReportStatus;
import com.cusco.limpio.domain.events.ReportCreatedEvent;
import com.cusco.limpio.domain.model.Location;
import com.cusco.limpio.domain.model.Report;
import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.report.CreateReportDTO;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.report.UpdateStatusDTO;
import com.cusco.limpio.exception.ResourceNotFoundException;
import com.cusco.limpio.mapper.ReportMapper;
import com.cusco.limpio.repository.LocationRepository;
import com.cusco.limpio.repository.ReportRepository;
import com.cusco.limpio.repository.UserRepository;
import com.cusco.limpio.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ReportMapper reportMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ReportDTO createReport(CreateReportDTO dto, String authenticatedEmail) {
        // El usuario siempre se obtiene del token JWT, nunca del cliente
        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + authenticatedEmail));

        Location location = locationRepository.save(
                Location.builder()
                        .latitude(dto.latitude())
                        .longitude(dto.longitude())
                        .address(dto.address())
                        .district(dto.district())
                        .build());

        Report report = reportRepository.save(
                Report.builder()
                        .title(dto.title())
                        .description(dto.description())
                        .category(dto.category())
                        .photoUrls(dto.photoUrls() == null ? List.of() : dto.photoUrls())
                        .user(user)
                        .location(location)
                        .status(ReportStatus.PENDIENTE)
                        .createdAt(LocalDateTime.now())
                        .build());

        // Publicar evento de dominio para integraciones futuras (notificaciones,
        // auditoría, etc.)
        eventPublisher.publishEvent(
                ReportCreatedEvent.of(report.getId(), user.getId(), report.getTitle(),
                        report.getCategory(), location.getDistrict()));

        return reportMapper.toDTO(report);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportById(Long id) {
        return reportMapper.toDTO(findReportOrThrow(id));
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
        ReportStatus rs = ReportStatus.from(status); // usa el parser con mensajes claros
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
    public ReportDTO updateReportStatus(Long id, UpdateStatusDTO dto) {
        Report report = findReportOrThrow(id);

        report.setStatus(dto.status());
        report.setUpdatedAt(LocalDateTime.now());
        report.getStatusHistory().add(
                Report.StatusHistory.builder()
                        .status(dto.status())
                        .timestamp(LocalDateTime.now())
                        .notes(dto.notes())
                        .build());

        return reportMapper.toDTO(reportRepository.save(report));
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reporte no encontrado con id: " + id);
        }
        reportRepository.deleteById(id);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private Report findReportOrThrow(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));
    }
}
