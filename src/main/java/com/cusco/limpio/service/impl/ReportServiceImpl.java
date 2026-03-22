package com.cusco.limpio.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
import com.cusco.limpio.exception.UnauthorizedException;
import com.cusco.limpio.mapper.ReportMapper;
import com.cusco.limpio.repository.LocationRepository;
import com.cusco.limpio.repository.ReportRepository;
import com.cusco.limpio.repository.UserRepository;
import com.cusco.limpio.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ReportMapper reportMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository,
            LocationRepository locationRepository, ReportMapper reportMapper,
            ApplicationEventPublisher eventPublisher) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.reportMapper = reportMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ReportDTO createReport(CreateReportDTO dto, String authenticatedEmail) {
        // El autor del reporte siempre se resuelve desde el contexto autenticado.
        String normalizedEmail = normalizeAuthenticatedEmail(authenticatedEmail);
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + authenticatedEmail));

        Location location = locationRepository.save(buildLocation(dto));
        LocalDateTime now = LocalDateTime.now();

        Report report = reportRepository.save(
                Report.builder()
                        .title(dto.title())
                        .description(dto.description())
                        .category(dto.category())
                        .photoUrls(dto.photoUrls() == null ? List.of() : new ArrayList<>(dto.photoUrls()))
                        .user(user)
                        .location(location)
                        .status(ReportStatus.PENDIENTE)
                        .createdAt(now)
                        .build());

        // Evento de dominio listo para notificaciones, auditoría o integraciones futuras.
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
        LocalDateTime now = LocalDateTime.now();

        report.setStatus(dto.status());
        report.setUpdatedAt(now);
        appendStatusHistory(report, dto, now);

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

    private Location buildLocation(CreateReportDTO dto) {
        return Location.builder()
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .address(dto.address())
                .district(dto.district())
                .build();
    }

    private void appendStatusHistory(Report report, UpdateStatusDTO dto, LocalDateTime timestamp) {
        if (report.getStatusHistory() == null) {
            report.setStatusHistory(new ArrayList<>());
        }

        report.getStatusHistory().add(
                Report.StatusHistory.builder()
                        .status(dto.status())
                        .timestamp(timestamp)
                        .notes(dto.notes())
                        .build());
    }

    private String normalizeAuthenticatedEmail(String authenticatedEmail) {
        if (!StringUtils.hasText(authenticatedEmail)) {
            throw new UnauthorizedException("Usuario autenticado no disponible");
        }

        return authenticatedEmail.trim().toLowerCase(Locale.ROOT);
    }
}
