CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255),
                       password VARCHAR(255),
                       dtype VARCHAR(31) NOT NULL,  -- Discriminator column for inheritance
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       role VARCHAR(255),                 -- Assuming Role is an enum mapped to ordinal
                       specialization VARCHAR(255)  -- Used only for Veterinarian, nullable
);
CREATE TABLE animal (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        owner_id BIGINT,                        -- Foreign key to users table
                        name VARCHAR(255) NOT NULL,
                        species VARCHAR(255) NOT NULL,
                        breed VARCHAR(255) NOT NULL,
                        birth_date DATE,
                        medical_history TEXT,                   -- Optional, can be null
                        CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);
