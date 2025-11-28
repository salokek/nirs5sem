
ALTER TABLE deliveries ADD COLUMN product_id INTEGER NOT NULL;

ALTER TABLE deliveries
    ADD CONSTRAINT fk_product_id
        FOREIGN KEY (product_id)
            REFERENCES catalog (product_id)
            ON DELETE CASCADE;
