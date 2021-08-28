package com.university.api.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.exceptions.StudentNotFoundException;
import com.university.api.service.models.Student;
import com.university.api.service.services.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StudentService studentService;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;
    @Captor
    private ArgumentCaptor<StudentUpdateCommand> studentUpdateCommandCaptor;
    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Test
    public void post_createsNewStudent_andReturnsObjWith201() throws Exception {
        //Given
        Student student = new Student("Robert", "Carlos");
        student.setId(1L);

        //When
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        //Then
        this.mockMvc.perform(post("/api/students").content(mapper.writeValueAsBytes(student)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.surname").isString())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.surname").value(student.getSurname()));
        verify(studentService, times(1)).addStudent(studentCaptor.capture());
    }

    @Test
    public void post_createsNewStudent_andReturns500() throws Exception {
        //Given
        Student student = new Student();

        //When
        when(studentService.addStudent(any(Student.class))).thenReturn(null);

        //Then
        this.mockMvc.perform(post("/api/students").content(mapper.writeValueAsBytes(student)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void get_findStudentById_andReturnsObjWith200() throws Exception {
        //Given
        Student student = new Student("Robert", "Carlos");
        student.setId(1L);

        //When
        when(studentService.findStudentById(any(Long.class))).thenReturn(java.util.Optional.of(student));

        //Then
        this.mockMvc.perform(get("/api/students/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.surname").isString())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.surname").value(student.getSurname()));
        verify(studentService, times(1)).findStudentById(idCaptor.capture());
    }

    @Test
    public void get_findStudentById_andReturns404() throws Exception {
        //Given
        Student student = new Student("Robert", "Carlos");
        student.setId(1L);

        //When
        when(studentService.findStudentById(any(Long.class))).thenThrow(StudentNotFoundException.class);

        //Then
        this.mockMvc.perform(get("/api/students/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void put_updateStudent_andReturns201() throws Exception {
        //Given
        Student student = new Student("Mike", "Wako");
        student.setId(1L);
        StudentUpdateCommand studentToUpdate = new StudentUpdateCommand(student.getId(), "Mike", "Wako");

        //When
        when(studentService.updateStudent(any(StudentUpdateCommand.class))).thenReturn(student);

        //Then
        this.mockMvc.perform(put("/api/students").content(mapper.writeValueAsBytes(studentToUpdate)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.surname").isString())
                .andExpect(jsonPath("$.surname").value(student.getSurname()));
        verify(studentService, times(1)).updateStudent(studentUpdateCommandCaptor.capture());
    }


    @Test
    public void put_updateStudent_andReturns404() throws Exception {
        //Given
        Student student = new Student("Mike", "Wako");
        student.setId(1L);
        StudentUpdateCommand studentToUpdate = new StudentUpdateCommand(3L, "Mike", "Wako");

        //When
        when(studentService.updateStudent(any(StudentUpdateCommand.class))).thenThrow(new StudentNotFoundException("Error, student with id "+studentToUpdate.getId()+ " not found!"));

        //Then
        this.mockMvc.perform(put("/api/students").content(mapper.writeValueAsBytes(studentToUpdate)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_deleteStudentById_andReturns200() throws Exception {
        //Given
        Student student = new Student("Mike", "Wako");
        student.setId(1L);

        //When
        doNothing().when(studentService).deleteStudentById(any(Long.class));

        //Then
        this.mockMvc.perform(delete("/api/students/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_deleteStudentById_andReturns404() throws Exception {
        //Given
        Student student = new Student("Mike", "Wako");
        student.setId(1L);

        //When
        doThrow(new StudentNotFoundException("Error, student with id"+ student.getId()+" not found!"))
                .when(studentService).deleteStudentById(any(Long.class));

        //Then
        this.mockMvc.perform(delete("/api/students/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


}
