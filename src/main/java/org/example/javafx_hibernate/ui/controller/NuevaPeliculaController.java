package org.example.javafx_hibernate.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx_hibernate.dao.PeliculaDao;
import org.example.javafx_hibernate.dao.PeliculaRepository;
import org.example.javafx_hibernate.entity.Pelicula;

import java.net.URL;
import java.util.ResourceBundle;

public class NuevaPeliculaController implements Initializable {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDirector;
    @FXML private Spinner<Integer> spAnio;
    @FXML private TextField txtGenero;
    @FXML private TextArea txtDescripcion;

    private final PeliculaDao peliculaDao = new PeliculaRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar Spinner para años (ej. 1900 a 2100, valor por defecto 2023)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, 2023);
        spAnio.setValueFactory(valueFactory);
    }

/*    * Maneja el evento de guardar una nueva película.
        Valida los campos y guarda la película en la base de datos.
 */
    @FXML
    public void onGuardar() {
        if (txtTitulo.getText().isEmpty() || txtDirector.getText().isEmpty()) {
            mostrarAlerta("Error", "El título y el director son obligatorios.");
            return;
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(txtTitulo.getText());
        pelicula.setDirector(txtDirector.getText());
        pelicula.setAnio(spAnio.getValue());
        pelicula.setGenero(txtGenero.getText());
        pelicula.setDescripcion(txtDescripcion.getText());

        try {
            peliculaDao.guardar(pelicula);
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta("Error al guardar", e.getMessage());
        }
    }

    @FXML
    public void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }


/*    * Muestra una alerta con el título y mensaje proporcionados.
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar en la alerta.
 */

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}