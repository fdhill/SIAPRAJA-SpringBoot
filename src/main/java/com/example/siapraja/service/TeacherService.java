package com.example.siapraja.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.TeacherRequestDTO;
import com.example.siapraja.dto.TeacherResponDTO;
import com.example.siapraja.dto.UserRequestDTO;
import com.example.siapraja.model.Teacher;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.TeacherRepository;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public TeacherResponDTO findById(Long id) {
        return teacherRepository.findById(id)
            .map(teacher -> modelMapper.map(teacher, TeacherResponDTO.class))
            .orElseThrow(() -> new RuntimeException("Teacher with id " + id + " not found!"));
    }

    @PreAuthorize("hasRole('TEACHER') and #id == authentication.principal.userId")
    @Transactional(readOnly = true)
    public TeacherResponDTO findByUserId(Long id) {
        return teacherRepository.findByUserId(id)
            .map(teacher -> modelMapper.map(teacher, TeacherResponDTO.class))
            .orElseThrow(() -> new RuntimeException("Teacher with user id " + id + " not found!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Iterable<TeacherResponDTO> findAll() {
        Iterable<Teacher> teachers = teacherRepository.findAll();

        return StreamSupport.stream(teachers.spliterator(), false)
        .map(teacher -> modelMapper.map(teacher, TeacherResponDTO.class))
        .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponDTO save(TeacherRequestDTO teacherRequestDTO) {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName(teacherRequestDTO.getName());
        newUser.setUsername(teacherRequestDTO.getNip());
        newUser.setPassword("123456");
        newUser.setRole(4);
        User savedUser = modelMapper.map(userService.save(newUser), User.class);

        Teacher teacher = modelMapper.map(teacherRequestDTO, Teacher.class);
        teacher.setUser(savedUser);

        return modelMapper.map(teacherRepository.save(teacher), TeacherResponDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #teacherId == authentication.principal.teacherId)")
    public TeacherResponDTO edit(Long teacherId, TeacherRequestDTO teacherRequestDTO) {
        Teacher existingTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existingTeacher.setName(teacherRequestDTO.getName());

        if(teacherRequestDTO.getNip() != null && !teacherRepository.existsByNip(teacherRequestDTO.getNip())){
            existingTeacher.setNip(teacherRequestDTO.getNip());
        }
        existingTeacher.setAddress(teacherRequestDTO.getAddress());
        existingTeacher.setGender(teacherRequestDTO.getGender());

        return modelMapper.map(teacherRepository.save(existingTeacher), TeacherResponDTO.class);
    }
}
