package com.university.api.service.services.implementations;

import com.university.api.service.commands.ProfessorUpdateCommand;
import com.university.api.service.exceptions.ProfessorNotFoundException;
import com.university.api.service.models.Professor;
import com.university.api.service.repositories.ProfessorRepo;
import com.university.api.service.services.ProfessorService;
import com.university.api.service.services.SequenceGeneratorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepo professorRepo;
    private final SequenceGeneratorService generatorService;

    public ProfessorServiceImpl(ProfessorRepo professorRepo, SequenceGeneratorService generatorService) {
        this.professorRepo = professorRepo;
        this.generatorService = generatorService;
    }

    @Override
    public Professor addProfessor(Professor professor) {
        professor.setId(generatorService.generateSequence(Professor.SEQUENCE_NAME));
        return professorRepo.save(professor);
    }

    @Override
    public Optional<Professor> findProfessorById(Long id) {
        return professorRepo.findById(id);
    }

    @Override
    public List<Professor> findAllProfessors() {
        return professorRepo.findAll();
    }

    @Override
    public void deleteProfessorById(Long id) {
        professorRepo.deleteById(id);
    }

    @Override
    public Professor updateProfessor(ProfessorUpdateCommand professorUpdateCommand) {
        Optional<Professor> professorFromDdb = professorRepo.findById(professorUpdateCommand.getId());
        if (!professorFromDdb.isPresent())
            throw new ProfessorNotFoundException("Error, student with id " + professorUpdateCommand.getId() + " not found!");
        if (professorUpdateCommand.getName() != null) {
            professorFromDdb.get().setName(professorUpdateCommand.getName());
        }
        if (professorUpdateCommand.getSurname() != null) {
            professorFromDdb.get().setSurname(professorUpdateCommand.getSurname());
        }
        return professorRepo.save(professorFromDdb.get());
    }
}
