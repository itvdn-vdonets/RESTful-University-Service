package com.university.api.service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Document
public class Course {
    @Transient
    public static final String SEQUENCE_NAME = "courses_sequence";

    @Id
    private Long id;
    @NotBlank(message = "Error, course name cannot be empty!")
    private String courseName;
    @FutureOrPresent(message = "Error, start date cannot be earlier than current time!")
    private Instant startDate;
    @Future(message = "Error, end date cannot be earlier than current time!")
    private Instant endDate;
    private HashMap<Long, Student> studentsOnTheCourse;
    private Professor professor;

    public Course(String courseName, Instant startDate, Instant endDate, HashMap<Long, Student> studentsOnTheCourse, Professor professor) {
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentsOnTheCourse = studentsOnTheCourse;
        this.professor = professor;
    }

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Map<Long, Student> getStudentsOnTheCourse() {
        return studentsOnTheCourse;
    }

    public void setStudentsOnTheCourse(HashMap<Long, Student> studentsOnTheCourse) {
        this.studentsOnTheCourse = studentsOnTheCourse;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

}
