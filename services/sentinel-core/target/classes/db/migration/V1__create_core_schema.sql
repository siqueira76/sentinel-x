CREATE TABLE agents (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL,
    credential_reference VARCHAR(255),
    metadata_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cameras (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    external_id VARCHAR(100) NOT NULL,
    display_name VARCHAR(255),
    location VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    metadata_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cameras_agent FOREIGN KEY (agent_id) REFERENCES agents (id),
    CONSTRAINT uk_cameras_agent_external UNIQUE (agent_id, external_id)
);

CREATE TABLE vehicle_events (
    id UUID PRIMARY KEY,
    agent_id BIGINT NOT NULL,
    camera_id BIGINT NOT NULL,
    external_event_id VARCHAR(100) NOT NULL,
    deduplication_key VARCHAR(255) NOT NULL UNIQUE,
    idempotency_key VARCHAR(255),
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    plate_number VARCHAR(32) NOT NULL,
    confidence NUMERIC(5,4),
    event_type VARCHAR(50) NOT NULL,
    direction VARCHAR(50),
    evidence_key VARCHAR(255),
    evidence_hash VARCHAR(255),
    evidence_content_type VARCHAR(100),
    evidence_size_bytes BIGINT,
    metadata_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_events_agent FOREIGN KEY (agent_id) REFERENCES agents (id),
    CONSTRAINT fk_vehicle_events_camera FOREIGN KEY (camera_id) REFERENCES cameras (id)
);

CREATE INDEX idx_vehicle_events_occurred_at ON vehicle_events (occurred_at);
CREATE INDEX idx_vehicle_events_plate_number ON vehicle_events (plate_number);
CREATE INDEX idx_vehicle_events_camera_id ON vehicle_events (camera_id);