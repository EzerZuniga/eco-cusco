package com.cusco.limpio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cusco.limpio.dto.report.CreateReportDTO;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.report.UpdateStatusDTO;
import com.cusco.limpio.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Crea un nuevo reporte.
     * El usuario autor se extrae del token JWT para evitar suplantación de
     * identidad.
     * Retorna 201 Created con el recurso creado.
     */
    @PostMapping
    public ResponseEntity<ReportDTO> createReport(
            @Validated @RequestBody CreateReportDTO dto,
            Authentication authentication) {
        // authentication.getName() retorna el email del usuario autenticado (subject
        // del JWT)
        ReportDTO created = reportService.createReport(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> listAll() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDTO>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReportDTO>> byStatus(@PathVariable String status) {
        return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    @GetMapping("/district/{district}")
    public ResponseEntity<List<ReportDTO>> byDistrict(@PathVariable String district) {
        return ResponseEntity.ok(reportService.getReportsByDistrict(district));
    }

    /**
     * Actualiza el estado de un reporte.
     * Solo ADMIN o MUNICIPAL_AGENT pueden cambiar estados.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MUNICIPAL_AGENT')")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long id,
            @Validated @RequestBody UpdateStatusDTO dto) {
        return ResponseEntity.ok(reportService.updateReportStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
