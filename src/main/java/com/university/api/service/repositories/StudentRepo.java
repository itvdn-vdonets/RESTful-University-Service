package com.university.api.service.repositories;

import com.university.api.service.models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepo extends MongoRepository<Student, Long> {

}
