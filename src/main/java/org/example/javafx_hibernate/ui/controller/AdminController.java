package org.example.javafx_hibernate.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx_hibernate.MainApp;
import org.example.javafx_hibernate.dao.PeliculaDao;
import org.example.javafx_hibernate.dao.PeliculaRepository;
import org.example.javafx_hibernate.entity.Pelicula;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
/*    * Controlador para la vista de administrador.
        Permite gestionar películas y cerrar sesión.
 */
    @FXML private Label lblUsuario;
    @FXML private TableView<Pelicula> tvPeliculas;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colAnio;
    @FXML private TableColumn<Pelicula, String> colGenero;

    private final PeliculaDao peliculaDao = new PeliculaRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsuario.setText("Hola, " + MainApp.getAuthService().getUsuarioActual().getNombreUsuario());

        // Configurar columnas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        cargarPeliculas();
    }
/*    * Carga las películas desde la base de datos y las muestra en la tabla.
 */
    private void cargarPeliculas() {
        tvPeliculas.setItems(FXCollections.observableArrayList(peliculaDao.listarTodas()));
    }
/*    * Abre la ventana para agregar una nueva película.
        Después de cerrar la ventana, refresca la tabla de películas.
 */
    @FXML
    public void onNuevaPelicula() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/org/example/javafx_hibernate/nueva-pelicula-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Nueva Película");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            // Esperar a que se cierre para refrescar la tabla
            stage.showAndWait();
            cargarPeliculas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*    * Cierra la sesión del usuario actual y vuelve a la vista de login.
 */
    @FXML
    public void onCerrarSesion() {
        MainApp.getAuthService().logout();
        try {
            MainApp.setRoot("login-view");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
