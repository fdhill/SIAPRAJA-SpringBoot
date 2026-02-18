package com.example.siapraja.service;

import com.example.siapraja.model.User;
import com.example.siapraja.repository.CompanyRepository;
import com.example.siapraja.repository.StudentRepository;
import com.example.siapraja.repository.TeacherRepository;
import com.example.siapraja.repository.UserRepository;
import com.example.siapraja.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));

        Object profile = null;
        switch (user.getRole()) {
            case 2 -> profile = studentRepository.findByUserId(user.getId()).orElse(null);
            case 3 -> profile = companyRepository.findByUserId(user.getId()).orElse(null);
            case 4 -> profile = teacherRepository.findByUserId(user.getId()).orElse(null);
        }

        return new MyUserDetails(user, profile);
    }
}