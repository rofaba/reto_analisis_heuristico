package org.example.javafx_hibernate.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pelicula")
public class Pelicula {
/*    * Entidad que representa una película en el sistema.
 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String genero;
    private Integer anio;
    private String imagen; // ruta relativa dentro de resources

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String director;

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Copia> copias = new HashSet<>();

    // Getters y setters
    public  Long getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public Set<Copia> getCopias() { return copias; }
    public void setCopias(Set<Copia> copias) { this.copias = copias; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}