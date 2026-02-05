package com.example.siapraja.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, length = 50)
    private String nip;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 1)
    private Character gender;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
