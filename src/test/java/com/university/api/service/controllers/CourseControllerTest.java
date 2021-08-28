package com.university.api.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.api.service.commands.CourseCommand;
import com.university.api.service.exceptions.CourseNotFoundException;
import com.university.api.service.models.Course;
import com.university.api.service.models.Professor;
import com.university.api.service.models.Student;
import com.university.api.service.services.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CourseService courseService;

    @Captor
    private ArgumentCaptor<Course> courseCaptor;

    @Captor
    private ArgumentCaptor<CourseCommand> courseCommandCaptor;

    @Captor
    private ArgumentCaptor<List<Long>> listCommandCaptor;


    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Test
    public void post_createsNewProfessor_andReturnsObjWith201() throws Exception {
        //Given
        Student student = new Student("Jen", "Kwo");
        student.setId(1L);
        HashMap<Long, Student> students = new HashMap<>();
        students.put(student.getId(), student);
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        Professor professor = new Professor("Mike", "Wako");
        professor.setId(1L);
        Course course = new Course("Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), students, professor);
        course.setId(1L);
        CourseCommand courseCommand = new CourseCommand(1L, "Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), ids, 1L);

        //When
        when(courseService.addCourse(any(CourseCommand.class))).thenReturn(course);

        //Then
        this.mockMvc.perform(post("/api/courses").content(mapper.writeValueAsBytes(courseCommand)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseName").isString())
                .andExpect(jsonPath("$.startDate").isString())
                .andExpect(jsonPath("$.endDate").isString())
                .andExpect(jsonPath("$.studentsOnTheCourse").isMap())
                .andExpect(jsonPath("$.professor").isMap())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.courseName").value(course.getCourseName()))
                .andExpect(jsonPath("$.professor").value(course.getProfessor()));
        verify(courseService, times(1)).addCourse(courseCommandCaptor.capture());
    }

    @Test
    public void get_findCourseById_andReturnsObjWith200() throws Exception {
        //Given
        Student student = new Student("Jen", "Kwo");
        student.setId(1L);
        HashMap<Long, Student> students = new HashMap<>();
        students.put(student.getId(), student);
        Professor professor = new Professor("Mike", "Wako");
        professor.setId(1L);
        Course course = new Course("Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), students, professor);
        course.setId(1L);

        //When
        when(courseService.findCourseById(any(Long.class))).thenReturn(java.util.Optional.of(course));

        //Then
        this.mockMvc.perform(get("/api/courses/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseName").isString())
                .andExpect(jsonPath("$.startDate").isString())
                .andExpect(jsonPath("$.endDate").isString())
                .andExpect(jsonPath("$.studentsOnTheCourse").isMap())
                .andExpect(jsonPath("$.professor").isMap())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.courseName").value(course.getCourseName()))
                .andExpect(jsonPath("$.professor").value(course.getProfessor()));
        verify(courseService, times(1)).findCourseById(idCaptor.capture());
    }


    @Test
    public void get_findCourseById_andReturns404() throws Exception {
        //Given
        Student student = new Student("Jen", "Kwo");
        student.setId(1L);
        HashMap<Long, Student> students = new HashMap<>();
        students.put(student.getId(), student);
        Professor professor = new Professor("Mike", "Wako");
        professor.setId(1L);
        Course course = new Course("Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), students, professor);
        course.setId(1L);

        //When
        doThrow(new CourseNotFoundException("Error, course not found with id" + course.getId())).when(courseService).findCourseById(any(Long.class));

        //Then
        this.mockMvc.perform(get("/api/courses/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


    @Test
    public void put_updateCourse_andReturnsObjWith200() throws Exception {
        //Given
        Student student = new Student("Jen", "Kwo");
        student.setId(1L);
        HashMap<Long, Student> students = new HashMap<>();
        students.put(student.getId(), student);
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        Professor professor = new Professor("Mike", "Wako");
        professor.setId(1L);
        Course course = new Course("Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), students, professor);
        course.setId(1L);
        CourseCommand courseCommand = new CourseCommand(1L, "Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), ids, 1L);

        //When
        when(courseService.updateCourse(any(CourseCommand.class))).thenReturn(course);

        //Then
        this.mockMvc.perform(put("/api/courses").content(mapper.writeValueAsBytes(courseCommand)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.courseName").isString())
                .andExpect(jsonPath("$.startDate").isString())
                .andExpect(jsonPath("$.endDate").isString())
                .andExpect(jsonPath("$.studentsOnTheCourse").isMap())
                .andExpect(jsonPath("$.professor").isMap())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.courseName").value(course.getCourseName()))
                .andExpect(jsonPath("$.professor").value(course.getProfessor()));
        verify(courseService, times(1)).updateCourse(courseCommandCaptor.capture());
    }


    @Test
    public void delete_deleteCourseById_andReturns200() throws Exception {
        //Given
        Student student = new Student("Jen", "Kwo");
        student.setId(1L);
        HashMap<Long, Student> students = new HashMap<>();
        students.put(student.getId(), student);
        Professor professor = new Professor("Mike", "Wako");
        professor.setId(1L);
        Course course = new Course("Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), students, professor);
        course.setId(1L);

        //When
        doNothing().when(courseService).deleteCourseById(any(Long.class));

        //Then
        this.mockMvc.perform(delete("/api/courses/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_deleteCourseById_andReturns404() throws Exception {
        //Given
        Student student = new Student("Jen", "Kwo");
        student.setId(1L);
        HashMap<Long, Student> students = new HashMap<>();
        students.put(student.getId(), student);
        Professor professor = new Professor("Mike", "Wako");
        professor.setId(1L);
        Course course = new Course("Big Data", Instant.parse("2021-10-28T08:16:46.687Z"), Instant.parse("2021-11-28T08:16:46.687Z"), students, professor);
        course.setId(1L);

        //When
        doThrow(new CourseNotFoundException("Error, course not found with id" + course.getId())).when(courseService).deleteCourseById(any(Long.class));

        //Then
        this.mockMvc.perform(delete("/api/courses/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}
