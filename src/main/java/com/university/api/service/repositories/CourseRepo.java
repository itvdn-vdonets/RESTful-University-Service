package com.university.api.service.repositories;

import com.university.api.service.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepo extends MongoRepository<Course, Long> {
}
