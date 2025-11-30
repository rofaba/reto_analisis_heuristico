package org.example.javafx_hibernate.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    @FXML private Label lblUsuario;
    @FXML private TableView<Pelicula> tvPeliculas;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colAnio;
    @FXML private TableColumn<Pelicula, String> colGenero;

    @FXML
    private TextField txtBuscar;

    private ObservableList<Pelicula> peliculas;        // lista completa
    private FilteredList<Pelicula> peliculasFiltradas; // lista filtrada

    private final PeliculaDao peliculaDao = new PeliculaRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsuario.setText("Hola, " + MainApp.getAuthService().getUsuarioActual().getNombreUsuario());

        // Configurar columnas sobre tvPeliculas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        cargarPeliculas();
        configurarFiltro();
    }
/*    * Carga las películas desde la base de datos y las asigna a la tabla.
     * Configura la lista filtrada para que la tabla siempre muestre los datos filtrados.
 */

    private void cargarPeliculas() {
        // carga desde BD
        peliculas = FXCollections.observableArrayList(peliculaDao.listarTodas());

        // FilteredList
        peliculasFiltradas = new FilteredList<>(peliculas, p -> true);

        // la tabla usa SIEMPRE la filtrada
        tvPeliculas.setItems(peliculasFiltradas);
    }

/*    * Maneja el evento de agregar una nueva película.
     * Abre una nueva ventana para ingresar los detalles de la película.
     * Después de cerrar la ventana, recarga la lista de películas.
 */

    @FXML
    public void onNuevaPelicula() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/org/example/javafx_hibernate/nueva-pelicula-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Nueva Película");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            stage.showAndWait();
            cargarPeliculas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCerrarSesion() {
        MainApp.getAuthService().logout();
        try {
            MainApp.setRoot("login-view");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*    * Configura el filtro para la búsqueda de películas en la tabla.
     * Actualiza la lista filtrada según el texto ingresado en el campo de búsqueda.
 */
    private void configurarFiltro() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtro = (newValue == null) ? "" : newValue.trim().toLowerCase();

            if (filtro.isEmpty()) {
                peliculasFiltradas.setPredicate(p -> true);
            } else {
                peliculasFiltradas.setPredicate(p -> {
                    String titulo   = p.getTitulo()   != null ? p.getTitulo().toLowerCase()   : "";
                    String director = p.getDirector() != null ? p.getDirector().toLowerCase() : "";
                    String genero   = p.getGenero()   != null ? p.getGenero().toLowerCase()   : "";

                    return titulo.contains(filtro)
                            || director.contains(filtro)
                            || genero.contains(filtro);
                });
            }
        });
    }
}

