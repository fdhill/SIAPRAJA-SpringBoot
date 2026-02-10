package com.example.siapraja.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data // Otomatis membuat Getter, Setter, toString, equals, dan hashCode
@NoArgsConstructor // Membuat constructor kosong (wajib untuk JPA)
@AllArgsConstructor // Membuat constructor dengan semua field
@Builder // Memungkinkan pembuatan objek gaya Student.builder().name("Andi").build()
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, length = 50)
    private String nisn;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String classroom;

    @Column(length = 1)
    private Character gender;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // Constructor custom jika kamu masih ingin menggunakannya secara manual
    // Tapi sebenarnya sudah tercover oleh @AllArgsConstructor atau @Builder
    public Student(String name, String nisn, String address, String classroom, Character gender, User user) {
        this.name = name;
        this.nisn = nisn;
        this.address = address;
        this.classroom = classroom;
        this.gender = gender;
        this.user = user;
    }
}