package com.university.api.service.repositories;

import com.university.api.service.models.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfessorRepo extends MongoRepository<Professor, Long> {
}
