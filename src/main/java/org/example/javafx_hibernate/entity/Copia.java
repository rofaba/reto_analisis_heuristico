package org.example.javafx_hibernate.entity;

import javax.persistence.*;
/*    * Entidad que representa una copia de una película en el sistema.
 */
@Entity
@Table(name = "copia")
public class Copia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String soporte;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;

    // Getters y setters
    public Integer getId() { return id; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getSoporte() { return soporte; }
    public void setSoporte(String soporte) { this.soporte = soporte; }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}