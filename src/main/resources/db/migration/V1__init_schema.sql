-- 1. Tabel User (Dibuat pertama karena menjadi referensi/parent)
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role INT NOT NULL
);

-- 2. Tabel Teacher
CREATE TABLE teacher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nip VARCHAR(50) UNIQUE,
    address TEXT,
    gender CHAR(1),
    user_id BIGINT UNIQUE, -- Relasi One-to-One biasanya ditandai dengan UNIQUE
    CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 3. Tabel Student
CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nisn VARCHAR(50) UNIQUE,
    address TEXT,
    classroom VARCHAR(50),
    gender CHAR(1),
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 4. Tabel Company
CREATE TABLE company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    quota INT NOT NULL,
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_company_user FOREIGN KEY (user_id) REFERENCES user(id)
);