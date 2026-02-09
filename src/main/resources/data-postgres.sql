-- Datos semilla para PostgreSQL
-- Nota: Las contraseñas están hasheadas con BCrypt
-- Contraseñas: admin123, juan123, maria123

-- Limpiar datos existentes (opcional - comentar si no quieres borrar datos)
-- TRUNCATE TABLE reports_photo_urls, report_status_history, reports, locations, users CASCADE;

-- Usuarios
INSERT INTO users (email, password, first_name, last_name, phone, role, active, created_at) VALUES
    ('admin@cusco-limpio.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5L9R3U0M1JhQqH3r5O1Z7n7h7n7h7', 'Admin', 'Sistema', '+51999888777', 'ADMIN', TRUE, CURRENT_TIMESTAMP),
    ('juan.perez@cusco.pe', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5L9R3U0M1JhQqH3r5O1Z7n7h7n7h7', 'Juan', 'Pérez', '+51987654321', 'CITIZEN', TRUE, CURRENT_TIMESTAMP),
    ('maria.garcia@cusco.pe', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5L9R3U0M1JhQqH3r5O1Z7n7h7n7h7', 'María', 'García', '+51976543210', 'MUNICIPAL_AGENT', TRUE, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Ubicaciones
INSERT INTO locations (latitude, longitude, address, district) VALUES
    (-13.531950, -71.967463, 'Plaza de Armas, Cusco', 'Cusco'),
    (-13.519385, -71.976391, 'Barrio de San Blas, Cusco', 'Cusco'),
    (-13.525260, -71.972584, 'Av. El Sol, Cusco', 'Wanchaq'),
    (-13.516830, -71.978690, 'Sacsayhuamán', 'Cusco'),
    (-13.535120, -71.969450, 'Mercado San Pedro', 'Cusco')
ON CONFLICT DO NOTHING;

-- Reportes (ajustar IDs según datos reales)
INSERT INTO reports (title, description, category, status, created_at, user_id, location_id)
SELECT
    'Basura acumulada en plaza',
    'Acumulación de basura cerca de la fuente principal de la Plaza de Armas. Se requiere limpieza urgente.',
    'Residuos Sólidos',
    'PENDIENTE',
    CURRENT_TIMESTAMP,
    (SELECT id FROM users WHERE email = 'juan.perez@cusco.pe' LIMIT 1),
    (SELECT id FROM locations WHERE district = 'Cusco' AND address LIKE '%Plaza de Armas%' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM reports WHERE title = 'Basura acumulada en plaza');

INSERT INTO reports (title, description, category, status, created_at, updated_at, user_id, location_id)
SELECT
    'Vandalismo en muro histórico',
    'Grafitis en el muro histórico del barrio de San Blas. Se requiere restauración.',
    'Vandalismo',
    'EN_PROCESO',
    CURRENT_TIMESTAMP - INTERVAL '2 days',
    CURRENT_TIMESTAMP,
    (SELECT id FROM users WHERE email = 'juan.perez@cusco.pe' LIMIT 1),
    (SELECT id FROM locations WHERE address LIKE '%San Blas%' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM reports WHERE title = 'Vandalismo en muro histórico');

INSERT INTO reports (title, description, category, status, created_at, user_id, location_id)
SELECT
    'Escombros en vía pública',
    'Escombros de construcción bloqueando la acera en Av. El Sol',
    'Escombros',
    'PENDIENTE',
    CURRENT_TIMESTAMP - INTERVAL '1 day',
    (SELECT id FROM users WHERE email = 'maria.garcia@cusco.pe' LIMIT 1),
    (SELECT id FROM locations WHERE address LIKE '%El Sol%' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM reports WHERE title = 'Escombros en vía pública');

-- URLs de fotos (ElementCollection)
-- Nota: Ajustar report_id según IDs reales de la base de datos
/*
INSERT INTO reports_photo_urls (report_id, photo_url) VALUES
    (1, 'https://example.com/photos/basura-plaza-1.jpg'),
    (1, 'https://example.com/photos/basura-plaza-2.jpg'),
    (2, 'https://example.com/photos/vandalismo-sanBlas-1.jpg');
*/
