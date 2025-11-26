package org.example.javafx_hibernate.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javafx_hibernate.MainApp;
import org.example.javafx_hibernate.entity.Usuario;
import org.example.javafx_hibernate.service.AuthService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    private AuthService authService;

    @FXML
    public void initialize() {
        // Obtenemos el AuthService compartido desde MainApp
        this.authService = MainApp.getAuthService();
    }

    @FXML
    public void onLogin() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (MainApp.getAuthService().login(usuario, password)) {
            try {
                // VERIFICAR ROL
                if (MainApp.getAuthService().esAdmin()) {
                    MainApp.setRoot("admin-view"); // Vista nueva
                } else {
                    MainApp.setRoot("main-view");  // Vista normal
                }
            } catch (IOException e) {
                e.printStackTrace();
                lblMensaje.setText("Error al cargar la vista.");
            }
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }
}
