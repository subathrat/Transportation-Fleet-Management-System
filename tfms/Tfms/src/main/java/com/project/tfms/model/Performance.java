package com.project.tfms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tfms")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reportType;

    @Lob // Use @Lob for large string data (e.g., JSON)
    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    private LocalDateTime generationTimestamp; // When this performance snapshot was generated

    // Default constructor for JPA
    public Performance() {
        this.generationTimestamp = LocalDateTime.now(); // Set default on creation
    }

    public Performance(String reportType, String data) {
        this.reportType = reportType;
        this.data = data;
        this.generationTimestamp = LocalDateTime.now(); // Automatically set timestamp
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getGenerationTimestamp() {
        return generationTimestamp;
    }

    public void setGenerationTimestamp(LocalDateTime generationTimestamp) {
        this.generationTimestamp = generationTimestamp;
    }

    @PrePersist
    protected void onCreate() {
        if (generationTimestamp == null) {
            generationTimestamp = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Performance{" +
                "id=" + id +
                ", reportType='" + reportType + '\'' +
                ", generationTimestamp=" + generationTimestamp +
                '}';
    }
}