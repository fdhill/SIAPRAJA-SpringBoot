package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.model.Student;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Stundet not found!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Student save(Student student) {
        User newUser = new User();
        newUser.setName(student.getName());
        newUser.setUsername(student.getNisn());
        newUser.setPassword("123456");
        newUser.setRole(2);

        User savedUser = userService.save(newUser);

        student.setUser(savedUser);

        return studentRepository.save(student);
    }

    public Iterable<Student> saveAll(Iterable<Student> student) {
        return studentRepository.saveAll(student);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    public Student edit(Long studentId, Student studentDetails) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existingStudent.setName(studentDetails.getName());

        if (studentDetails.getNisn()!= null && !studentRepository.existsByNisn(studentDetails.getNisn())) {
            existingStudent.setNisn(studentDetails.getNisn());
        }

        existingStudent.setAddress(studentDetails.getAddress());
        existingStudent.setClassroom(studentDetails.getClassroom());
        existingStudent.setGender(studentDetails.getGender());

        return existingStudent;
    }
}
