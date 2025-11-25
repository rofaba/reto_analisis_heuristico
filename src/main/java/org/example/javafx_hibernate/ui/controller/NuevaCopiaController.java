package org.example.javafx_hibernate.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx_hibernate.MainApp;
import org.example.javafx_hibernate.config.HibernateUtil;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Pelicula;
import org.example.javafx_hibernate.entity.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class NuevaCopiaController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private ComboBox<String> cbSoporte;

    @FXML
    private Spinner<Integer> spCantidad;

    private Copia copiaEnEdicion;   // null = modo NUEVA, no null = modo EDITAR

    // Para poder pedir al MainController que recargue la tabla
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        // Valores fijos según tu comentario
        cbEstado.setItems(FXCollections.observableArrayList("BUENO", "DAÑADO"));
        cbSoporte.setItems(FXCollections.observableArrayList("DVD", "BLURAY", "VHS"));
        spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1)
        );

    }

    @FXML
    private void onGuardar() {
        String titulo = txtTitulo.getText() != null ? txtTitulo.getText().trim() : "";
        String estado = cbEstado.getValue();
        String soporte = cbSoporte.getValue();
        Integer cantidad = spCantidad.getValue();

        if (titulo.isEmpty() || estado == null || soporte == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe completar todos los campos.");
            return;
        }

        // Usuario logueado
        Usuario usuario = MainApp.getAuthService().getUsuarioActual();
        if (usuario == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "No hay usuario logueado.");
            return;
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // 1. Buscar película por título
            Query<Pelicula> queryPelicula = session.createQuery(
                    "FROM Pelicula p WHERE p.titulo = :titulo", Pelicula.class);
            queryPelicula.setParameter("titulo", titulo);
            Pelicula pelicula = queryPelicula.uniqueResult();

            if (pelicula == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "La película con título '" + titulo + "' no existe.");
                tx.rollback();
                return;
            }

            // --- Lógica de Edición vs. Creación/Agrupación ---
            if (copiaEnEdicion != null) {
                // Modo EDICIÓN (Problema 2: Guardaba como nuevo)

                // session.merge() es más seguro cuando el objeto viene de otra sesión
                Copia copiaParaActualizar = (Copia) session.merge(copiaEnEdicion);

                // Solo se actualizan los campos modificables
                copiaParaActualizar.setEstado(estado);
                copiaParaActualizar.setSoporte(soporte);
                copiaParaActualizar.setCantidad(cantidad); // Se guarda el nuevo valor ingresado

                mostrarAlerta(Alert.AlertType.INFORMATION, "Copia actualizada correctamente.");

            } else {
                // Modo NUEVA copia (Problema 1: No agrupaba)

                // Buscar si ya existe una copia idéntica (Película, Usuario, Estado, Soporte)
                Query<Copia> copiaQuery = session.createQuery(
                        "FROM Copia c WHERE c.pelicula = :pelicula " +
                                "AND c.usuario = :usuario " +
                                "AND c.estado = :estado " +
                                "AND c.soporte = :soporte", Copia.class);
                copiaQuery.setParameter("pelicula", pelicula);
                copiaQuery.setParameter("usuario", usuario);
                copiaQuery.setParameter("estado", estado);
                copiaQuery.setParameter("soporte", soporte);

                Copia copiaExistente = copiaQuery.uniqueResult();

                if (copiaExistente != null) {
                    // Existe: Aumentamos la cantidad
                    copiaExistente.setCantidad(copiaExistente.getCantidad() + cantidad);
                    session.update(copiaExistente);
                    mostrarAlerta(Alert.AlertType.INFORMATION,
                            "La copia ya existía. Cantidad aumentada a " + copiaExistente.getCantidad() + ".");
                } else {
                    // No existe: Creamos una nueva
                    Copia nuevaCopia = new Copia();
                    nuevaCopia.setPelicula(pelicula);
                    nuevaCopia.setUsuario(usuario);
                    nuevaCopia.setEstado(estado);
                    nuevaCopia.setSoporte(soporte);
                    nuevaCopia.setCantidad(cantidad);

                    session.persist(nuevaCopia);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Copia guardada correctamente.");
                }
            }

            // Commit de la transacción
            tx.commit();

            // Recargar tabla en MainController
            if (mainController != null) {
                mainController.cargarCopias(usuario);
            }

            cerrarVentana();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar la copia.");
        }
    }


    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setCopiaEnEdicion(Copia copia) {
        this.copiaEnEdicion = copia;

        // Rellenar campos con los datos de la copia seleccionada
        txtTitulo.setText(copia.getPelicula().getTitulo());
        txtTitulo.setDisable(true);  // No permitir cambiar película en edición

        cbEstado.setValue(copia.getEstado());
        cbSoporte.setValue(copia.getSoporte());
        spCantidad.getValueFactory().setValue(copia.getCantidad());
    }

}