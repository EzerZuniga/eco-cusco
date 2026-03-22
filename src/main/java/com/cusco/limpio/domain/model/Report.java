package com.cusco.limpio.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cusco.limpio.domain.enums.ReportStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Campo de versión para control de concurrencia optimista (optimistic locking).
     * JPA lanzará OptimisticLockException si dos transacciones intentan
     * modificar el mismo reporte simultáneamente.
     */
    @Version
    private Long version;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ReportStatus status;

    @Column(nullable = false, length = 150)
    private String category;

    @ElementCollection
    @CollectionTable(name = "report_photo_urls", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "photo_urls", length = 2000)
    private List<String> photoUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(name = "report_status_history", joinColumns = @JoinColumn(name = "report_id"))
    private List<StatusHistory> statusHistory = new ArrayList<>();

    public Report() {
    }

    public Report(Long id, Long version, String title, String description, ReportStatus status, String category,
            List<String> photoUrls, User user, Location location, LocalDateTime createdAt, LocalDateTime updatedAt,
            List<StatusHistory> statusHistory) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.description = description;
        this.status = status;
        this.category = category;
        this.photoUrls = photoUrls == null ? new ArrayList<>() : new ArrayList<>(photoUrls);
        this.user = user;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.statusHistory = statusHistory == null ? new ArrayList<>() : new ArrayList<>(statusHistory);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls == null ? new ArrayList<>() : new ArrayList<>(photoUrls);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<StatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<StatusHistory> statusHistory) {
        this.statusHistory = statusHistory == null ? new ArrayList<>() : new ArrayList<>(statusHistory);
    }

    @Embeddable
    public static class StatusHistory {
        @Enumerated(EnumType.STRING)
        @Column(name = "status", length = 50)
        private ReportStatus status;

        @Column(name = "timestamp")
        private LocalDateTime timestamp;

        @Column(name = "notes", length = 1000)
        private String notes;

        public StatusHistory() {
        }

        public StatusHistory(ReportStatus status, LocalDateTime timestamp, String notes) {
            this.status = status;
            this.timestamp = timestamp;
            this.notes = notes;
        }

        public static StatusHistoryBuilder builder() {
            return new StatusHistoryBuilder();
        }

        public ReportStatus getStatus() {
            return status;
        }

        public void setStatus(ReportStatus status) {
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    public static final class Builder {
        private Long id;
        private Long version;
        private String title;
        private String description;
        private ReportStatus status;
        private String category;
        private List<String> photoUrls;
        private User user;
        private Location location;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<StatusHistory> statusHistory;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(ReportStatus status) {
            this.status = status;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder photoUrls(List<String> photoUrls) {
            this.photoUrls = photoUrls;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder statusHistory(List<StatusHistory> statusHistory) {
            this.statusHistory = statusHistory;
            return this;
        }

        public Report build() {
            return new Report(id, version, title, description, status, category, photoUrls, user, location,
                    createdAt, updatedAt, statusHistory);
        }
    }

    public static final class StatusHistoryBuilder {
        private ReportStatus status;
        private LocalDateTime timestamp;
        private String notes;

        private StatusHistoryBuilder() {
        }

        public StatusHistoryBuilder status(ReportStatus status) {
            this.status = status;
            return this;
        }

        public StatusHistoryBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public StatusHistoryBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public StatusHistory build() {
            return new StatusHistory(status, timestamp, notes);
        }
    }
}
