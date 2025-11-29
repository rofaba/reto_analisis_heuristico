🎬 Catálogo de Copias de Películas

Aplicación de escritorio desarrollada en JavaFX + Hibernate para gestionar un catálogo de copias de películas. Permite visualizar información detallada, imágenes locales y administrar los registros desde una interfaz gráfica moderna.

🚀 Características

Interfaz desarrollada completamente con FXML + JavaFX

Carga de imágenes locales para cada película

Vista de detalle con diseño limpio y fondos personalizados

Tabla principal con ordenamiento y selección

Botones que se habilitan/deshabilitan automáticamente

Integración con Hibernate para persistencia en BD

🛠️ Tecnologías utilizadas

Java 17+

JavaFX

Hibernate ORM

MySQL

Maven (si lo agregas)

CSS para estilos

📂 Estructura del proyecto
src/main/java/org/example/javafx_hibernate/
entity/
repository/
ui/controller/

src/main/resources/
images/
fondos/
css/

🗄️ Base de datos

Tabla principal:

pelicula (titulo, anio, director, descripcion, genero, imagen)


La imagen no se guarda en la BD, solo la ruta relativa dentro del proyecto:

/images/peliculas/matrix.jpg

🖥️ Vistas principales

MainView: tabla de copias con barra superior personalizada

DetalleCopiaView: vista de detalles con imagen y fondo translúcido

📸 Manejo de imágenes

Las imágenes se cargan mediante:

Image img = new Image(
getClass().getResourceAsStream(ruta)
);


Ubicadas en:

src/main/resources/images/peliculas

▶️ Ejecución

Puedes ejecutar desde tu IDE (IntelliJ, Eclipse, VS Code), o empaquetar más adelante en un .jar.

🧩 Próximas mejoras recomendadas

CRUD completo desde la interfaz

Búsqueda por título o soporte

Estilos CSS separados por pantalla

Login real + usuarios en BD