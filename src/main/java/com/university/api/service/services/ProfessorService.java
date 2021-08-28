package com.university.api.service.services;

import com.university.api.service.commands.ProfessorUpdateCommand;
import com.university.api.service.models.Professor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProfessorService {
    Professor addProfessor(Professor professor);

    Optional<Professor> findProfessorById(Long id);

    List<Professor> findAllProfessors();

    void deleteProfessorById(Long id);

    Professor updateProfessor(ProfessorUpdateCommand professorUpdateCommand);
}
