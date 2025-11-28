ALTER TABLE warehouse DROP COLUMN "purchase_cost";

ALTER TABLE warehouse
    ADD COLUMN quantity INTEGER;

ALTER TABLE deliveries
    ADD COLUMN purchase_cost DECIMAL(15,2) NOT NULL