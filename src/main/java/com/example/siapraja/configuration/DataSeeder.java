package com.example.siapraja.configuration;

import com.example.siapraja.model.*;
import com.example.siapraja.repository.UserRepository;
import com.example.siapraja.service.CompanyService;
import com.example.siapraja.service.StudentService;
import com.example.siapraja.service.TeacherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataSeeder {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(
            UserRepository userRepository, 
            PasswordEncoder encoder,
            StudentService studentService,
            TeacherService teacherService,
            CompanyService companyService) {
        return args -> {
            if (userRepository.count() > 0) {
                System.out.println("Database sudah terisi, melewati proses seeding.");
                return;
            }

            // --- TRIK SAKTI: Masuk sebagai sistem (Admin) ---
            // Ini agar method @PreAuthorize di Service tidak error
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("system", null, 
                AuthorityUtils.createAuthorityList("ROLE_ADMIN")) 
            );

            try {
                System.out.println("Memulai seeding data dengan otorisasi sistem...");

                // 1. Simpan Admin dulu
                userRepository.save(User.builder()
                        .name("Administrator")
                        .username("admin")
                        .password(encoder.encode("admin"))
                        .role(1).build());

                // 2. Simpan Siswa via Service
                studentService.save(new Student("Andi", "001", "Solo", "XII-RPL-1", 'M', null));
                studentService.save(new Student("Bella", "002", "Solo", "XII-RPL-1", 'F', null));

                // 3. Simpan Guru via Service
                teacherService.save(Teacher.builder()
                        .name("Budi Santoso").nip("19850101").address("Jl. Merdeka 1").gender('M').build());

                // 4. Simpan Company via Service
                companyService.save(Company.builder()
                        .name("PT Teknologi Maju").address("Jakarta").phone("021-111").quota(5).build());

                System.out.println("Seeding selesai!");

            } finally {
                // Hapus otorisasi setelah selesai agar kembali bersih
                SecurityContextHolder.clearContext();
            }
        };
    }
}