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

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username '" + user.getUsername() + "already used");
        }

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public User edit(Long userId, User userDetails) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

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
