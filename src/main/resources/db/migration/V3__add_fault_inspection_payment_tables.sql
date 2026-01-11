-- Faults table
CREATE TABLE faults (
    id BIGSERIAL PRIMARY KEY,
    elevator_id BIGINT NOT NULL REFERENCES elevators(id) ON DELETE CASCADE,
    fault_subject VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255) NOT NULL,
    building_authorized_message TEXT,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'COMPLETED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inspections table
CREATE TABLE inspections (
    id BIGSERIAL PRIMARY KEY,
    elevator_id BIGINT NOT NULL REFERENCES elevators(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    result VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Payment receipts table
CREATE TABLE payment_receipts (
    id BIGSERIAL PRIMARY KEY,
    maintenance_id BIGINT NOT NULL REFERENCES maintenances(id) ON DELETE CASCADE,
    amount DOUBLE PRECISION NOT NULL,
    payer_name VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_faults_elevator_id ON faults(elevator_id);
CREATE INDEX idx_faults_status ON faults(status);
CREATE INDEX idx_faults_created_at ON faults(created_at);
CREATE INDEX idx_inspections_elevator_id ON inspections(elevator_id);
CREATE INDEX idx_inspections_date ON inspections(date);
CREATE INDEX idx_payment_receipts_maintenance_id ON payment_receipts(maintenance_id);
CREATE INDEX idx_payment_receipts_date ON payment_receipts(date);

