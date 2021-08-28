package com.university.api.service.controllers;

import com.university.api.service.commands.ProfessorUpdateCommand;
import com.university.api.service.exceptions.ProfessorNotFoundException;
import com.university.api.service.models.Professor;
import com.university.api.service.services.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public ResponseEntity<List<Professor>> findAllProfessors() {
        return ResponseEntity.ok(professorService.findAllProfessors());
    }

    @PostMapping
    public ResponseEntity<Professor> addProfessor(@RequestBody @Valid Professor professor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.addProfessor(professor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> findProfessorById(@PathVariable Long id) {
        Optional<Professor> professorById = professorService.findProfessorById(id);
        return professorById.map(professor -> ResponseEntity.ok().body(professor)).orElseThrow(() -> new ProfessorNotFoundException("Error, professor with id "+id+" not found!"));
    }

    @DeleteMapping("/{id}")
    public void deleteProfessorById(@PathVariable Long id) {
        professorService.deleteProfessorById(id);
    }

    @PutMapping
    public ResponseEntity<Professor> updateProfessor(@RequestBody @Valid ProfessorUpdateCommand professorUpdateCommand) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.updateProfessor(professorUpdateCommand));
    }
}
