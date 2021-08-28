package com.university.api.service.controllers;

import com.university.api.service.commands.CourseCommand;
import com.university.api.service.exceptions.CourseNotFoundException;
import com.university.api.service.models.Course;
import com.university.api.service.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> findAllCourses() {
        return ResponseEntity.ok(courseService.findAllCourses());
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody @Valid CourseCommand courseCommand) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addCourse(courseCommand));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course courseById = courseService.findCourseById(id).orElseThrow( () -> new CourseNotFoundException("Course not found with id: "+id));
        return ResponseEntity.ok(courseById);
    }

    @DeleteMapping("/{id}")
    public void deleteCourseById(@PathVariable Long id) {
        courseService.deleteCourseById(id);
    }

    @PutMapping
    public ResponseEntity<Course> updateCourse(@RequestBody @Valid CourseCommand courseCommand) {
        return ResponseEntity.ok(courseService.updateCourse(courseCommand));
    }

    @PostMapping("/assign")
    public ResponseEntity<Course> assignStudentsOnTheCourse(@RequestBody CourseCommand courseCommand) {
        return ResponseEntity.ok(courseService.assignStudentsOnTheCourse(courseCommand.getId(), courseCommand.getStudentsOnTheCourse()));
    }

    @PostMapping("/unassign")
    public ResponseEntity<Course> unAssignStudentsOnTheCourse(@RequestBody CourseCommand courseCommand) {
        return ResponseEntity.ok(courseService.unAssignStudentsOnTheCourse(courseCommand.getId(), courseCommand.getStudentsOnTheCourse()));
    }

}
