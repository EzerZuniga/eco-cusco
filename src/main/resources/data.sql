-- Datos semilla para desarrollo (H2)
-- Contraseñas en texto plano para referencia (NUNCA en producción):
--   admin@cusco-limpio.com  → admin123
--   juan.perez@cusco.pe     → juan123
--   maria.garcia@cusco.pe   → maria123
-- Usuarios
INSERT INTO users (
        id,
        email,
        password,
        first_name,
        last_name,
        phone,
        role,
        active,
        created_at
    )
VALUES (
        1,
        'admin@cusco-limpio.com',
        '$2b$10$kpxvO/H9jEyALA2KY3CseexDY4NJT3hOx2Y8ZImhJdoTb801UWYw.',
        'Admin',
        'Sistema',
        '+51999888777',
        'ADMIN',
        TRUE,
        CURRENT_TIMESTAMP
    ),
    (
        2,
        'juan.perez@cusco.pe',
        '$2b$10$NX.BofoBPXm4U8OS9fu0N.sePfL7/ULXzkIBRg.GX9t2L3bxp4oZa',
        'Juan',
        'Pérez',
        '+51987654321',
        'CITIZEN',
        TRUE,
        CURRENT_TIMESTAMP
    ),
    (
        3,
        'maria.garcia@cusco.pe',
        '$2b$10$UFLTRqV35IMZ23YdEb9a3uwzBFk2Oqcv4.cEJIipPyoVrtwOKAWsm',
        'María',
        'García',
        '+51976543210',
        'MUNICIPAL_AGENT',
        TRUE,
        CURRENT_TIMESTAMP
    );
-- Ubicaciones
INSERT INTO locations (id, latitude, longitude, address, district)
VALUES (
        1,
        -13.531950,
        -71.967463,
        'Plaza de Armas, Cusco',
        'Cusco'
    ),
    (
        2,
        -13.519385,
        -71.976391,
        'Barrio de San Blas, Cusco',
        'Cusco'
    ),
    (
        3,
        -13.525260,
        -71.972584,
        'Av. El Sol, Cusco',
        'Wanchaq'
    );
-- Reportes
INSERT INTO reports (
        id,
        version,
        title,
        description,
        category,
        status,
        created_at,
        updated_at,
        user_id,
        location_id
    )
VALUES (
        1,
        0,
        'Basura acumulada en plaza',
        'Acumulación de basura cerca de la fuente principal de la Plaza de Armas',
        'Residuos Sólidos',
        'PENDIENTE',
        CURRENT_TIMESTAMP,
        NULL,
        2,
        1
    ),
    (
        2,
        0,
        'Vandalismo en muro histórico',
        'Grafitis en el muro histórico del barrio de San Blas',
        'Vandalismo',
        'EN_PROCESO',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        2,
        2
    ),
    (
        3,
        0,
        'Escombros en vía pública',
        'Escombros de construcción bloqueando la acera en Av. El Sol',
        'Escombros',
        'PENDIENTE',
        CURRENT_TIMESTAMP,
        NULL,
        2,
        3
    );
-- URLs de fotos (ElementCollection)
INSERT INTO report_photo_urls (report_id, photo_urls)
VALUES (
        1,
        'https://example.com/photos/basura-plaza-1.jpg'
    ),
    (
        1,
        'https://example.com/photos/basura-plaza-2.jpg'
    ),
    (
        2,
        'https://example.com/photos/vandalismo-sanBlas-1.jpg'
    );
