-- Seat Allocation Management System - Database Initialization Script

CREATE DATABASE IF NOT EXISTS seat_allocation_db;
USE seat_allocation_db;

DROP TABLE IF EXISTS seat;

CREATE TABLE seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    row_name VARCHAR(2) NOT NULL,
    seat_number INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'FREE',
    UNIQUE KEY uk_row_seat (row_name, seat_number)
);

INSERT INTO seat (row_name, seat_number, status) VALUES
('A', 1, 'FREE'), ('A', 2, 'FREE'), ('A', 3, 'FREE'), ('A', 4, 'FREE'), ('A', 5, 'FREE'),
('A', 6, 'FREE'), ('A', 7, 'FREE'), ('A', 8, 'FREE'), ('A', 9, 'FREE'), ('A', 10, 'FREE'),
('B', 1, 'FREE'), ('B', 2, 'FREE'), ('B', 3, 'FREE'), ('B', 4, 'FREE'), ('B', 5, 'FREE'),
('B', 6, 'FREE'), ('B', 7, 'FREE'), ('B', 8, 'FREE'), ('B', 9, 'FREE'), ('B', 10, 'FREE'),
('C', 1, 'FREE'), ('C', 2, 'FREE'), ('C', 3, 'FREE'), ('C', 4, 'FREE'), ('C', 5, 'FREE'),
('C', 6, 'FREE'), ('C', 7, 'FREE'), ('C', 8, 'FREE'), ('C', 9, 'FREE'), ('C', 10, 'FREE'),
('D', 1, 'FREE'), ('D', 2, 'FREE'), ('D', 3, 'FREE'), ('D', 4, 'FREE'), ('D', 5, 'FREE'),
('D', 6, 'FREE'), ('D', 7, 'FREE'), ('D', 8, 'FREE'), ('D', 9, 'FREE'), ('D', 10, 'FREE'),
('E', 1, 'FREE'), ('E', 2, 'FREE'), ('E', 3, 'FREE'), ('E', 4, 'FREE'), ('E', 5, 'FREE'),
('E', 6, 'FREE'), ('E', 7, 'FREE'), ('E', 8, 'FREE'), ('E', 9, 'FREE'), ('E', 10, 'FREE'),
('F', 1, 'FREE'), ('F', 2, 'FREE'), ('F', 3, 'FREE'), ('F', 4, 'FREE'), ('F', 5, 'FREE'),
('F', 6, 'FREE'), ('F', 7, 'FREE'), ('F', 8, 'FREE'), ('F', 9, 'FREE'), ('F', 10, 'FREE'),
('G', 1, 'FREE'), ('G', 2, 'FREE'), ('G', 3, 'FREE'), ('G', 4, 'FREE'), ('G', 5, 'FREE'),
('G', 6, 'FREE'), ('G', 7, 'FREE'), ('G', 8, 'FREE'), ('G', 9, 'FREE'), ('G', 10, 'FREE'),
('H', 1, 'FREE'), ('H', 2, 'FREE'), ('H', 3, 'FREE'), ('H', 4, 'FREE'), ('H', 5, 'FREE'),
('H', 6, 'FREE'), ('H', 7, 'FREE'), ('H', 8, 'FREE'), ('H', 9, 'FREE'), ('H', 10, 'FREE');
