package com.example.siapraja.service;

import com.example.siapraja.model.User;
import com.example.siapraja.repository.UserRepository;
import com.example.siapraja.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan: " + username));

        // Tambahkan log ini untuk debug:
        System.out.println("User ditemukan: " + user.getUsername());
        System.out.println("Password di DB: " + user.getPassword());
        return new MyUserDetails(user);
    }
}