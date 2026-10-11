package cl.duocuc.salgado.mich.enums;

public enum Roles {

    // Acceso completo al sistema
    ADMINISTRADOR,

    // Acceso a libros disponibles, préstamos y devoluciones
    ESTUDIANTE,

    // Acceso a libros disponibles y solicitudes de nuevos libros
    DOCENTE,

    // Solo puede visualizar los libros disponibles
    VISITA
}