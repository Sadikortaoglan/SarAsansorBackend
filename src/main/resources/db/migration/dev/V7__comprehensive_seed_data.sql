-- Comprehensive seed data for development and testing
-- Password hash for "password" using BCrypt (rounds=10)
-- All field values use English only

-- Users (1 patron + 2 technicians)
INSERT INTO users (username, password_hash, role, active, created_at)
VALUES 
    ('patron', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PATRON', true, CURRENT_TIMESTAMP),
    ('technician1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PERSONEL', true, CURRENT_TIMESTAMP),
    ('technician2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PERSONEL', true, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- Elevators (10 elevators in 5 different buildings)
INSERT INTO elevators (
    identity_number, building_name, address, elevator_number, floor_count, capacity, speed,
    technical_notes, drive_type, machine_brand, door_type, installation_year, serial_number,
    control_system, rope, modernization, inspection_date, expiry_date, created_at, updated_at
)
VALUES
    ('ELEV-001', 'Central Business Center', '123 Main Street, Business District', 'A1', 5, 630, 1.0,
     'Regular maintenance required', 'Hydraulic', 'Otis', 'Automatic', 2020, 'SN-001', 'Siemens', '6 ropes', '2020',
     CURRENT_DATE - INTERVAL '6 months', CURRENT_DATE + INTERVAL '6 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-002', 'Residential Complex Block B', '456 Residential Avenue, Block B', 'B1', 4, 450, 0.75,
     'Low usage', 'Traction', 'Schindler', 'Manual', 2019, 'SN-002', 'Mitsubishi', '4 ropes', NULL,
     CURRENT_DATE - INTERVAL '11 months', CURRENT_DATE + INTERVAL '1 month', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-003', 'Commercial Center', '789 Commerce Boulevard, Suite 8', 'C1', 3, 800, 1.5,
     'High traffic area', 'Traction', 'ThyssenKrupp', 'Automatic', 2021, 'SN-003', 'Kone', '8 ropes', NULL,
     CURRENT_DATE - INTERVAL '13 months', CURRENT_DATE - INTERVAL '1 month', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-004', 'Central Business Center', '123 Main Street, Business District', 'A2', 5, 630, 1.0,
     'Second elevator in building', 'Hydraulic', 'Otis', 'Automatic', 2020, 'SN-004', 'Siemens', '6 ropes', NULL,
     CURRENT_DATE - INTERVAL '3 months', CURRENT_DATE + INTERVAL '9 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-005', 'Tech Park Tower', '100 Innovation Drive, Floor 10', 'TP1', 10, 1000, 2.0,
     'Modern installation', 'Traction', 'Kone', 'Automatic', 2022, 'SN-005', 'Kone', '8 ropes', '2022',
     CURRENT_DATE - INTERVAL '2 months', CURRENT_DATE + INTERVAL '10 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-006', 'Residential Complex Block B', '456 Residential Avenue, Block B', 'B2', 4, 450, 0.75,
     'Standard residential', 'Traction', 'Schindler', 'Manual', 2019, 'SN-006', 'Mitsubishi', '4 ropes', NULL,
     CURRENT_DATE - INTERVAL '9 months', CURRENT_DATE + INTERVAL '3 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-007', 'Shopping Mall North Wing', '200 Retail Road, North Wing', 'M1', 4, 1600, 2.5,
     'Heavy duty for shopping', 'Traction', 'Otis', 'Automatic', 2018, 'SN-007', 'Otis', '10 ropes', NULL,
     CURRENT_DATE - INTERVAL '4 months', CURRENT_DATE + INTERVAL '8 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-008', 'Tech Park Tower', '100 Innovation Drive, Floor 10', 'TP2', 10, 1000, 2.0,
     'Second elevator in tower', 'Traction', 'Kone', 'Automatic', 2022, 'SN-008', 'Kone', '8 ropes', NULL,
     CURRENT_DATE - INTERVAL '1 month', CURRENT_DATE + INTERVAL '11 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-009', 'Hospital Main Building', '500 Health Avenue, Main Building', 'H1', 8, 1600, 2.0,
     'Medical facility - priority', 'Traction', 'ThyssenKrupp', 'Automatic', 2020, 'SN-009', 'ThyssenKrupp', '10 ropes', NULL,
     CURRENT_DATE - INTERVAL '8 months', CURRENT_DATE + INTERVAL '4 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('ELEV-010', 'Shopping Mall North Wing', '200 Retail Road, North Wing', 'M2', 4, 1600, 2.5,
     'Second elevator in mall', 'Traction', 'Otis', 'Automatic', 2018, 'SN-010', 'Otis', '10 ropes', NULL,
     CURRENT_DATE - INTERVAL '7 months', CURRENT_DATE + INTERVAL '5 months', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (identity_number) DO NOTHING;

-- Parts (20 parts)
INSERT INTO parts (name, unit_price, stock, created_at)
VALUES
    ('Elevator Rope 8mm', 150.00, 10, CURRENT_TIMESTAMP),
    ('Motor Control Board', 2500.00, 3, CURRENT_TIMESTAMP),
    ('Door Motor', 1800.00, 5, CURRENT_TIMESTAMP),
    ('Lighting Fixture', 85.00, 20, CURRENT_TIMESTAMP),
    ('Safety Brake', 3200.00, 2, CURRENT_TIMESTAMP),
    ('Cable 10mm', 200.00, 8, CURRENT_TIMESTAMP),
    ('Control Panel', 1500.00, 4, CURRENT_TIMESTAMP),
    ('Speed Governor', 2800.00, 3, CURRENT_TIMESTAMP),
    ('Car Operating Panel', 450.00, 6, CURRENT_TIMESTAMP),
    ('Hoistway Door Lock', 120.00, 15, CURRENT_TIMESTAMP),
    ('Door Operator', 2200.00, 4, CURRENT_TIMESTAMP),
    ('Buffer Spring', 350.00, 7, CURRENT_TIMESTAMP),
    ('Guide Rail', 180.00, 12, CURRENT_TIMESTAMP),
    ('Compensation Chain', 420.00, 5, CURRENT_TIMESTAMP),
    ('Emergency Battery', 680.00, 4, CURRENT_TIMESTAMP),
    ('LED Display', 95.00, 18, CURRENT_TIMESTAMP),
    ('Limit Switch', 65.00, 25, CURRENT_TIMESTAMP),
    ('Reducer Gear', 5200.00, 2, CURRENT_TIMESTAMP),
    ('Traction Sheave', 3800.00, 2, CURRENT_TIMESTAMP),
    ('Counterweight Frame', 2100.00, 3, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Maintenances (15 records)
INSERT INTO maintenances (
    elevator_id, date, description, technician_user_id, amount, is_paid, payment_date, created_at
)
VALUES
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-001'), CURRENT_DATE - INTERVAL '30 days', 'Monthly periodic maintenance', (SELECT id FROM users WHERE username = 'technician1'), 1500.00, true, CURRENT_DATE - INTERVAL '25 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-002'), CURRENT_DATE - INTERVAL '25 days', 'Quarterly inspection and lubrication', (SELECT id FROM users WHERE username = 'technician1'), 2000.00, true, CURRENT_DATE - INTERVAL '20 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-003'), CURRENT_DATE - INTERVAL '60 days', 'Emergency repair - motor issue', (SELECT id FROM users WHERE username = 'technician2'), 3500.00, true, CURRENT_DATE - INTERVAL '55 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-004'), CURRENT_DATE - INTERVAL '15 days', 'Monthly periodic maintenance', (SELECT id FROM users WHERE username = 'technician1'), 1500.00, false, NULL, CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-005'), CURRENT_DATE - INTERVAL '20 days', 'Annual comprehensive service', (SELECT id FROM users WHERE username = 'technician2'), 5000.00, true, CURRENT_DATE - INTERVAL '15 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-001'), CURRENT_DATE - INTERVAL '90 days', 'Quarterly maintenance', (SELECT id FROM users WHERE username = 'technician1'), 2000.00, true, CURRENT_DATE - INTERVAL '85 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-006'), CURRENT_DATE - INTERVAL '10 days', 'Monthly periodic maintenance', (SELECT id FROM users WHERE username = 'technician2'), 1500.00, false, NULL, CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-007'), CURRENT_DATE - INTERVAL '45 days', 'Replacement of door motor', (SELECT id FROM users WHERE username = 'technician1'), 2500.00, true, CURRENT_DATE - INTERVAL '40 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-008'), CURRENT_DATE - INTERVAL '5 days', 'Monthly periodic maintenance', (SELECT id FROM users WHERE username = 'technician2'), 1500.00, false, NULL, CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-009'), CURRENT_DATE - INTERVAL '35 days', 'Safety system inspection', (SELECT id FROM users WHERE username = 'technician1'), 3000.00, true, CURRENT_DATE - INTERVAL '30 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-010'), CURRENT_DATE - INTERVAL '50 days', 'Cable replacement', (SELECT id FROM users WHERE username = 'technician2'), 4000.00, true, CURRENT_DATE - INTERVAL '45 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-002'), CURRENT_DATE - INTERVAL '75 days', 'Quarterly maintenance', (SELECT id FROM users WHERE username = 'technician1'), 2000.00, true, CURRENT_DATE - INTERVAL '70 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-004'), CURRENT_DATE - INTERVAL '120 days', 'Quarterly maintenance', (SELECT id FROM users WHERE username = 'technician2'), 2000.00, true, CURRENT_DATE - INTERVAL '115 days', CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-005'), CURRENT_DATE - INTERVAL '8 days', 'Monthly periodic maintenance', (SELECT id FROM users WHERE username = 'technician1'), 1500.00, false, NULL, CURRENT_TIMESTAMP),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-007'), CURRENT_DATE - INTERVAL '28 days', 'Monthly periodic maintenance', (SELECT id FROM users WHERE username = 'technician2'), 1500.00, true, CURRENT_DATE - INTERVAL '23 days', CURRENT_TIMESTAMP);

-- Faults (10 records - 5 OPEN, 5 COMPLETED)
INSERT INTO faults (
    elevator_id, fault_subject, contact_person, building_authorized_message, description, status, created_at
)
VALUES
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-003'), 'Motor not working', 'John Smith', 'Urgent intervention required', 'Elevator stuck on 2nd floor', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-006'), 'Door malfunction', 'Sarah Johnson', 'Please fix as soon as possible', 'Door not closing properly', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-009'), 'Display error', 'Michael Brown', 'Priority repair needed', 'LED display showing error code', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '3 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-002'), 'Strange noise', 'Emily Davis', 'Please investigate', 'Unusual sound from motor room', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '5 hours'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-007'), 'Button not responding', 'Robert Wilson', 'Customer complaint', 'Ground floor button not working', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '12 hours'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-001'), 'Cable replacement completed', 'John Smith', 'Work completed successfully', 'All cables replaced', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '15 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-004'), 'Door motor fixed', 'Sarah Johnson', 'Thank you for quick service', 'Door motor replaced and tested', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '20 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-005'), 'Control panel updated', 'Michael Brown', 'System working perfectly', 'Control panel firmware updated', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-008'), 'Safety inspection passed', 'Emily Davis', 'All checks completed', 'Annual safety inspection completed', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '30 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-010'), 'Lubrication completed', 'Robert Wilson', 'Maintenance done', 'All moving parts lubricated', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '35 days');

-- Inspections (10 records)
INSERT INTO inspections (
    elevator_id, date, result, description, created_at
)
VALUES
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-001'), CURRENT_DATE - INTERVAL '180 days', 'PASSED', 'All checks completed, everything is in order', CURRENT_TIMESTAMP - INTERVAL '180 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-002'), CURRENT_DATE - INTERVAL '90 days', 'PASSED', 'Regular inspection completed successfully', CURRENT_TIMESTAMP - INTERVAL '90 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-003'), CURRENT_DATE - INTERVAL '365 days', 'FAILED', 'Motor issues detected, requires repair', CURRENT_TIMESTAMP - INTERVAL '365 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-004'), CURRENT_DATE - INTERVAL '180 days', 'PASSED', 'All systems operational', CURRENT_TIMESTAMP - INTERVAL '180 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-005'), CURRENT_DATE - INTERVAL '60 days', 'PASSED', 'New installation inspection - all good', CURRENT_TIMESTAMP - INTERVAL '60 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-006'), CURRENT_DATE - INTERVAL '90 days', 'PASSED', 'Standard inspection passed', CURRENT_TIMESTAMP - INTERVAL '90 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-007'), CURRENT_DATE - INTERVAL '270 days', 'PASSED', 'Comprehensive inspection completed', CURRENT_TIMESTAMP - INTERVAL '270 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-008'), CURRENT_DATE - INTERVAL '60 days', 'PASSED', 'New installation inspection passed', CURRENT_TIMESTAMP - INTERVAL '60 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-009'), CURRENT_DATE - INTERVAL '240 days', 'PASSED', 'Medical facility inspection - priority maintained', CURRENT_TIMESTAMP - INTERVAL '240 days'),
    ((SELECT id FROM elevators WHERE identity_number = 'ELEV-010'), CURRENT_DATE - INTERVAL '270 days', 'PASSED', 'Shopping mall inspection completed', CURRENT_TIMESTAMP - INTERVAL '270 days');

-- Payment Receipts (10 records - use subquery to get valid maintenance IDs)
INSERT INTO payment_receipts (
    maintenance_id, amount, payer_name, date, note, created_at
)
SELECT 
    m.id,
    CASE 
        WHEN m.amount IS NOT NULL THEN m.amount
        ELSE 1500.00
    END,
    'Building Management',
    COALESCE(m.payment_date, m.date),
    'Payment for maintenance #' || m.id,
    m.created_at
FROM maintenances m
WHERE m.is_paid = true
ORDER BY m.id
LIMIT 10;

