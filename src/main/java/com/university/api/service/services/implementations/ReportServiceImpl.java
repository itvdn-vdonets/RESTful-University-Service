package com.university.api.service.services.implementations;

import com.university.api.service.models.Course;
import com.university.api.service.services.ReportService;
import com.university.api.service.utils.CsvReportWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


@Service
public class ReportServiceImpl implements ReportService {
    private final static int NUMBERS_OF_STUDENTS = 2;
    private final MongoTemplate mongoTemplate;

    public ReportServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void generateReport() throws IOException {
        List<Course> actualCourses = filterCoursesByNumberOfStudents(findActualCourses());
        CsvReportWriter.writeReportToCsv(actualCourses);
    }

    private List<Course> findActualCourses() {
        AggregationOperation match = match(Criteria.where("studentsOnTheCourse").exists(true));
        Aggregation aggregation = newAggregation(match);
        return mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Course.class), Course.class).getMappedResults();
    }
    
    private List<Course> filterCoursesByNumberOfStudents(List<Course> actualCourses) {
        return actualCourses.stream()
                .filter(x -> x.getStudentsOnTheCourse().size() >= NUMBERS_OF_STUDENTS)
                .collect(Collectors.toList());
    }

}
