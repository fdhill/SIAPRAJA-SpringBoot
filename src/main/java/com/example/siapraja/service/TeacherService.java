package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(readOnly = true)
    public Teacher findById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher save(Teacher teacher) {
        User newUser = new User();
        newUser.setName(teacher.getName());
        newUser.setUsername(teacher.getNip());
        newUser.setPassword(teacher.getNip());
        newUser.setRole(3);

        User savedUser = userService.save(newUser);

        teacher.setUser(savedUser);

        return teacherRepository.save(teacher);
    }

    public Iterable<Teacher> saveAll(Iterable<Teacher> teacher) {
        return teacherRepository.saveAll(teacher);
    }

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
