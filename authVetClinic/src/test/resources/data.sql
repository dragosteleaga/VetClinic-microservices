VALUES ('admin@admin.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'User', 'Dragos', 'Teleaga', 'ADMIN');
INSERT INTO users (email, password, dtype, first_name, last_name, role)
VALUES ('admin2@admin.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'User', 'Dragos2', 'Teleaga2', 'ADMIN');
INSERT INTO users (email, password, dtype, first_name, last_name, role, specialization)
VALUES ('vet@clinic.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'VETERINARIAN', 'Vlad', 'Vetescu', 'VETERINARIAN', 'Dermatology');
INSERT INTO users (email, password, dtype, first_name, last_name, role, specialization)
VALUES ('vet2@clinic.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'VETERINARIAN', 'Andrei', 'Sorcaru', 'VETERINARIAN', 'Dermatology');

INSERT INTO users (email, password, dtype, first_name, last_name, role)
VALUES ('owner1@example.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'OWNER', 'John', 'Doe', 'OWNER');

INSERT INTO users (email, password, dtype, first_name, last_name, role)
VALUES ('dteleaga@example.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'OWNER', 'Dragos', 'Teleaga', 'OWNER');

INSERT INTO animal (owner_id, name, species, breed, birth_date, medical_history)
VALUES (4, 'Max', 'Dog', 'Labrador', '2020-01-15', 'No issues');

-- Insert animals for the owner (use the actual owner ID or let Hibernate auto-manage)
INSERT INTO animal (owner_id, name, species, breed, birth_date, medical_history)
VALUES (5, 'Folly', 'Cat', 'European', '2020-01-15', 'No issues');

INSERT INTO users (email, password, dtype, first_name, last_name, role)
VALUES ('dteleaga2@example.com', '$2a$10$ueaDyqodECm2JKHAswIb0Osbff7HsZIr.bB0x0dd7ItZNH7dmYdKe', 'OWNER', 'Dragos2', 'Teleaga2', 'OWNER');

INSERT INTO animal (owner_id, name, species, breed, birth_date, medical_history)
VALUES (6, 'Max', 'Dog', 'Labrador', '2020-01-15', 'No issues');