package com.university.api.service.services;

import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.models.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {
    Student addStudent(Student student);

    Optional<Student> findStudentById(Long id);

    List<Student> findAllStudents();

    Student updateStudent(StudentUpdateCommand studentUpdateCommand);

    void deleteStudentById(Long id);
}
