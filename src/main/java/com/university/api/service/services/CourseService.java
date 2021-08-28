package com.university.api.service.services;

import com.university.api.service.commands.CourseCommand;
import com.university.api.service.models.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CourseService {
    Course addCourse(CourseCommand courseCommand);

    Optional<Course> findCourseById(Long id);

    Course updateCourse(CourseCommand courseCommand);

    void deleteCourseById(long id);

    Course unAssignStudentsOnTheCourse(Long courseId, List<Long> studentIds);

    Course assignStudentsOnTheCourse(Long courseId, List<Long> studentIds);

    List<Course> findAllCourses();
}
