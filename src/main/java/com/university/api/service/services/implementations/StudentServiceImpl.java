package com.university.api.service.services.implementations;

import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.exceptions.StudentNotFoundException;
import com.university.api.service.models.Student;
import com.university.api.service.repositories.StudentRepo;
import com.university.api.service.services.SequenceGeneratorService;
import com.university.api.service.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepo studentRepo;
    private final SequenceGeneratorService sequenceGenerator;

    public StudentServiceImpl(StudentRepo studentRepo, SequenceGeneratorService sequenceGenerator) {
        this.studentRepo = studentRepo;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public Student addStudent(Student student) {
        student.setId(sequenceGenerator.generateSequence(Student.SEQUENCE_NAME));
        return studentRepo.save(student);
    }

    @Override
    public Optional<Student> findStudentById(Long id) {
        return studentRepo.findById(id);
    }

    @Override
    public List<Student> findAllStudents() {
        return studentRepo.findAll();
    }

    @Override
    public Student updateStudent(StudentUpdateCommand studentUpdateCommand) {
        Optional<Student> studentFromDb = studentRepo.findById(studentUpdateCommand.getId());
        if (!studentFromDb.isPresent())
            throw new StudentNotFoundException("Error, student with id " + studentUpdateCommand.getId() + " not found!");

        if (studentUpdateCommand.getName() != null) {
            studentFromDb.get().setName(studentUpdateCommand.getName());
        }

        if (studentUpdateCommand.getSurname() != null) {
            studentFromDb.get().setSurname(studentUpdateCommand.getSurname());
        }
        return studentRepo.save(studentFromDb.get());
    }

    @Override
    public void deleteStudentById(Long id) {
        if (!findStudentById(id).isPresent()) throw new StudentNotFoundException("Error, student with id " + id + " not found!");
        studentRepo.deleteById(id);
    }


}
