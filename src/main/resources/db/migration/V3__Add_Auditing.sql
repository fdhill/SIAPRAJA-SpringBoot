ALTER TABLE users ADD COLUMN created_at DATETIME;
ALTER TABLE users ADD COLUMN updated_at DATETIME;

ALTER TABLE students ADD COLUMN created_at DATETIME;
ALTER TABLE students ADD COLUMN updated_at DATETIME;

ALTER TABLE companies ADD COLUMN created_at DATETIME;
ALTER TABLE companies ADD COLUMN updated_at DATETIME;

ALTER TABLE teachers ADD COLUMN created_at DATETIME;
ALTER TABLE teachers ADD COLUMN updated_at DATETIME;

ALTER TABLE submissions ADD COLUMN created_at DATETIME;
ALTER TABLE submissions ADD COLUMN updated_at DATETIME;

ALTER TABLE monitorings ADD COLUMN created_at DATETIME;
ALTER TABLE monitorings ADD COLUMN updated_at DATETIME;

ALTER TABLE presences ADD COLUMN created_at DATETIME;
ALTER TABLE presences ADD COLUMN updated_at DATETIME;
