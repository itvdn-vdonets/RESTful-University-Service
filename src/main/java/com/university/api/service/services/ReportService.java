package com.university.api.service.services;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ReportService {
    void generateReport() throws IOException;
}
