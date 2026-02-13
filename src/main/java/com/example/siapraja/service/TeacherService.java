package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.model.Teacher;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.TeacherRepository;

@Service
@Transactional
public class TeacherService {
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    @Transactional(readOnly = true)
    public Teacher findById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Teacher findByUserId(Long id) {
        return teacherRepository.findByUserId(id)
            .orElseThrow(() -> new RuntimeException("Stundet not found!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Iterable<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Teacher save(Teacher teacher) {
        User newUser = new User();
        newUser.setName(teacher.getName());
        newUser.setUsername(teacher.getNip());
        newUser.setPassword("123456");
        newUser.setRole(4);

        User savedUser = userService.save(newUser);

        teacher.setUser(savedUser);

        return teacherRepository.save(teacher);
    }

    public Iterable<Teacher> saveAll(Iterable<Teacher> teacher) {
        return teacherRepository.saveAll(teacher);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Teacher edit(Long id, Teacher teacherDetails) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existingTeacher.setName(teacherDetails.getName());
        existingTeacher.setNip(teacherDetails.getNip());
        existingTeacher.setAddress(teacherDetails.getAddress());
        existingTeacher.setGender(teacherDetails.getGender());

        User user = existingTeacher.getUser();
        user.setName(teacherDetails.getName());
        user.setUsername(teacherDetails.getNip());
        userService.edit(user.getId(), user);

        return existingTeacher;
    }
}
