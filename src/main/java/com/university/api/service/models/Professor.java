package com.university.api.service.models;

import org.springframework.data.annotation.Id;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Professor {
    @Transient
    public static final String SEQUENCE_NAME = "professors_sequence";

    @Id
    private Long id;
    @NotBlank(message = "Error, name cannot be empty!")
    private String name;
    @NotBlank(message = "Error, surname cannot be empty!")
    private String surname;

    public Professor(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Professor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(id, professor.id) && Objects.equals(name, professor.name) && Objects.equals(surname, professor.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }
}
