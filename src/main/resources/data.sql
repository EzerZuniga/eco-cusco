-- Datos semilla para desarrollo
-- Nota: Las contraseñas están hasheadas con BCrypt

-- Usuarios
-- Contraseñas: admin123, juan123, maria123
INSERT INTO users (id, email, password, first_name, last_name, phone, role, active, created_at) VALUES
	(1, 'admin@cusco-limpio.com', '$2a$10$xVqXqXqXqXqXqXqXqXqXqOZQvZl5l5l5l5l5l5l5l5l5l5l5l5', 'Admin', 'Sistema', '+51999888777', 'ADMIN', TRUE, CURRENT_TIMESTAMP),
	(2, 'juan.perez@cusco.pe', '$2a$10$xVqXqXqXqXqXqXqXqXqXqOZQvZl5l5l5l5l5l5l5l5l5l5l5l5', 'Juan', 'Pérez', '+51987654321', 'CITIZEN', TRUE, CURRENT_TIMESTAMP),
	(3, 'maria.garcia@cusco.pe', '$2a$10$xVqXqXqXqXqXqXqXqXqXqOZQvZl5l5l5l5l5l5l5l5l5l5l5l5', 'María', 'García', '+51976543210', 'MUNICIPAL_AGENT', TRUE, CURRENT_TIMESTAMP);

-- Ubicaciones
INSERT INTO locations (id, latitude, longitude, address, district) VALUES
	(1, -13.531950, -71.967463, 'Plaza de Armas, Cusco', 'Cusco'),
	(2, -13.519385, -71.976391, 'Barrio de San Blas, Cusco', 'Cusco'),
	(3, -13.525260, -71.972584, 'Av. El Sol, Cusco', 'Wanchaq');

-- Reportes
INSERT INTO reports (id, title, description, category, status, created_at, updated_at, user_id, location_id) VALUES
	(1, 'Basura acumulada en plaza', 'Acumulación de basura cerca de la fuente principal de la Plaza de Armas', 'Residuos Sólidos', 'PENDIENTE', CURRENT_TIMESTAMP, NULL, 2, 1),
	(2, 'Vandalismo en muro histórico', 'Grafitis en el muro histórico del barrio de San Blas', 'Vandalismo', 'EN_PROCESO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2),
	(3, 'Escombros en vía pública', 'Escombros de construcción bloqueando la acera en Av. El Sol', 'Escombros', 'PENDIENTE', CURRENT_TIMESTAMP, NULL, 2, 3);

-- URLs de fotos (ElementCollection)
INSERT INTO reports_photo_urls (report_id, photo_url) VALUES
	(1, 'https://example.com/photos/basura-plaza-1.jpg'),
	(1, 'https://example.com/photos/basura-plaza-2.jpg'),
	(2, 'https://example.com/photos/vandalismo-sanBlas-1.jpg');

COMMIT;

