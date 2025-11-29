package org.example.javafx_hibernate.service;

import org.example.javafx_hibernate.dao.UsuarioDao;
import org.example.javafx_hibernate.dao.UsuarioRepository;
import org.example.javafx_hibernate.entity.Usuario;

public class AuthService {
/*
    * Servicio de autenticación para gestionar el login y logout de usuarios.
 */
    private final UsuarioDao usuarioDao;
    private Usuario usuarioActual;

    public AuthService() {
        this.usuarioDao = new UsuarioRepository();
    }
/*
    * Intenta autenticar a un usuario con el nombre de usuario y contraseña proporcionados.
    * Retorna true si la autenticación es exitosa, de lo contrario retorna false.
 */
    public boolean login(String nombreUsuario, String password) {
        try {
            Usuario u = usuarioDao.buscarPorNombreYPassword(nombreUsuario, password);
            if (u != null) {
                this.usuarioActual = u;
                return true;
            } else {
                this.usuarioActual = null;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.usuarioActual = null;
            return false;
        }
    }
/*    * Retorna el usuario actualmente autenticado.
 */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean esAdmin() {
        return usuarioActual != null &&
                "ADMIN".equalsIgnoreCase(usuarioActual.getRol());
    }

    public void logout() {
        this.usuarioActual = null;
    }
}
