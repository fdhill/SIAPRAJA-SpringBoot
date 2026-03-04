package com.example.siapraja.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.StudentRequestDTO;
import com.example.siapraja.dto.StudentRespontDTO;
import com.example.siapraja.dto.UserRequestDTO;
import com.example.siapraja.model.Student;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    @Transactional(readOnly = true)
    public StudentRespontDTO findById(Long studentId) {
        return studentRepository.findById(studentId)
            .map(studnet -> modelMapper.map(studnet, StudentRespontDTO.class))
            .orElseThrow(() -> new RuntimeException("Studnet with id " + studentId + " nout found"));
    }
    
    @PreAuthorize("hasRole('STUDENT') and #userId == authentication.principal.userId")
    @Transactional(readOnly = true)
    public StudentRespontDTO findByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
            .map(student -> modelMapper.map(student, StudentRespontDTO.class))
            .orElseThrow(() -> new RuntimeException("Studnet with user id " + userId + " nout found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Iterable<StudentRespontDTO> findAll() {
        Iterable<Student> students = studentRepository.findAll();


        return StreamSupport.stream(students.spliterator(), false)
            .map(student -> modelMapper.map(student, StudentRespontDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public StudentRespontDTO save(StudentRequestDTO studentRequestDTO) {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName(studentRequestDTO.getName());
        newUser.setUsername(studentRequestDTO.getNisn());
        newUser.setPassword("123456");
        newUser.setRole(2);
        User savedUser = modelMapper.map(userService.save(newUser), User.class);

        Student student = modelMapper.map(studentRequestDTO, Student.class);
        student.setUser(savedUser);

        return modelMapper.map(studentRepository.save(student), StudentRespontDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    public StudentRespontDTO edit(Long studentId, StudentRequestDTO studentRequestDTO) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existingStudent.setName(studentRequestDTO.getName());

        if (studentRequestDTO.getNisn()!= null && !studentRepository.existsByNisn(studentRequestDTO.getNisn())) {
            existingStudent.setNisn(studentRequestDTO.getNisn());
        }

        existingStudent.setAddress(studentRequestDTO.getAddress());
        existingStudent.setClassroom(studentRequestDTO.getClassroom());
        existingStudent.setGender(studentRequestDTO.getGender());

        return modelMapper.map(studentRepository.save(existingStudent), StudentRespontDTO.class);
    }
}
