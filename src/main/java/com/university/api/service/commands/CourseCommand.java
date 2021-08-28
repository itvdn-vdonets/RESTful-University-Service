package com.university.api.service.commands;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Document
public class CourseCommand {
    private final Long id;
    @NotBlank(message = "Error, course name cannot be empty!")
    private final String courseName;
    @FutureOrPresent(message = "Error, start date cannot be earlier than current time!")
    private final Instant startDate;
    @Future(message = "Error, end date cannot be earlier than current time!")
    private final Instant endDate;
    private final List<Long> studentsOnTheCourse;
    private final Long professor;

    public CourseCommand(Long id, String courseName, Instant startDate, Instant endDate, List<Long> studentsOnTheCourse, Long professor) {
        this.id = id;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentsOnTheCourse = studentsOnTheCourse;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public List<Long> getStudentsOnTheCourse() {
        return studentsOnTheCourse;
    }

    public Long getProfessorId() {
        return professor;
    }

    @Override
    public String toString() {
        return "CourseDao{" +
                "courseName='" + courseName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", studentsOnTheCourse=" + studentsOnTheCourse +
                ", professor=" + professor +
                '}';
    }
}


