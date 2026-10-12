 <img src="https://www.duoc.cl/wp-content/uploads/2022/09/logo-0.png" width="300" alt="Logo Duoc UC"/>

Proyecto desarrollado para la asignatura **Desarrollo Orientado a Objetos II (PRY2203)** de Duoc UC.
# 🏫 S9 - Gestor de Biblioteca Escolar

---

## 📌 Descripción

El proyecto **Gestor de Biblioteca Escolar** consiste en el desarrollo de una aplicación de escritorio en Java destinada a la administración de los recursos de una biblioteca escolar.

El sistema contempla la gestión de libros, categorías, estudiantes, usuarios y préstamos, utilizando una interfaz gráfica desarrollada con Java Swing y una base de datos MySQL para almacenar la información.

El proyecto aplica conceptos de programación orientada a objetos, separación de responsabilidades, manejo de excepciones y conexión a bases de datos mediante JDBC.

---

## 🛠️ Tecnologías utilizadas

- **Java:** desarrollo de la aplicación.
- **Java Swing:** construcción de la interfaz gráfica.
- **MySQL:** gestión de la base de datos.
- **JDBC:** conexión entre Java y MySQL.
- **Maven:** administración de dependencias y configuración del proyecto.
- **Git y GitHub:** control de versiones y alojamiento del código fuente.

---

## 📂 Estructura del proyecto

```text
GestorBibliotecaEscolar/
└── src/
    └── main/
        └── java/
            └── cl/duocuc/salgado/mich/
                ├── app/
                │   └── Main.java
                ├── controller/
                │   ├── CategoriaController.java
                │   ├── EstudianteController.java
                │   ├── LibroController.java
                │   ├── LoginController.java
                │   └── PrestamoController.java
                ├── dao/
                │   ├── CategoriaDAO.java
                │   ├── CategoriaDAOImpl.java
                │   ├── ConexionDB.java
                │   ├── EstudianteDAO.java
                │   ├── EstudianteDAOImpl.java
                │   ├── LibroDAO.java
                │   ├── LibroDAOImpl.java
                │   ├── PrestamoDAO.java
                │   ├── PrestamoDAOImpl.java
                │   ├── UsuarioDAO.java
                │   └── UsuarioDAOImpl.java
                ├── enums/
                │   └── Roles.java
                ├── model/
                │   ├── Categoria.java
                │   ├── Docente.java
                │   ├── Estudiante.java
                │   ├── Libro.java
                │   ├── Persona.java
                │   ├── PersonaGenerica.java
                │   ├── Prestamo.java
                │   ├── Rut.java
                │   └── Usuario.java
                ├── service/
                │   ├── CategoriaService.java
                │   ├── EstudianteService.java
                │   ├── LibroService.java
                │   ├── PrestamoService.java
                │   └── UsuarioService.java
                ├── ui/
                │   ├── CategoriaMenu.java
                │   ├── EstudianteMenu.java
                │   ├── LibroMenu.java
                │   ├── LoginMenu.java
                │   ├── Menu.java
                │   ├── PrestamoMenu.java
                │   └── UsuarioMenu.java
                └── util/
                    └── InvalidRutException.java
```

---

## 📚 Descripción de los paquetes

### `app`

Contiene la clase principal `Main.java`, encargada de iniciar la aplicación.

### `model`

Contiene las clases que representan las entidades del sistema:

- **Persona:** clase abstracta que representa los datos comunes de una persona.
- **Estudiante:** representa a los estudiantes registrados.
- **Docente:** representa a los docentes.
- **PersonaGenerica:** permite representar personas genéricas.
- **Rut:** administra la información y validación del RUT chileno.
- **Usuario:** representa las cuentas de acceso y sus roles.
- **Libro:** representa los libros de la biblioteca.
- **Categoria:** representa las categorías de los libros.
- **Prestamo:** representa los préstamos y su estado de devolución.

### `enums`

Contiene `Roles.java`, que define los roles utilizados para distinguir los tipos de usuarios del sistema.

### `exception` / `util`

La clase `InvalidRutException.java`, ubicada en `util`, permite representar errores relacionados con la validación del RUT mediante una excepción personalizada.

### `dao`

Contiene las interfaces DAO y sus implementaciones, responsables de las operaciones de acceso a la base de datos.

- **ConexionDB:** administra la conexión con MySQL.
- **CategoriaDAO / CategoriaDAOImpl:** operaciones de categorías.
- **EstudianteDAO / EstudianteDAOImpl:** operaciones de estudiantes.
- **LibroDAO / LibroDAOImpl:** operaciones de libros.
- **UsuarioDAO / UsuarioDAOImpl:** operaciones de usuarios y autenticación.
- **PrestamoDAO / PrestamoDAOImpl:** operaciones de préstamos y devoluciones.

### `service`

Contiene las clases de servicio que aplican validaciones y coordinan las operaciones del sistema.

### `controller`

Contiene las clases controladoras que conectan la interfaz gráfica con los servicios de la aplicación.

### `ui`

Contiene las ventanas y los menús gráficos para interactuar con el sistema:

- **LoginMenu:** interfaz de inicio de sesión.
- **Menu:** menú principal.
- **LibroMenu:** gestión de libros.
- **CategoriaMenu:** gestión de categorías.
- **EstudianteMenu:** gestión de estudiantes.
- **PrestamoMenu:** gestión de préstamos y devoluciones.
- **UsuarioMenu:** gestión de usuarios.

---

## 🧠 Conceptos de programación aplicados

- **Abstracción:** representación de características comunes mediante clases abstractas.
- **Encapsulamiento:** organización y protección de los atributos de las clases.
- **Herencia:** reutilización de características definidas en `Persona`.
- **Polimorfismo:** sobrescritura de métodos en clases derivadas.
- **Enumeraciones:** representación de los roles mediante `Roles`.
- **Manejo de excepciones:** tratamiento de errores y validaciones.
- **Patrón DAO:** separación del acceso a datos respecto de la lógica de la aplicación.
- **Patrón Singleton:** centralización de la instancia de conexión en `ConexionDB`.
- **Separación de responsabilidades:** organización en modelos, DAO, servicios, controladores e interfaz gráfica.
- **Integridad referencial:** relaciones entre registros mediante claves foráneas en MySQL.

---

## ⚙️ Requisitos

Para ejecutar el proyecto se necesita:

- Java JDK compatible con la configuración de `pom.xml`.
- MySQL Server.
- IntelliJ IDEA u otro entorno compatible con Maven.
- MySQL Connector/J, declarado como dependencia del proyecto.
- El script SQL para crear y preparar la base de datos.

---

## 🗄️ Base de datos

El sistema utiliza la base de datos `biblioteca_escolar_db`, que contempla las siguientes tablas:

- **usuarios:** almacena las cuentas de acceso y sus roles.
- **estudiantes:** almacena la información de los estudiantes.
- **categorias:** permite clasificar los libros.
- **libros:** almacena los datos de los libros y su stock.
- **prestamos:** registra los préstamos, las fechas y las devoluciones.

Las claves foráneas permiten relacionar los préstamos con los estudiantes y los libros correspondientes.

---

## ▶️ Ejecución del proyecto

1. Descargar o clonar el repositorio.
2. Abrir el proyecto desde IntelliJ IDEA.
3. Esperar a que Maven cargue las dependencias definidas en `pom.xml`.
4. Ejecutar el script SQL para crear la base de datos y sus tablas.
5. Verificar las credenciales de conexión en `ConexionDB.java`.
6. Confirmar que MySQL Server esté funcionando.
7. Ejecutar la clase `Main.java`, ubicada en el paquete `app`.
8. Iniciar sesión con un usuario previamente registrado en la base de datos.

---

## 👨‍💻 Autor

**Michael Salgado**

Proyecto desarrollado para la asignatura **Desarrollo Orientado a Objetos II (PRY2203)** de Duoc UC.
