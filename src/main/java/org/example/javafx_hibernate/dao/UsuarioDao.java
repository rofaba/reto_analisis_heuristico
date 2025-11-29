package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.entity.Usuario;

public interface UsuarioDao {
/*     * Busca un usuario por su nombre de usuario y contraseña.
     * Retorna el usuario si se encuentra, de lo contrario lanza una excepción.
     */
    Usuario buscarPorNombreYPassword(String nombreUsuario, String password) throws Exception;
}
