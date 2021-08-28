package com.university.api.service.commands;

import javax.validation.constraints.NotBlank;

public class StudentUpdateCommand {
    private final Long id;
    @NotBlank(message = "Error, name cannot be empty!")
    private final String name;
    @NotBlank(message = "Error, surname cannot be empty!")
    private final String surname;

    public StudentUpdateCommand(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "StudentUpdateCommand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
