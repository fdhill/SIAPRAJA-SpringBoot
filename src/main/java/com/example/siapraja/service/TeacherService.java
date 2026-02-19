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

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Teacher findById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @PreAuthorize("#id == authentication.principal.userId")
    @Transactional(readOnly = true)
    public Teacher findByUserId(Long id) {
        return teacherRepository.findByUserId(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found!"));
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

    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #teacherId == authentication.principal.teacherId)")
    public Teacher edit(Long teacherId, Teacher teacherDetails) {
        Teacher existingTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existingTeacher.setName(teacherDetails.getName());

        if(teacherDetails.getNip() != null && !teacherRepository.existsByNip(teacherDetails.getNip())){
            existingTeacher.setNip(teacherDetails.getNip());
        }
        existingTeacher.setAddress(teacherDetails.getAddress());
        existingTeacher.setGender(teacherDetails.getGender());

        return existingTeacher;
    }
}
