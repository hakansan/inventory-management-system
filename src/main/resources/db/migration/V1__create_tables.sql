CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          created_by VARCHAR(255) NOT NULL,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_by VARCHAR(255),
                          active BOOLEAN DEFAULT TRUE
);

CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         price NUMERIC(10, 2) NOT NULL,
                         quantity INT NOT NULL,
                         category_id INT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         created_by VARCHAR(255) NOT NULL,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_by VARCHAR(255),
                         active BOOLEAN DEFAULT TRUE,
                         FOREIGN KEY (category_id) REFERENCES category (id)
);

INSERT INTO category (name, created_at, created_by, updated_at, updated_by, active)
VALUES
    ('Elektronik', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE),
    ('Kitaplar', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE),
    ('Kıyafetler', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE);

INSERT INTO product (name, price, quantity, category_id, created_at, created_by, updated_at, updated_by, active)
VALUES
    ('Laptop', 5000.00, 10, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE),
    ('Telefon', 3000.00, 15, 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE),
    ('Roman Kitabı', 50.00, 100, 2, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE),
    ('Tişört', 30.00, 200, 3, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', TRUE);