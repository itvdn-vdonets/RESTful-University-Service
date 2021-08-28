package com.university.api.service.utils;

import com.university.api.service.models.Course;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CsvReportWriter {
    private static final String fileName = "report/report.csv";
    private static final String CSV_SEPARATOR = ",";
    private static final List<List<String>> headers = Collections.singletonList(
            Arrays.asList("id", "name", "startData", "endDate", "numberOfStudentsEnrolled", "professor"));

    public static void writeReportToCsv(List<Course> courses) throws IOException {
        try (FileWriter csvWriter = new FileWriter(fileName)) {
            for (List<String> rowData : headers) {
                csvWriter.append(String.join(CSV_SEPARATOR, rowData));
                csvWriter.append("\n");
            }
            for (Course c : courses) {
                csvWriter.append(c.getId().toString());
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append(c.getCourseName());
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append(c.getStartDate().toString());
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append(c.getEndDate().toString());
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append(String.valueOf(c.getStudentsOnTheCourse().size()));
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append(c.getProfessor().toString());
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append("\n");
            }
            csvWriter.flush();
        }
    }
}
