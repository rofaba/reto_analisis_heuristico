package org.example.javafx_hibernate.ui.controller;

import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Modality;



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

    @FXML
    private TableColumn<Copia, Integer> colCantidad;


    @FXML
    private Button btnNuevaCopia;

    @FXML
    private Button btnEditarCopia;

    @FXML
    private Button btnEliminarCopia;

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

        colCantidad.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());

        //cargarCopiasUsuarioActual();
        // Usuario logueado desde AuthService
        Usuario u = MainApp.getAuthService().getUsuarioActual();
        if (u != null) {
            lblBienvenida.setText("Bienvenido, " + u.getNombreUsuario() + " (" + u.getRol() + ")");
            lblBienvenida.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");
            lblBienvenida.setWrapText(true);
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
        btnEditarCopia.disableProperty().bind(
                tvCopias.getSelectionModel().selectedItemProperty().isNull()
        );

        btnEliminarCopia.disableProperty().bind(
                tvCopias.getSelectionModel().selectedItemProperty().isNull()
        );

    }
/*    * Carga las copias asociadas al usuario dado y las muestra en la tabla.
     * @param usuario El usuario cuyas copias se cargarán.
 */
    protected void cargarCopias(Usuario usuario) {
        try {
            List<Copia> copias = copiaDao.listarPorUsuario(usuario);
            System.out.println("Copias encontradas para " + usuario.getNombreUsuario() +
                    ": " + copias.size());
            tvCopias.setItems(FXCollections.observableArrayList(copias));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
/*    * Cierra la sesión del usuario actual y vuelve a la vista de login.
 */
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

    /*    * Elimina la copia seleccionada después de confirmar con el usuario.
     * Muestra una alerta si no hay ninguna copia seleccionada o si ocurre un error.
 */
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
                copiaDao.eliminarCopia(seleccionada.getId());
                Usuario u = MainApp.getAuthService().getUsuarioActual();
                cargarCopias(u); // refrescamos la tabla
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al eliminar la copia.");
            }
        }
    }

    /*    * Muestra una alerta de información con el mensaje proporcionado.
     * @param mensaje El mensaje a mostrar en la alerta.
 */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /*    * Abre la ventana para crear una nueva copia.
     * Después de cerrar la ventana, se espera que se haya llamado a cargarCopias() desde el controlador hijo.
 */
    @FXML
    private void onNuevaCopia() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/javafx_hibernate/nueva-copia-view.fxml")
            );
            Parent root = loader.load();

            NuevaCopiaController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Nueva copia");
            stage.setScene(new Scene(root));
            stage.initOwner(tvCopias.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait(); // Al cerrar, se habrá llamado a cargarCopias()

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de nueva copia.");
        }
    }

/*    * Abre la ventana para editar la copia seleccionada.
     * Muestra una alerta si no hay ninguna copia seleccionada o si ocurre un error.
 */
    @FXML
    private void onEditarCopia() {
        Copia seleccionada = tvCopias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Debes seleccionar una copia para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/javafx_hibernate/nueva-copia-view.fxml")
            );
            Parent root = loader.load();

            NuevaCopiaController controller = loader.getController();
            controller.setMainController(this);
            controller.setCopiaEnEdicion(seleccionada); // Pasar la copia seleccionada para edición

            Stage stage = new Stage();
            stage.setTitle("Editar copia");
            stage.setScene(new Scene(root));
            stage.initOwner(tvCopias.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

           stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de edición.");
        }
    }
}


