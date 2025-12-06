package com.cusco.limpio.repository;

import com.cusco.limpio.domain.model.Report;
import com.cusco.limpio.domain.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserId(Long userId);
    List<Report> findByStatus(ReportStatus status);
    
    @Query("SELECT r FROM Report r WHERE r.location.district = :district")
    List<Report> findByDistrict(String district);
    
    @Query("SELECT r FROM Report r ORDER BY r.createdAt DESC")
    List<Report> findAllOrderByCreatedAtDesc();
}