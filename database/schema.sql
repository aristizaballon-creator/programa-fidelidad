SET NAMES utf8mb4;
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
