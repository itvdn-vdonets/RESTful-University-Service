package com.university.api.service.controllers;

import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.exceptions.StudentNotFoundException;
import com.university.api.service.models.Student;
import com.university.api.service.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/students")
public class StudentController {

    final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> findAllStudents() {
        return ResponseEntity.ok(studentService.findAllStudents());
    }

    @PostMapping
    public ResponseEntity<Student> createStudentOrReturnCached(@RequestBody @Valid Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudentOrReturnCached(student));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Student> createStudentAndRefreshCache(@RequestBody @Valid Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudentAndRefreshCache(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable long id) {
        Student studentById = studentService.findStudentById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: "+ id));
        return ResponseEntity.ok(studentById);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody @Valid StudentUpdateCommand studentUpdateCommand) {
        return ResponseEntity.ok(studentService.updateStudent(studentUpdateCommand));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable long id) {
        studentService.deleteStudentById(id);
    }

}
