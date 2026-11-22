-- Create the Database
CREATE DATABASE IF NOT EXISTS CommercialAppDB
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE CommercialAppDB;

-- 1. Roles Table
CREATE TABLE roles (
                       role_id INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Users Table (Admin, Manager, Client)
CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       role_id INT NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       is_active BOOLEAN DEFAULT TRUE,
                       last_login TIMESTAMP NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT
);

-- 3. Products Table (The Core of your Vendor System)
CREATE TABLE products (
                          product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- This links the product to the Manager (Vendor) who owns it
                          manager_id BIGINT NOT NULL,
                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          price DECIMAL(10, 2) NOT NULL,
                          stock_quantity INT DEFAULT 0,
                          is_active BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (manager_id) REFERENCES users(user_id) ON DELETE CASCADE
);


USE CommercialAppDB;

INSERT INTO roles (role_name, description) VALUES
                                               ('ADMIN', 'Owner of the system, full access'),
                                               ('MANAGER', 'Vendor, can manage their own products'),
                                               ('CLIENT', 'Customer, can browse and buy products');