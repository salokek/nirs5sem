ALTER TABLE deliveries RENAME COLUMN delivery_id TO record_id;

ALTER TABLE deliveries ADD COLUMN delivery_id BIGINT NOT NULL;

-- заодно обновим существующие записи, чтобы у них delivery_id == record_id
UPDATE deliveries SET delivery_id = record_id;

--ALTER TABLE deliveries ALTER COLUMN record_id ADD GENERATED ALWAYS AS IDENTITY;