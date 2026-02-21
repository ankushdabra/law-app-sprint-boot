-- Ensure profile_picture_data stores full Base64 text payloads.
-- Postgres migration: convert column to TEXT.
ALTER TABLE users DROP COLUMN IF EXISTS profile_picture_path;
ALTER TABLE users DROP COLUMN IF EXISTS profile_picture_content_type;
ALTER TABLE users DROP COLUMN IF EXISTS profile_picture_file_name;
ALTER TABLE users ALTER COLUMN profile_picture_data TYPE TEXT;
