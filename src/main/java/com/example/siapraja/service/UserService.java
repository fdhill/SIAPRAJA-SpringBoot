package com.example.siapraja.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.UserRequestDTO;
import com.example.siapraja.dto.UserResponDTO;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @Transactional(readOnly = true)
    public UserResponDTO findById(Long userId) {
        return userRepository.findById(userId)
            .map(user -> modelMapper.map(user, UserResponDTO.class))
            .orElseThrow(() -> new RuntimeException("user with id " + userId + " not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Iterable<UserResponDTO> findAll() {
        Iterable<User> users = userRepository.findAll();

        return StreamSupport.stream(users.spliterator(), false)
            .map(user -> modelMapper.map(user, UserResponDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponDTO save(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().startsWith("$2a$")) {
            userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Username '" + userRequestDTO.getUsername() + "already used");
        }

        User user = modelMapper.map(userRequestDTO, User.class);

        return modelMapper.map(userRepository.save(user), UserResponDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public UserResponDTO edit(Long userId, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        existingUser.setName(userRequestDTO.getName());

        if (userRequestDTO.getUsername() != null && !userRequestDTO.getUsername().equals(existingUser.getUsername())) {

            if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
                throw new RuntimeException("Username '" + userRequestDTO.getUsername() + "already used");
            }

            existingUser.setUsername(userRequestDTO.getUsername());
        }

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            if (!userRequestDTO.getPassword().startsWith("$2a$")) {
                existingUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            }
        }
        return modelMapper.map(existingUser, UserResponDTO.class);
    }

}
