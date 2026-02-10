package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.model.User;
import com.example.siapraja.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }


    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public Iterable<User> saveAll(Iterable<User> user) {
        return userRepository.saveAll(user);
    }

    @PreAuthorize("hasRole('ADMIN') or #userDetails.id == authentication.principal.userId")
    public User edit(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(userDetails.getName());

        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(existingUser.getUsername())) {

            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username '" + userDetails.getUsername() + "already used");
            }

            existingUser.setUsername(userDetails.getUsername());
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            if (!userDetails.getPassword().startsWith("$2a$")) {
                existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
        }

        return existingUser;
    }

}
