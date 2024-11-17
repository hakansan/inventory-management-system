ALTER TABLE product
    ADD COLUMN discounted_price NUMERIC(10, 2),
ADD COLUMN discounted BOOLEAN DEFAULT FALSE;

CREATE TABLE discount (
                          id SERIAL PRIMARY KEY,
                          product_id INT NOT NULL,
                          discount_rate NUMERIC(5, 2) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          created_by VARCHAR(255) NOT NULL,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_by VARCHAR(255),
                          active BOOLEAN DEFAULT TRUE,
                          FOREIGN KEY (product_id) REFERENCES product (id)
);