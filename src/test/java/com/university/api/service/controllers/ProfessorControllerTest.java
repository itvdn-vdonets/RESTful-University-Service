package com.university.api.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.api.service.commands.ProfessorUpdateCommand;
import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.exceptions.ProfessorNotFoundException;
import com.university.api.service.exceptions.StudentNotFoundException;
import com.university.api.service.models.Professor;
import com.university.api.service.models.Student;
import com.university.api.service.services.ProfessorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ProfessorService professorService;

    @Captor
    private ArgumentCaptor<Professor> professorCaptor;

    @Captor
    private ArgumentCaptor<ProfessorUpdateCommand> professorUpdateCommandCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;


    @Test
    public void post_createsNewProfessor_andReturnsObjWith201() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);

        //When
        when(professorService.addProfessor(any(Professor.class))).thenReturn(professor);

        //Then
        this.mockMvc.perform(post("/api/professors").content(mapper.writeValueAsBytes(professor)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.surname").isString())
                .andExpect(jsonPath("$.id").value(professor.getId()))
                .andExpect(jsonPath("$.name").value(professor.getName()))
                .andExpect(jsonPath("$.surname").value(professor.getSurname()));
        verify(professorService, times(1)).addProfessor(professorCaptor.capture());
    }


    @Test
    public void post_createsNewProfessor_andReturns500() throws Exception {
        //Given
        Professor professor = new Professor();

        //When
        when(professorService.addProfessor(any(Professor.class))).thenReturn(professor);

        //Then
        this.mockMvc.perform(post("/api/professors").content(mapper.writeValueAsBytes(professor)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void get_findProfessorById_andReturnsObjWith200() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);

        //When
        when(professorService.findProfessorById(any(Long.class))).thenReturn(java.util.Optional.of(professor));

        //Then
        this.mockMvc.perform(get("/api/professors/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.surname").isString())
                .andExpect(jsonPath("$.id").value(professor.getId()))
                .andExpect(jsonPath("$.name").value(professor.getName()))
                .andExpect(jsonPath("$.surname").value(professor.getSurname()));
        verify(professorService, times(1)).findProfessorById(idCaptor.capture());
    }

    @Test
    public void get_findProfessorById_andReturns404() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);

        //When
        when(professorService.findProfessorById(any(Long.class))).thenThrow(ProfessorNotFoundException.class);

        //Then
        this.mockMvc.perform(get("/api/professors/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void put_updateProfessor_andReturns201() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);
        StudentUpdateCommand professorToUpdate = new StudentUpdateCommand(professor.getId(), "Pablo", "Picasso");

        //When
        when(professorService.updateProfessor(any(ProfessorUpdateCommand.class))).thenReturn(professor);

        //Then
        this.mockMvc.perform(put("/api/professors").content(mapper.writeValueAsBytes(professorToUpdate)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(professor.getId()))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value(professor.getName()))
                .andExpect(jsonPath("$.surname").isString())
                .andExpect(jsonPath("$.surname").value(professor.getSurname()));
        verify(professorService, times(1)).updateProfessor(professorUpdateCommandCaptor.capture());
    }


    @Test
    public void put_updateProfessor_andReturns404() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);
        StudentUpdateCommand professorToUpdate = new StudentUpdateCommand(professor.getId(), "Pablo", "Picasso");

        //When
        when(professorService.updateProfessor(any(ProfessorUpdateCommand.class)))
                .thenThrow(new ProfessorNotFoundException("Error, professor with id "+professorToUpdate.getId()+ " not found!"));

        //Then
        this.mockMvc.perform(put("/api/professors").content(mapper.writeValueAsBytes(professorToUpdate)).contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_deleteProfessorById_andReturns200() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);

        //When
        doNothing().when(professorService).deleteProfessorById(any(Long.class));

        //Then
        this.mockMvc.perform(delete("/api/professors/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_deleteProfessorById_andReturns404() throws Exception {
        //Given
        Professor professor = new Professor("Andy", "Warhol");
        professor.setId(1L);

        //When
        doThrow(new ProfessorNotFoundException("Error, professor with id"+ professor.getId()+" not found!"))
                .when(professorService).deleteProfessorById(any(Long.class));

        //Then
        this.mockMvc.perform(delete("/api/students/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
