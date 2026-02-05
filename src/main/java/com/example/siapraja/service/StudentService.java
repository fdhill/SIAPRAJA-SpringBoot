package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }


    @Transactional(readOnly = true)
    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student save(Student student) {
        User newUser = new User();
        newUser.setName(student.getName());
        newUser.setUsername(student.getNisn());
        newUser.setPassword(student.getNisn());
        newUser.setRole(3);

        User savedUser = userService.save(newUser);

        student.setUser(savedUser);

        return studentRepository.save(student);
    }

    public Iterable<Student> saveAll(Iterable<Student> student) {
        return studentRepository.saveAll(student);
    }

    public Student edit(Long id, Student studentDetails) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existingStudent.setName(studentDetails.getName());
        existingStudent.setNisn(studentDetails.getNisn());
        existingStudent.setAddress(studentDetails.getAddress());
        existingStudent.setGender(studentDetails.getGender());

        User user = existingStudent.getUser();
        user.setName(studentDetails.getName());
        user.setUsername(studentDetails.getNisn());
        userService.edit(user.getId(), user);

        return existingStudent;
    }
}
