package com.example.workfast.Modelo;

public class Job {
    private String title;
    private String description;
    private String requirements;
    private String salary;
    private String location;

    // Constructor
    public Job(String title, String description, String requirements, String salary, String location) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.salary = salary;
        this.location = location;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getSalary() {
        return salary;
    }

    public String getLocation() {
        return location;
    }
}
