ALTER TABLE deliveries RENAME COLUMN record_id TO tmp_id;
ALTER TABLE deliveries RENAME COLUMN delivery_id TO record_id;
ALTER TABLE deliveries RENAME COLUMN tmp_id TO delivery_id;
