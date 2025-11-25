package org.example.javafx_hibernate.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import org.example.javafx_hibernate.MainApp;
import org.example.javafx_hibernate.dao.CopiaDao;
import org.example.javafx_hibernate.dao.CopiaRepository;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class MainController {

    @FXML
    private Label lblBienvenida;

    @FXML
    private TableView<Copia> tvCopias;

    @FXML
    private TableColumn<Copia, String> colTitulo;

    @FXML
    private TableColumn<Copia, String> colEstado;

    @FXML
    private TableColumn<Copia, String> colSoporte;


    private final CopiaDao copiaDao = new CopiaRepository();

    @FXML
    public void initialize() {
        // Configurar columnas
        colTitulo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPelicula().getTitulo()));

        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));

        colSoporte.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSoporte()));

        // Usuario logueado desde AuthService
        Usuario u = MainApp.getAuthService().getUsuarioActual();
        if (u != null) {
            lblBienvenida.setText("Bienvenido, " + u.getNombreUsuario() + " (" + u.getRol() + ")");
            cargarCopias(u);
        }
        tvCopias.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Copia seleccionada = tvCopias.getSelectionModel().getSelectedItem();
                if (seleccionada != null) {
                    abrirDetalleCopia(seleccionada);
                }
            }
        });

    }

    private void cargarCopias(Usuario usuario) {
        try {
            List<Copia> copias = copiaDao.listarPorUsuario(usuario);
            System.out.println("Copias encontradas para " + usuario.getNombreUsuario() +
                    ": " + copias.size());
            tvCopias.setItems(FXCollections.observableArrayList(copias));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void onCerrarSesion() throws Exception {
        MainApp.getAuthService().logout();
        MainApp.setRoot("login-view");
    }
    private void abrirDetalleCopia(Copia copia) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/javafx_hibernate/detalle-copia-view.fxml")
            );
            Parent root = loader.load();

            DetalleCopiaController controller = loader.getController();
            controller.setCopia(copia);

            Stage stage = new Stage();
            stage.setTitle("Detalle de la copia");
            stage.setScene(new Scene(root));
            stage.initOwner(tvCopias.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el detalle de la copia.");
        }
    }
    @FXML
    private void onEliminarCopia() {
        Copia seleccionada = tvCopias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Debe seleccionar una copia para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que desea eliminar esta copia?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                copiaDao.eliminarCopia(seleccionada.getId());  // ← usamos TU método
                Usuario u = MainApp.getAuthService().getUsuarioActual();
                cargarCopias(u); // refrescamos la tabla
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al eliminar la copia.");
            }
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void onNuevaCopia() {
        // De momento solo para que no reviente
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad 'Nueva copia' aún no implementada.");
        alert.showAndWait();
    }
    @FXML
    private void onEditarCopia() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad 'Editar copia' aún no implementada.");
        alert.showAndWait();
    }

}
