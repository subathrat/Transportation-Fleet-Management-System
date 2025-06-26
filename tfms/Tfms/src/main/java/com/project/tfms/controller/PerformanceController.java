package com.project.tfms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tfms.dto.FuelUsageDto;
import com.project.tfms.dto.MaintenanceCostDto;
import com.project.tfms.dto.TripSummaryDto;
import com.project.tfms.dto.VehiclePerformanceDto;
import com.project.tfms.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot auto-configures this for JSON

    @GetMapping
    public String viewDashboard(Model model) throws JsonProcessingException {
        // Fetch consolidated data for charts
        List<VehiclePerformanceDto> overallPerformanceData = performanceService.getOverallVehiclePerformanceData();

        // Convert the consolidated DTO list to JSON string to pass to JavaScript for Chart.js
        model.addAttribute("overallPerformanceDataJson", objectMapper.writeValueAsString(overallPerformanceData));



        return "performance-dashboard";
    }

    @GetMapping("/api/fuel-efficiency")
    @ResponseBody
    public List<FuelUsageDto> getFuelEfficiencyDataApi() {
        return performanceService.getFuelEfficiencyData();
    }

    @GetMapping("/api/trip-summary")
    @ResponseBody
    public List<TripSummaryDto> getTripSummaryDataApi() {
        return performanceService.getTripSummaryData();
    }

    @GetMapping("/api/maintenance-cost")
    @ResponseBody
    public List<MaintenanceCostDto> getMaintenanceCostDataApi() {
        return performanceService.getMaintenanceCostData();
    }

    // Endpoint to serve the consolidated DTO data as JSON
    @GetMapping("/api/overall-performance")
    @ResponseBody
    public List<VehiclePerformanceDto> getOverallPerformanceDataApi() {
        return performanceService.getOverallVehiclePerformanceData();
    }


    @GetMapping("/report/excel")
    public ResponseEntity<byte[]> downloadExcelReport() throws IOException {
        ByteArrayOutputStream excelStream = performanceService.generateOverallPerformanceExcelReport();

        String filename = "Fleet_Performance_Report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelStream.toByteArray());
    }
}