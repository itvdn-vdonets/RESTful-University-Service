package com.university.api.service.controllers;

import com.university.api.service.services.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public void findAllActiveCourses() {
        try {
            reportService.generateReport();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
