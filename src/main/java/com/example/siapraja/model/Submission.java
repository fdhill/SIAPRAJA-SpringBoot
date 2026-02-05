package com.example.siapraja.model;

public class Submission {
    private Long id;
    private Student student;
    private Company company;

    public Submission(Student student, Company company) {
        this.student = student;
        this.company = company;
    }

    public Submission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
