-- Datos semilla para PostgreSQL (entorno local)
-- Este script es idempotente y seguro para múltiples ejecuciones.

-- Contraseñas de referencia (NUNCA usar en producción):
-- admin@cusco-limpio.com  -> admin123
-- juan.perez@cusco.pe     -> juan123
-- maria.garcia@cusco.pe   -> maria123

INSERT INTO users (email, password, first_name, last_name, phone, role, active, created_at)
VALUES ('admin@cusco-limpio.com', '$2b$10$kpxvO/H9jEyALA2KY3CseexDY4NJT3hOx2Y8ZImhJdoTb801UWYw.', 'Admin', 'Sistema',
        '+51999888777', 'ADMIN', TRUE, CURRENT_TIMESTAMP),
       ('juan.perez@cusco.pe', '$2b$10$NX.BofoBPXm4U8OS9fu0N.sePfL7/ULXzkIBRg.GX9t2L3bxp4oZa', 'Juan', 'Pérez',
        '+51987654321', 'CITIZEN', TRUE, CURRENT_TIMESTAMP),
       ('maria.garcia@cusco.pe', '$2b$10$UFLTRqV35IMZ23YdEb9a3uwzBFk2Oqcv4.cEJIipPyoVrtwOKAWsm', 'María', 'García',
        '+51976543210', 'MUNICIPAL_AGENT', TRUE, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

INSERT INTO locations (latitude, longitude, address, district)
SELECT -13.531950, -71.967463, 'Plaza de Armas, Cusco', 'Cusco'
WHERE NOT EXISTS (
    SELECT 1 FROM locations
    WHERE latitude = -13.531950 AND longitude = -71.967463 AND address = 'Plaza de Armas, Cusco'
);

INSERT INTO locations (latitude, longitude, address, district)
SELECT -13.519385, -71.976391, 'Barrio de San Blas, Cusco', 'Cusco'
WHERE NOT EXISTS (
    SELECT 1 FROM locations
    WHERE latitude = -13.519385 AND longitude = -71.976391 AND address = 'Barrio de San Blas, Cusco'
);

INSERT INTO locations (latitude, longitude, address, district)
SELECT -13.525260, -71.972584, 'Av. El Sol, Cusco', 'Wanchaq'
WHERE NOT EXISTS (
    SELECT 1 FROM locations
    WHERE latitude = -13.525260 AND longitude = -71.972584 AND address = 'Av. El Sol, Cusco'
);

INSERT INTO reports (title, description, category, status, created_at, user_id, location_id)
SELECT 'Basura acumulada en plaza',
       'Acumulación de basura cerca de la fuente principal de la Plaza de Armas. Se requiere limpieza urgente.',
       'Residuos Sólidos',
       'PENDIENTE',
       CURRENT_TIMESTAMP,
       (SELECT id FROM users WHERE email = 'juan.perez@cusco.pe' LIMIT 1),
       (SELECT id FROM locations WHERE address = 'Plaza de Armas, Cusco' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM reports WHERE title = 'Basura acumulada en plaza');

INSERT INTO reports (title, description, category, status, created_at, updated_at, user_id, location_id)
SELECT 'Vandalismo en muro histórico',
       'Grafitis en el muro histórico del barrio de San Blas. Se requiere restauración.',
       'Vandalismo',
       'EN_PROCESO',
       CURRENT_TIMESTAMP - INTERVAL '2 days',
       CURRENT_TIMESTAMP,
       (SELECT id FROM users WHERE email = 'juan.perez@cusco.pe' LIMIT 1),
       (SELECT id FROM locations WHERE address = 'Barrio de San Blas, Cusco' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM reports WHERE title = 'Vandalismo en muro histórico');

INSERT INTO reports (title, description, category, status, created_at, user_id, location_id)
SELECT 'Escombros en vía pública',
       'Escombros de construcción bloqueando la acera en Av. El Sol.',
       'Escombros',
       'PENDIENTE',
       CURRENT_TIMESTAMP - INTERVAL '1 day',
       (SELECT id FROM users WHERE email = 'maria.garcia@cusco.pe' LIMIT 1),
       (SELECT id FROM locations WHERE address = 'Av. El Sol, Cusco' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM reports WHERE title = 'Escombros en vía pública');

INSERT INTO report_photo_urls (report_id, photo_urls)
SELECT r.id, 'https://example.com/photos/basura-plaza-1.jpg'
FROM reports r
WHERE r.title = 'Basura acumulada en plaza'
  AND NOT EXISTS (
      SELECT 1
      FROM report_photo_urls pu
      WHERE pu.report_id = r.id AND pu.photo_urls = 'https://example.com/photos/basura-plaza-1.jpg'
  );

INSERT INTO report_photo_urls (report_id, photo_urls)
SELECT r.id, 'https://example.com/photos/basura-plaza-2.jpg'
FROM reports r
WHERE r.title = 'Basura acumulada en plaza'
  AND NOT EXISTS (
      SELECT 1
      FROM report_photo_urls pu
      WHERE pu.report_id = r.id AND pu.photo_urls = 'https://example.com/photos/basura-plaza-2.jpg'
  );

INSERT INTO report_photo_urls (report_id, photo_urls)
SELECT r.id, 'https://example.com/photos/vandalismo-sanBlas-1.jpg'
FROM reports r
WHERE r.title = 'Vandalismo en muro histórico'
  AND NOT EXISTS (
      SELECT 1
      FROM report_photo_urls pu
      WHERE pu.report_id = r.id AND pu.photo_urls = 'https://example.com/photos/vandalismo-sanBlas-1.jpg'
  );

INSERT INTO report_status_history (report_id, status, timestamp, notes)
SELECT r.id, 'EN_PROCESO', CURRENT_TIMESTAMP, 'Atendido por agente municipal'
FROM reports r
WHERE r.title = 'Vandalismo en muro histórico'
  AND NOT EXISTS (
      SELECT 1
      FROM report_status_history h
      WHERE h.report_id = r.id AND h.status = 'EN_PROCESO'
  );
