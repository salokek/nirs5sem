ALTER TABLE deliveries
    ADD COLUMN quantity INTEGER;
ALTER TABLE deliveries
    ADD COLUMN supplier_id INTEGER NOT NULL;

ALTER TABLE orders
    ADD COLUMN receipt_number VARCHAR(255);
ALTER TABLE orders
    ADD COLUMN order_date DATE NOT NULL;

