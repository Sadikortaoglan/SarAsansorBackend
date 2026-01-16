-- Add description column to parts table
ALTER TABLE parts ADD COLUMN IF NOT EXISTS description TEXT;
