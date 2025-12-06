package com.cusco.limpio.service;

import com.cusco.limpio.dto.report.CreateReportDTO;
import com.cusco.limpio.dto.report.ReportDTO;
import com.cusco.limpio.dto.report.UpdateStatusDTO;

import java.util.List;

public interface ReportService {
    ReportDTO createReport(CreateReportDTO createReportDTO, Long userId);
    ReportDTO getReportById(Long id);
    List<ReportDTO> getAllReports();
    List<ReportDTO> getReportsByUser(Long userId);
    List<ReportDTO> getReportsByStatus(String status);
    List<ReportDTO> getReportsByDistrict(String district);
    ReportDTO updateReportStatus(Long id, UpdateStatusDTO updateStatusDTO);
    void deleteReport(Long id);
}