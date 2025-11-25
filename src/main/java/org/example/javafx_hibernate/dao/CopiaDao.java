package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Usuario;

import java.util.List;

public interface CopiaDao {
    List<Copia> listarPorUsuario(Usuario usuario) throws Exception;

    void eliminarCopia(Integer id);
}
