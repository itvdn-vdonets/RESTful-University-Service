package com.university.api.service.services.implementations;

import com.university.api.service.commands.StudentUpdateCommand;
import com.university.api.service.exceptions.StudentNotFoundException;
import com.university.api.service.models.Student;
import com.university.api.service.repositories.StudentRepo;
import com.university.api.service.services.SequenceGeneratorService;
import com.university.api.service.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = "studentsCache")
public class StudentServiceImpl implements StudentService {
    private final StudentRepo studentRepo;
    private final CacheManager cacheManager;
    private final SequenceGeneratorService sequenceGenerator;

    public StudentServiceImpl(StudentRepo studentRepo, CacheManager cacheManager, SequenceGeneratorService sequenceGenerator) {
        this.studentRepo = studentRepo;
        this.cacheManager = cacheManager;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    @Cacheable(key = "#student.name")
    public Student createStudentOrReturnCached(Student student) {
        log.info("Adding new student... " + student);
        student.setId(sequenceGenerator.generateSequence(Student.SEQUENCE_NAME));
        return studentRepo.save(student);
    }


    @Override
    @CachePut(key = "#student.name")
    public Student createStudentAndRefreshCache(Student student) {
        log.info("Adding new student... " + student);
        student.setId(sequenceGenerator.generateSequence(Student.SEQUENCE_NAME));
        return studentRepo.save(student);
    }

    @Override
    @Cacheable
    public Optional<Student> findStudentById(Long id) {
        log.info("Finding student by id... " + id);
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
    @CacheEvict
    public void deleteStudentById(Long id) {
        if (!findStudentById(id).isPresent())
            throw new StudentNotFoundException("Error, student with id " + id + " not found!");
        studentRepo.deleteById(id);
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleCacheCleaning() {
        log.info("Scheduling cleaning job...");
        cleanCache();
    }

    public void cleanCache() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            cache.clear();
            log.info("Cleaning..." + cache);
        }
    }


}
