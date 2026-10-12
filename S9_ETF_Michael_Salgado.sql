-- =====================================================
-- PROYECTO: GESTOR DE BIBLIOTECA ESCOLAR
-- BASE DE DATOS: biblioteca_escolar_db
-- =====================================================

CREATE DATABASE IF NOT EXISTS biblioteca_escolar_db;
USE biblioteca_escolar_db;

-- =====================================================
-- TABLA: ESTUDIANTES
-- =====================================================

CREATE TABLE IF NOT EXISTS estudiantes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    rut INT NOT NULL,
    dv CHAR(1) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apaterno VARCHAR(50) NOT NULL,
    amaterno VARCHAR(50) NOT NULL,
    curso VARCHAR(12) NOT NULL,
    correo VARCHAR(40) NOT NULL
);

-- =====================================================
-- TABLA: USUARIOS
-- =====================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    rut INT NOT NULL,
    dv CHAR(1) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    correo VARCHAR(40) NOT NULL,
    clave VARCHAR(50) NOT NULL,
    rol VARCHAR(30) NOT NULL
);

-- =====================================================
-- TABLA: CATEGORIAS
-- =====================================================

CREATE TABLE IF NOT EXISTS categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_categoria VARCHAR(20) NOT NULL
);

-- =====================================================
-- TABLA: LIBROS
-- =====================================================

CREATE TABLE IF NOT EXISTS libros (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(40) NOT NULL,
    editorial VARCHAR(100),
    isbn VARCHAR(20),
    stock INT NOT NULL,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);

-- =====================================================
-- TABLA: PRESTAMOS
-- =====================================================

CREATE TABLE IF NOT EXISTS prestamos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_estudiante INT NOT NULL,
    id_libro INT NOT NULL,
    f_prestamo DATE NOT NULL,
    f_devolucion DATE NOT NULL,
    devuelto BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id),
    FOREIGN KEY (id_libro) REFERENCES libros(id)
);

-- =====================================================
-- POBLACION INICIAL: CATEGORIAS
-- =====================================================

INSERT INTO categorias (nombre_categoria)
SELECT 'Literatura'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre_categoria = 'Literatura'
);

INSERT INTO categorias (nombre_categoria)
SELECT 'Ciencia'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre_categoria = 'Ciencia'
);

INSERT INTO categorias (nombre_categoria)
SELECT 'Historia'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre_categoria = 'Historia'
);

INSERT INTO categorias (nombre_categoria)
SELECT 'Tecnología'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre_categoria = 'Tecnología'
);

INSERT INTO categorias (nombre_categoria)
SELECT 'Matemáticas'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre_categoria = 'Matemáticas'
);

-- =====================================================
-- POBLACION INICIAL: LIBROS
-- =====================================================

INSERT INTO libros (titulo, autor, editorial, isbn, stock, id_categoria)
SELECT 'Don Quijote de la Mancha', 'Miguel de Cervantes',
       'Editorial Clasica', '9788420412146', 5, c.id
FROM categorias c
WHERE c.nombre_categoria = 'Literatura'
AND NOT EXISTS (
    SELECT 1 FROM libros WHERE isbn = '9788420412146'
);

INSERT INTO libros (titulo, autor, editorial, isbn, stock, id_categoria)
SELECT 'Cien años de soledad', 'Gabriel García Márquez',
       'Editorial Sudamericana', '9780307474728', 4, c.id
FROM categorias c
WHERE c.nombre_categoria = 'Literatura'
AND NOT EXISTS (
    SELECT 1 FROM libros WHERE isbn = '9780307474728'
);

INSERT INTO libros (titulo, autor, editorial, isbn, stock, id_categoria)
SELECT 'Breve historia del tiempo', 'Stephen Hawking',
       'Planeta', '9788408006720', 3, c.id
FROM categorias c
WHERE c.nombre_categoria = 'Ciencia'
AND NOT EXISTS (
    SELECT 1 FROM libros WHERE isbn = '9788408006720'
);

INSERT INTO libros (titulo, autor, editorial, isbn, stock, id_categoria)
SELECT 'Historia del mundo', 'J. M. Roberts',
       'Debate', '9788499920304', 2, c.id
FROM categorias c
WHERE c.nombre_categoria = 'Historia'
AND NOT EXISTS (
    SELECT 1 FROM libros WHERE isbn = '9788499920304'
);

INSERT INTO libros (titulo, autor, editorial, isbn, stock, id_categoria)
SELECT 'Introducción a la programación', 'Autor de ejemplo',
       'Editorial Académica', '9780000000001', 5, c.id
FROM categorias c
WHERE c.nombre_categoria = 'Tecnología'
AND NOT EXISTS (
    SELECT 1 FROM libros WHERE isbn = '9780000000001'
);

-- =====================================================
-- POBLACION INICIAL: ESTUDIANTES
-- =====================================================

INSERT INTO estudiantes
    (rut, dv, nombre, apaterno, amaterno, curso, correo)
SELECT 12345678, '5', 'Estudiante', 'Prueba', 'Uno',
       '1A', 'estudiante1@biblioteca.cl'
WHERE NOT EXISTS (
    SELECT 1 FROM estudiantes
    WHERE correo = 'estudiante1@biblioteca.cl'
);

INSERT INTO estudiantes
    (rut, dv, nombre, apaterno, amaterno, curso, correo)
SELECT 11222333, '4', 'Estudiante', 'Prueba', 'Dos',
       '2A', 'estudiante2@biblioteca.cl'
WHERE NOT EXISTS (
    SELECT 1 FROM estudiantes
    WHERE correo = 'estudiante2@biblioteca.cl'
);

-- =====================================================
-- POBLACION INICIAL: USUARIO ADMINISTRADOR
-- =====================================================
INSERT INTO usuarios (rut, dv, nombre, correo, clave, rol)
SELECT 12345678, '5', 'Administrador',
       'admin@biblioteca.cl', 'Admin123', 'ADMINISTRADOR'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios
    WHERE correo = 'admin@biblioteca.cl'
);