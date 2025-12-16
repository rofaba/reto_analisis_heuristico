package org.example.javafx_hibernate.dao;


import org.example.javafx_hibernate.entity.Pelicula;
import java.util.List;

/*
    * Interfaz DAO para la entidad Pelicula.
 */
public interface PeliculaDao {
    void guardar(Pelicula pelicula);
    List<Pelicula> listarTodas();
    int deleteById(Long id);
}