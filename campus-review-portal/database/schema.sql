-- Campus Review Portal Database Schema
-- Simple MySQL Database Setup for Mini-Project

-- Create database
CREATE DATABASE IF NOT EXISTS campus_review_portal;
USE campus_review_portal;

-- Users table (for students and faculty)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admins table (separate login system for authorities)
CREATE TABLE admins (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Campus facilities table
CREATE TABLE facilities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL
);

-- Reviews table
CREATE TABLE reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    facility_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (facility_id) REFERENCES facilities(id)
);

-- Complaints table
CREATE TABLE complaints (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    facility_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    admin_response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (facility_id) REFERENCES facilities(id)
);

-- Insert default admin
INSERT INTO admins (username, password, full_name, email) VALUES
('admin', 'admin123', 'System Admin', 'admin@campus.edu');

-- Insert sample facilities
INSERT INTO facilities (name, category) VALUES
('Main Library', 'LIBRARY'),
('Sports Turf', 'TURF'),
('Boys Washroom Block A', 'WASHROOM'),
('Girls Washroom Block A', 'WASHROOM'),
('Canteen', 'CANTEEN'),
('Computer Lab', 'LAB'),
('Auditorium', 'AUDITORIUM'),
('Parking Area', 'PARKING'),
('Gym', 'GYM');

-- Insert sample user
INSERT INTO users (username, password, full_name, email) VALUES
('user1', 'user123', 'John Doe', 'john@campus.edu');

-- Insert sample review
INSERT INTO reviews (user_id, facility_id, title, content, rating, status) VALUES
(1, 1, 'Good Library', 'Library is well maintained and quiet.', 4, 'APPROVED');

-- Insert sample complaint
INSERT INTO complaints (user_id, facility_id, title, description, status) VALUES
(1, 3, 'Broken Tap', 'Water tap in washroom is not working.', 'PENDING');