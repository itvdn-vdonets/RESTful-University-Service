package com.university.api.service.services.implementations;

import com.university.api.service.commands.CourseCommand;
import com.university.api.service.exceptions.CourseNotFoundException;
import com.university.api.service.exceptions.ProfessorNotFoundException;
import com.university.api.service.exceptions.StudentAlreadyAssignedException;
import com.university.api.service.exceptions.StudentNotFoundException;
import com.university.api.service.models.Course;
import com.university.api.service.models.Professor;
import com.university.api.service.models.Student;
import com.university.api.service.repositories.CourseRepo;
import com.university.api.service.services.CourseService;
import com.university.api.service.services.ProfessorService;
import com.university.api.service.services.SequenceGeneratorService;
import com.university.api.service.services.StudentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepo courseRepo;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final SequenceGeneratorService sequenceGenerator;

    public CourseServiceImpl(CourseRepo courseRepo, ProfessorService professorService, StudentService studentService, SequenceGeneratorService sequenceGenerator) {
        this.courseRepo = courseRepo;
        this.professorService = professorService;
        this.studentService = studentService;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public Course addCourse(CourseCommand courseCommand) {
        Course course = new Course();
        course.setId(sequenceGenerator.generateSequence(Course.SEQUENCE_NAME));
        course.setCourseName(courseCommand.getCourseName());
        course.setStartDate(courseCommand.getStartDate());
        course.setEndDate(courseCommand.getEndDate());
        course.setProfessor(fetchProfessorToTheCourseById(courseCommand.getProfessorId()));
        course.setStudentsOnTheCourse(fetchStudentsToTheCourseByIds(courseCommand.getStudentsOnTheCourse()));
        return courseRepo.save(course);
    }

    @Override
    public Course unAssignStudentsOnTheCourse(Long courseId, List<Long> studentIds) {
        Course courseById =  courseRepo.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Error, course not found with id "+courseId));
        for (Long id: studentIds) {
            if (!courseById.getStudentsOnTheCourse().containsKey(id)) throw new StudentNotFoundException("Error, students not found with ids "+id);
            courseById.getStudentsOnTheCourse().remove(id);
        }
        return courseRepo.save(courseById);
    }

    @Override
    public Course assignStudentsOnTheCourse(Long courseId, List<Long> studentIds) {
        Course courseById =  courseRepo.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Error, course not found with id "+courseId));
        HashMap<Long, Student> fetchStudentsToTheCourseByIds = fetchStudentsToTheCourseByIds(studentIds);
        for (Long id: studentIds) {
            if (courseById.getStudentsOnTheCourse().containsKey(id)) throw new StudentAlreadyAssignedException("Error, students have already assigned to the course with ids: "+id);
            courseById.getStudentsOnTheCourse().putAll(fetchStudentsToTheCourseByIds);
        }
        return courseRepo.save(courseById);
    }

    @Override
    public List<Course> findAllCourses() {
        return courseRepo.findAll();
    }

    @Override
    public Optional<Course> findCourseById(Long id) {
        return courseRepo.findById(id);
    }

    @Override
    public Course updateCourse(CourseCommand courseCommand) {
        System.out.println("UpdateCourse service () --->"+courseCommand);
        Optional<Course> courseFromDb = courseRepo.findById(courseCommand.getId());
        if (!courseFromDb.isPresent())
            throw new CourseNotFoundException("Error, student with id " + courseCommand.getId() + " not found!");
        if (courseCommand.getCourseName() != null) {
            courseFromDb.get().setCourseName(courseCommand.getCourseName());
        }
        if (courseCommand.getStartDate() != null) {
            courseFromDb.get().setStartDate(courseCommand.getStartDate());
        }
        if (courseCommand.getEndDate() != null) {
            courseFromDb.get().setEndDate(courseCommand.getEndDate());
        }
        if (courseCommand.getStudentsOnTheCourse() != null) {
            courseFromDb.get().setStudentsOnTheCourse(fetchStudentsToTheCourseByIds(courseCommand.getStudentsOnTheCourse()));
        }
        if (courseCommand.getProfessorId() != null) {
            courseFromDb.get().setProfessor(fetchProfessorToTheCourseById(courseCommand.getProfessorId()));
        }
        return courseRepo.save(courseFromDb.get());
    }

    @Override
    public void deleteCourseById(long id) {
        courseRepo.deleteById(id);
    }

    private HashMap<Long, Student> fetchStudentsToTheCourseByIds(List<Long> ids) {
        HashMap<Long, Student> studentHashMap = new HashMap<>();
        for (Long id : ids) {
            Student studentById = studentService.findStudentById(id).orElseThrow(() ->
                    new StudentNotFoundException("Error, student with id " + id + " not found!"));
            studentHashMap.putIfAbsent(studentById.getId(), studentById);
        }
        return studentHashMap;
    }

    private Professor fetchProfessorToTheCourseById(Long id) {
        return professorService.findProfessorById(id).orElseThrow(() -> new ProfessorNotFoundException("Error, professor with id " + id + " not found!"));
    }
}
