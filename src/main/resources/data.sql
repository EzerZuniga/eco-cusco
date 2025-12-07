-- Datos semilla para desarrollo
-- Usuarios
INSERT INTO users (id, username, password, full_name, email, created_at) VALUES
	(1, 'admin', 'adminpass', 'Administrador', 'admin@local.test', CURRENT_TIMESTAMP),
	(2, 'juan', 'juanpass', 'Juan Perez', 'juan@local.test', CURRENT_TIMESTAMP);

-- Ubicaciones
INSERT INTO locations (id, latitude, longitude, address, district) VALUES
	(1, -13.531, -71.967, 'Plaza de Armas, Cusco', 'Cusco'),
	(2, -13.519, -71.976, 'San Blas, Cusco', 'Cusco');

-- Reportes
INSERT INTO reports (id, title, description, category, status, created_at, updated_at, user_id, location_id) VALUES
	(1, 'Basura en plaza', 'Acumulación de basura cerca de la fuente', 'Basura', 'PENDIENTE', CURRENT_TIMESTAMP, NULL, 2, 1),
	(2, 'Vandalismo en pared', 'Grafitis en el muro histórico', 'Vandalismo', 'PENDIENTE', CURRENT_TIMESTAMP, NULL, 2, 2);

-- Status history (si existe tabla status_history asociada)
-- INSERT INTO report_status_history (id, report_id, status, timestamp, notes) VALUES
--   (1,1,'PENDIENTE',CURRENT_TIMESTAMP,'Creado por seed');

COMMIT;

