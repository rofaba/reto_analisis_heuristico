package org.example.javafx_hibernate.dao;


import org.example.javafx_hibernate.entity.Pelicula;
import java.util.List;

public interface PeliculaDao {
    void guardar(Pelicula pelicula);
    List<Pelicula> listarTodas();
}