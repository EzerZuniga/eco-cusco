package com.cusco.limpio.service;

import java.util.List;

import com.cusco.limpio.dto.report.CreateReportDTO;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.report.UpdateStatusDTO;

public interface ReportService {

    /**
     * Crea un nuevo reporte asociado al usuario autenticado.
     *
     * @param createReportDTO    datos del reporte
     * @param authenticatedEmail email extraído del token JWT (nunca del cliente)
     */
    ReportDTO createReport(CreateReportDTO createReportDTO, String authenticatedEmail);

    ReportDTO getReportById(Long id);

    List<ReportDTO> getAllReports();

    List<ReportDTO> getReportsByUser(Long userId);

    List<ReportDTO> getReportsByStatus(String status);

    List<ReportDTO> getReportsByDistrict(String district);

    ReportDTO updateReportStatus(Long id, UpdateStatusDTO updateStatusDTO);

    void deleteReport(Long id);
}
