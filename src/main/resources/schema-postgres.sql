-- Schema SQL para PostgreSQL
-- Compatible con las entidades JPA del proyecto
-- Crea tablas principales: users, locations, reports

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Tabla Ubicaciones
CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address VARCHAR(500),
    district VARCHAR(200)
);

-- Tabla Reportes
CREATE TABLE IF NOT EXISTS reports (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    category VARCHAR(150) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    CONSTRAINT fk_reports_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reports_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

-- ElementCollection: photoUrls (lista de strings)
CREATE TABLE IF NOT EXISTS reports_photo_urls (
    report_id BIGINT NOT NULL,
    photo_url VARCHAR(2000),
    CONSTRAINT fk_photo_urls_report FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);

-- ElementCollection: statusHistory (embeddable)
CREATE TABLE IF NOT EXISTS report_status_history (
    id BIGSERIAL PRIMARY KEY,
    report_id BIGINT NOT NULL,
    status VARCHAR(50),
    timestamp TIMESTAMP,
    notes VARCHAR(1000),
    CONSTRAINT fk_status_history_report FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_reports_user_id ON reports(user_id);
CREATE INDEX IF NOT EXISTS idx_reports_status ON reports(status);
CREATE INDEX IF NOT EXISTS idx_reports_category ON reports(category);
CREATE INDEX IF NOT EXISTS idx_reports_created_at ON reports(created_at);
CREATE INDEX IF NOT EXISTS idx_locations_district ON locations(district);

-- Comentarios para documentación
COMMENT ON TABLE users IS 'Tabla de usuarios del sistema (ciudadanos, agentes municipales, administradores)';
COMMENT ON TABLE locations IS 'Tabla de ubicaciones geográficas de los reportes';
COMMENT ON TABLE reports IS 'Tabla de reportes de problemas ambientales en Cusco';
COMMENT ON COLUMN users.role IS 'Roles: CITIZEN, MUNICIPAL_AGENT, ADMIN';
COMMENT ON COLUMN reports.status IS 'Estados: PENDIENTE, EN_PROCESO, RESUELTO, RECHAZADO';
