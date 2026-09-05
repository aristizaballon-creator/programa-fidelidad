CREATE DATABASE IF NOT EXISTS fidelidad
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fidelidad;

CREATE TABLE tipo_identificacion (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    codigo  VARCHAR(5)  NOT NULL UNIQUE,
    nombre  VARCHAR(60) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE pais (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(80) NOT NULL UNIQUE,
    codigo_iso2 CHAR(2)     NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE departamento (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    pais_id  INT NOT NULL,
    nombre   VARCHAR(100) NOT NULL,
    CONSTRAINT fk_departamento_pais
        FOREIGN KEY (pais_id) REFERENCES pais(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE KEY uq_departamento (pais_id, nombre)
) ENGINE=InnoDB;

CREATE TABLE ciudad (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    departamento_id  INT NOT NULL,
    nombre           VARCHAR(100) NOT NULL,
    CONSTRAINT fk_ciudad_departamento
        FOREIGN KEY (departamento_id) REFERENCES departamento(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE KEY uq_ciudad (departamento_id, nombre)
) ENGINE=InnoDB;

CREATE TABLE marca (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(60) NOT NULL UNIQUE,
    activa  BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE INDEX idx_departamento_pais ON departamento(pais_id);
CREATE INDEX idx_ciudad_departamento ON ciudad(departamento_id);

CREATE TABLE inscripcion (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_identificacion_id  INT NOT NULL,
    numero_identificacion   VARCHAR(20) NOT NULL,
    nombres                 VARCHAR(100) NOT NULL,
    apellidos               VARCHAR(100) NOT NULL,
    fecha_nacimiento        DATE NOT NULL,
    email                   VARCHAR(120) NULL,
    telefono                VARCHAR(20)  NULL,
    direccion               VARCHAR(200) NOT NULL,
    ciudad_id               INT NULL,
    ciudad_otra             VARCHAR(150) NULL,
    marca_id                INT NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                       ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inscripcion_tipo_id
        FOREIGN KEY (tipo_identificacion_id) REFERENCES tipo_identificacion(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_inscripcion_ciudad
        FOREIGN KEY (ciudad_id) REFERENCES ciudad(id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_inscripcion_marca
        FOREIGN KEY (marca_id) REFERENCES marca(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_inscripcion_doc_marca
        UNIQUE (tipo_identificacion_id, numero_identificacion, marca_id),
    CONSTRAINT chk_inscripcion_ciudad
        CHECK (ciudad_id IS NOT NULL OR (ciudad_otra IS NOT NULL AND ciudad_otra <> ''))
) ENGINE=InnoDB;

CREATE INDEX idx_inscripcion_ciudad     ON inscripcion(ciudad_id);
CREATE INDEX idx_inscripcion_marca      ON inscripcion(marca_id);
CREATE INDEX idx_inscripcion_documento  ON inscripcion(numero_identificacion);
USE fidelidad;

INSERT INTO tipo_identificacion (codigo, nombre) VALUES
('CC', 'Cédula de ciudadanía'),
('CE', 'Cédula de extranjería'),
('TI', 'Tarjeta de identidad'),
('PA', 'Pasaporte'),
('NIT', 'NIT');

INSERT INTO marca (nombre) VALUES
('Americanino'),
('American Eagle'),
('Chevignon'),
('Esprit'),
('Naf Naf'),
('Rifle');

INSERT INTO pais (nombre, codigo_iso2) VALUES
('Colombia', 'CO'),
('México', 'MX'),
('Perú', 'PE'),
('Ecuador', 'EC'),
('Chile', 'CL'),
('Argentina', 'AR'),
('España', 'ES'),
('Estados Unidos', 'US'),
('Venezuela', 'VE');

INSERT INTO departamento (pais_id, nombre)
SELECT id, d.nombre FROM pais, (
    SELECT 'Amazonas' AS nombre UNION ALL SELECT 'Antioquia' UNION ALL
    SELECT 'Arauca' UNION ALL SELECT 'Atlántico' UNION ALL
    SELECT 'Bogotá D.C.' UNION ALL SELECT 'Bolívar' UNION ALL
    SELECT 'Boyacá' UNION ALL SELECT 'Caldas' UNION ALL
    SELECT 'Caquetá' UNION ALL SELECT 'Casanare' UNION ALL
    SELECT 'Cauca' UNION ALL SELECT 'Cesar' UNION ALL
    SELECT 'Chocó' UNION ALL SELECT 'Córdoba' UNION ALL
    SELECT 'Cundinamarca' UNION ALL SELECT 'Guainía' UNION ALL
    SELECT 'Guaviare' UNION ALL SELECT 'Huila' UNION ALL
    SELECT 'La Guajira' UNION ALL SELECT 'Magdalena' UNION ALL
    SELECT 'Meta' UNION ALL SELECT 'Nariño' UNION ALL
    SELECT 'Norte de Santander' UNION ALL SELECT 'Putumayo' UNION ALL
    SELECT 'Quindío' UNION ALL SELECT 'Risaralda' UNION ALL
    SELECT 'San Andrés y Providencia' UNION ALL SELECT 'Santander' UNION ALL
    SELECT 'Sucre' UNION ALL SELECT 'Tolima' UNION ALL
    SELECT 'Valle del Cauca' UNION ALL SELECT 'Vaupés' UNION ALL
    SELECT 'Vichada'
) AS d
WHERE pais.nombre = 'Colombia';

INSERT INTO departamento (pais_id, nombre)
SELECT id, 'Ciudad de México' FROM pais WHERE nombre = 'México' UNION ALL
SELECT id, 'Jalisco' FROM pais WHERE nombre = 'México' UNION ALL
SELECT id, 'Nuevo León' FROM pais WHERE nombre = 'México' UNION ALL
SELECT id, 'Lima' FROM pais WHERE nombre = 'Perú' UNION ALL
SELECT id, 'Arequipa' FROM pais WHERE nombre = 'Perú' UNION ALL
SELECT id, 'Pichincha' FROM pais WHERE nombre = 'Ecuador' UNION ALL
SELECT id, 'Guayas' FROM pais WHERE nombre = 'Ecuador' UNION ALL
SELECT id, 'Región Metropolitana' FROM pais WHERE nombre = 'Chile' UNION ALL
SELECT id, 'Buenos Aires' FROM pais WHERE nombre = 'Argentina' UNION ALL
SELECT id, 'Madrid' FROM pais WHERE nombre = 'España' UNION ALL
SELECT id, 'Cataluña' FROM pais WHERE nombre = 'España' UNION ALL
SELECT id, 'Florida' FROM pais WHERE nombre = 'Estados Unidos' UNION ALL
SELECT id, 'Nueva York' FROM pais WHERE nombre = 'Estados Unidos' UNION ALL
SELECT id, 'Distrito Capital' FROM pais WHERE nombre = 'Venezuela';

INSERT INTO ciudad (departamento_id, nombre)
SELECT dep.id, c.nombre
FROM departamento dep
JOIN pais p ON p.id = dep.pais_id AND p.nombre = 'Colombia'
JOIN (
    SELECT 'Amazonas' AS depto, 'Leticia' AS nombre UNION ALL
    SELECT 'Amazonas', 'Puerto Nariño' UNION ALL
    SELECT 'Antioquia', 'Medellín' UNION ALL
    SELECT 'Antioquia', 'Envigado' UNION ALL
    SELECT 'Antioquia', 'Bello' UNION ALL
    SELECT 'Arauca', 'Arauca' UNION ALL
    SELECT 'Arauca', 'Saravena' UNION ALL
    SELECT 'Atlántico', 'Barranquilla' UNION ALL
    SELECT 'Atlántico', 'Soledad' UNION ALL
    SELECT 'Bogotá D.C.', 'Bogotá' UNION ALL
    SELECT 'Bolívar', 'Cartagena' UNION ALL
    SELECT 'Bolívar', 'Magangué' UNION ALL
    SELECT 'Boyacá', 'Tunja' UNION ALL
    SELECT 'Boyacá', 'Duitama' UNION ALL
    SELECT 'Caldas', 'Manizales' UNION ALL
    SELECT 'Caldas', 'La Dorada' UNION ALL
    SELECT 'Caquetá', 'Florencia' UNION ALL
    SELECT 'Casanare', 'Yopal' UNION ALL
    SELECT 'Cauca', 'Popayán' UNION ALL
    SELECT 'Cesar', 'Valledupar' UNION ALL
    SELECT 'Chocó', 'Quibdó' UNION ALL
    SELECT 'Córdoba', 'Montería' UNION ALL
    SELECT 'Cundinamarca', 'Soacha' UNION ALL
    SELECT 'Cundinamarca', 'Zipaquirá' UNION ALL
    SELECT 'Cundinamarca', 'Chía' UNION ALL
    SELECT 'Guainía', 'Inírida' UNION ALL
    SELECT 'Guaviare', 'San José del Guaviare' UNION ALL
    SELECT 'Huila', 'Neiva' UNION ALL
    SELECT 'La Guajira', 'Riohacha' UNION ALL
    SELECT 'Magdalena', 'Santa Marta' UNION ALL
    SELECT 'Meta', 'Villavicencio' UNION ALL
    SELECT 'Nariño', 'Pasto' UNION ALL
    SELECT 'Norte de Santander', 'Cúcuta' UNION ALL
    SELECT 'Putumayo', 'Mocoa' UNION ALL
    SELECT 'Quindío', 'Armenia' UNION ALL
    SELECT 'Risaralda', 'Pereira' UNION ALL
    SELECT 'San Andrés y Providencia', 'San Andrés' UNION ALL
    SELECT 'Santander', 'Bucaramanga' UNION ALL
    SELECT 'Santander', 'Floridablanca' UNION ALL
    SELECT 'Sucre', 'Sincelejo' UNION ALL
    SELECT 'Tolima', 'Ibagué' UNION ALL
    SELECT 'Valle del Cauca', 'Cali' UNION ALL
    SELECT 'Valle del Cauca', 'Palmira' UNION ALL
    SELECT 'Valle del Cauca', 'Buenaventura' UNION ALL
    SELECT 'Vaupés', 'Mitú' UNION ALL
    SELECT 'Vichada', 'Puerto Carreño'
) AS c ON c.depto = dep.nombre;

INSERT INTO ciudad (departamento_id, nombre)
SELECT dep.id, c.nombre
FROM departamento dep
JOIN (
    SELECT 'Ciudad de México' AS depto, 'Ciudad de México' AS nombre UNION ALL
    SELECT 'Jalisco', 'Guadalajara' UNION ALL
    SELECT 'Nuevo León', 'Monterrey' UNION ALL
    SELECT 'Lima', 'Lima' UNION ALL
    SELECT 'Arequipa', 'Arequipa' UNION ALL
    SELECT 'Pichincha', 'Quito' UNION ALL
    SELECT 'Guayas', 'Guayaquil' UNION ALL
    SELECT 'Región Metropolitana', 'Santiago' UNION ALL
    SELECT 'Buenos Aires', 'Ciudad Autónoma de Buenos Aires' UNION ALL
    SELECT 'Buenos Aires', 'La Plata' UNION ALL
    SELECT 'Madrid', 'Madrid' UNION ALL
    SELECT 'Cataluña', 'Barcelona' UNION ALL
    SELECT 'Florida', 'Miami' UNION ALL
    SELECT 'Nueva York', 'Nueva York' UNION ALL
    SELECT 'Distrito Capital', 'Caracas'
) AS c ON c.depto = dep.nombre;
