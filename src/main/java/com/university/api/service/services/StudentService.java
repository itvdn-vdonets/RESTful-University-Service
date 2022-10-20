package com.university.api.service.services;

import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.models.Student;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {

    @Cacheable(value = "studentsCache", key = "#student.name")
    Student createStudentOrReturnCached(Student student);

    Student createStudentAndRefreshCache(Student student);

    Optional<Student> findStudentById(Long id);

    List<Student> findAllStudents();

    Student updateStudent(StudentUpdateCommand studentUpdateCommand);

    void deleteStudentById(Long id);
}
