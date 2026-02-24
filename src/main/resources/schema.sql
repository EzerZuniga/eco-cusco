-- Schema SQL compatible con las entidades JPA del proyecto (H2)
-- Crea tablas principales: users, locations, reports
-- Usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
-- Ubicaciones
CREATE TABLE IF NOT EXISTS locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address VARCHAR(500),
    district VARCHAR(200)
);
-- Reportes
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    version BIGINT DEFAULT 0,
    title VARCHAR(255) NOT NULL,
    description CLOB,
    status VARCHAR(50) NOT NULL,
    category VARCHAR(150) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    CONSTRAINT fk_reports_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reports_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);
-- ElementCollection: photoUrls (lista de strings)
CREATE TABLE IF NOT EXISTS report_photo_urls (
    report_id BIGINT NOT NULL,
    photo_urls VARCHAR(2000),
    CONSTRAINT fk_photo_urls_report FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);
-- ElementCollection: statusHistory (embeddable)
CREATE TABLE IF NOT EXISTS report_status_history (
    report_id BIGINT NOT NULL,
    status VARCHAR(50),
    timestamp TIMESTAMP,
    notes VARCHAR(1000),
    CONSTRAINT fk_status_history_report FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);
-- Indexes
CREATE INDEX IF NOT EXISTS idx_reports_user_id ON reports(user_id);
CREATE INDEX IF NOT EXISTS idx_reports_status ON reports(status);
CREATE INDEX IF NOT EXISTS idx_locations_district ON locations(district);
