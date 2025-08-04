INSERT INTO user (id, email, first_name, last_name, password, roles) VALUES
(1, 'rin.itoshi@frombluelock.com', 'Rin', 'Itoshi', '{bcrypt}$2a$10$peQhS46onmwQE2XQjzjm0Mu1quEtJQEmB2guLskxRdp.Kf1Y2a6X6', 'USER'),
(2, 'hyoma.chigiri@frombluelock.com', 'Hyoma', 'Chigiri', '{bcrypt}$2a$10$peQhS46onmwQE2XQjzjm0Mu1quEtJQEmB2guLskxRdp.Kf1Y2a6X6', 'USER'),
(3, 'meguru.bachira@frombluelock.com', 'Meguru', 'Bachira', '{bcrypt}$2a$10$peQhS46onmwQE2XQjzjm0Mu1quEtJQEmB2guLskxRdp.Kf1Y2a6X6', 'USER'),
(4, 'yoichi.isagi@frombluelock.com', 'Yoichi', 'Isagi', '{bcrypt}$2a$10$peQhS46onmwQE2XQjzjm0Mu1quEtJQEmB2guLskxRdp.Kf1Y2a6X6', 'USER'),
(5, 'nagi.seishiro@frombluelock.com', 'Nagi', 'Seishiro', '{bcrypt}$2a$10$wcKUyMD9urywaSN5gRBfx.0H49BXHpr4fnPS3PYIdiHtZL3GSe7g6', 'ADMIN');

INSERT INTO profile (id, description, name) VALUES
(1, 'Regular User', 'User'),
(2, 'Administrator User', 'Admin');

INSERT INTO user_profile (id, profile_id, user_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 2, 5);