-- 5. Tabel Monitoring
CREATE TABLE monitorings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT UNIQUE,
    teacher_id BIGINT,
    company_id BIGINT,
    start_date DATE,
    end_date DATE,
    CONSTRAINT fk_monitoring_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_monitoring_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    CONSTRAINT fk_monitoring_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- 6. Tabel Presence
CREATE TABLE presences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    monitoring_id BIGINT,
    location TEXT NOT NULL,
    notes TEXT,
    status INT NOT NULL,
    date DATE NOT NULL,
    checkin_time TIME NOT NULL,
    checkout_time TIME,
    CONSTRAINT fk_presence_monitoring FOREIGN KEY (monitoring_id) REFERENCES monitorings(id)
);

-- 7. Tabel Submission
CREATE TABLE submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT,
    company_id BIGINT,
    status INT NOT NULL,
    CONSTRAINT fk_submission_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_submission_company FOREIGN KEY (company_id) REFERENCES companies(id)
);