-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('PATRON', 'PERSONEL')),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Elevators table
CREATE TABLE elevators (
    id BIGSERIAL PRIMARY KEY,
    identity_number VARCHAR(255) UNIQUE NOT NULL,
    building_name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    elevator_number VARCHAR(255),
    floor_count INTEGER,
    capacity INTEGER,
    speed DOUBLE PRECISION,
    technical_notes TEXT,
    drive_type VARCHAR(255),
    machine_brand VARCHAR(255),
    door_type VARCHAR(255),
    installation_year INTEGER,
    serial_number VARCHAR(255),
    control_system VARCHAR(255),
    rope VARCHAR(255),
    modernization VARCHAR(255),
    inspection_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Maintenances table
CREATE TABLE maintenances (
    id BIGSERIAL PRIMARY KEY,
    elevator_id BIGINT NOT NULL REFERENCES elevators(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    description TEXT,
    technician_user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    amount DOUBLE PRECISION,
    is_paid BOOLEAN NOT NULL DEFAULT false,
    payment_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Parts table
CREATE TABLE parts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price DOUBLE PRECISION NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Offers table
CREATE TABLE offers (
    id BIGSERIAL PRIMARY KEY,
    elevator_id BIGINT REFERENCES elevators(id) ON DELETE SET NULL,
    date DATE NOT NULL,
    vat_rate DOUBLE PRECISION NOT NULL DEFAULT 20.0,
    discount_amount DOUBLE PRECISION DEFAULT 0.0,
    subtotal DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    total_amount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Offer items table
CREATE TABLE offer_items (
    id BIGSERIAL PRIMARY KEY,
    offer_id BIGINT NOT NULL REFERENCES offers(id) ON DELETE CASCADE,
    part_id BIGINT NOT NULL REFERENCES parts(id),
    quantity INTEGER NOT NULL,
    unit_price DOUBLE PRECISION NOT NULL,
    line_total DOUBLE PRECISION NOT NULL
);

-- File attachments table
CREATE TABLE file_attachments (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL CHECK (entity_type IN ('ELEVATOR', 'MAINTENANCE', 'OFFER')),
    entity_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    url VARCHAR(1000),
    uploaded_by_user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Audit logs table
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    entity_id BIGINT,
    metadata_json TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_elevators_identity_number ON elevators(identity_number);
CREATE INDEX idx_elevators_expiry_date ON elevators(expiry_date);
CREATE INDEX idx_maintenances_elevator_id ON maintenances(elevator_id);
CREATE INDEX idx_maintenances_date ON maintenances(date);
CREATE INDEX idx_offer_items_offer_id ON offer_items(offer_id);
CREATE INDEX idx_offer_items_part_id ON offer_items(part_id);
CREATE INDEX idx_file_attachments_entity ON file_attachments(entity_type, entity_id);
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

