-- Seed data (only for development)
-- Password hash for "1234" using BCrypt (rounds=10)
-- You should use BCryptPasswordEncoder to generate this in production

-- Insert patron user (username: patron, password: password)
-- Note: BCrypt hash for "password" is used for development
INSERT INTO users (username, password_hash, role, active, created_at)
VALUES ('patron', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PATRON', true, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- Insert sample elevators
INSERT INTO elevators (
    identity_number, building_name, address, elevator_number, floor_count, capacity, speed,
    inspection_date, expiry_date, created_at, updated_at
)
VALUES
    ('ELEV-001', 'Central Business Center', '123 Main Street, Business District', 'A1', 5, 630, 1.0,
     CURRENT_DATE - INTERVAL '6 months', CURRENT_DATE + INTERVAL '6 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ELEV-002', 'Residential Complex Block B', '456 Residential Avenue, Block B', 'B1', 4, 450, 0.75,
     CURRENT_DATE - INTERVAL '11 months', CURRENT_DATE + INTERVAL '1 month', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ELEV-003', 'Commercial Center', '789 Commerce Boulevard, Suite 8', 'C1', 3, 800, 1.5,
     CURRENT_DATE - INTERVAL '13 months', CURRENT_DATE - INTERVAL '1 month', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (identity_number) DO NOTHING;

-- Insert sample parts
INSERT INTO parts (name, unit_price, stock, created_at)
VALUES
    ('Elevator Rope 8mm', 150.00, 10, CURRENT_TIMESTAMP),
    ('Motor Control Board', 2500.00, 3, CURRENT_TIMESTAMP),
    ('Door Motor', 1800.00, 5, CURRENT_TIMESTAMP),
    ('Lighting Fixture', 85.00, 20, CURRENT_TIMESTAMP),
    ('Safety Brake', 3200.00, 2, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

