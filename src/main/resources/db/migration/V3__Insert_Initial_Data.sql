-- 1. ADMIN (ID: 1)
INSERT INTO users (name, username, password, role) VALUES 
('Admin', 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8q2OuVGkqEnLPzSGH94ohz78fshY5N79K6K', 1);

-- 2. SISWA (ID: 2 - 11)
INSERT INTO users (name, username, password, role) VALUES 
('Andi', 'andi', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Bella', 'bella', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Caca', 'caca', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Deni', 'deni', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Eka', 'eka', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Fani', 'fani', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Gani', 'gani', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Hani', 'hani', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Ivan', 'ivan', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2),
('Joni', 'joni', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 2);

INSERT INTO students (name, nisn, address, classroom, gender, user_id) VALUES 
('Andi', '001', 'Solo', 'XII-RPL-1', 'M', 2),
('Bella', '002', 'Solo', 'XII-RPL-1', 'F', 3),
('Caca', '003', 'Solo', 'XII-RPL-2', 'F', 4),
('Deni', '004', 'Solo', 'XII-RPL-2', 'M', 5),
('Eka', '005', 'Solo', 'XII-TKJ-1', 'F', 6),
('Fani', '006', 'Solo', 'XII-TKJ-1', 'F', 7),
('Gani', '007', 'Solo', 'XII-TKJ-2', 'M', 8),
('Hani', '008', 'Solo', 'XII-TKJ-2', 'F', 9),
('Ivan', '009', 'Solo', 'XII-MM-1', 'M', 10),
('Joni', '010', 'Solo', 'XII-MM-2', 'M', 11);

-- 3. GURU (ID: 12 - 16)
INSERT INTO users (name, username, password, role) VALUES 
('Budi Santoso', 'budi', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 4),
('Siti Aminah', 'siti', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 4),
('Iwan Fals', 'iwan', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 4),
('Rina Nose', 'rina', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 4),
('Agus Kotak', 'agus', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 4);

INSERT INTO teachers (name, nip, address, gender, user_id) VALUES 
('Budi Santoso', '19850101', 'Jl. Merdeka 1', 'M', 12),
('Siti Aminah', '19850102', 'Jl. Mawar 2', 'F', 13),
('Iwan Fals', '19850103', 'Jl. Melati 3', 'M', 14),
('Rina Nose', '19850104', 'Jl. Anggrek 4', 'F', 15),
('Agus Kotak', '19850105', 'Jl. Kamboja 5', 'M', 16);

-- 4. PERUSAHAAN (ID: 17 - 21)
INSERT INTO users (name, username, password, role) VALUES 
('PT Teknologi Maju', 'techmaju', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 3),
('CV Kreatif Digital', 'kreatif', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 3),
('PT Solusi Data', 'solusidata', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 3),
('PT Inovasi Siber', 'inovasi', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 3),
('Global Tech Corp', 'globaltech', '$2a$10$Y50UaMWM7bRauvLDj7qNbeQn7Yn6S.O2S0S2Xy8A9o0O0I0O0I0O0', 3);

INSERT INTO companies (name, address, phone, quota, user_id) VALUES 
('PT Teknologi Maju', 'Jakarta', '021-111', 5, 17),
('CV Kreatif Digital', 'Bandung', '022-222', 3, 18),
('PT Solusi Data', 'Surabaya', '031-333', 4, 19),
('PT Inovasi Siber', 'Yogyakarta', '0274-444', 2, 20),
('Global Tech Corp', 'Semarang', '024-555', 10, 21);