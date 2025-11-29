package org.example.javafx_hibernate.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Pelicula;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DetalleCopiaController {

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblAnio;
    @FXML
    private Label lblDirector;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblSoporte;
    @FXML
    private ImageView ivImagen;

    @FXML
    private Label lblCantidad;

    private Copia copia;

    /*    * Establece la copia cuya información se mostrará en la vista.
     * Actualiza las etiquetas y la imagen según los datos de la copia y su película asociada.
     */

    public void setCopia(Copia copia) {
        this.copia = copia;

        Pelicula peli = copia.getPelicula();

        lblTitulo.setText(peli.getTitulo());
        lblGenero.setText(peli.getGenero());
        lblAnio.setText(peli.getAnio() != null ? peli.getAnio().toString() : "");
        lblDirector.setText(peli.getDirector());
        lblDescripcion.setText(peli.getDescripcion());
        lblEstado.setText(copia.getEstado());
        lblSoporte.setText(copia.getSoporte());
        lblCantidad.setText(copia.getCantidad().toString());
        cargarImagenPelicula(peli);

    }

    @FXML
    private void onCerrar() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }
/*    * Carga y muestra la imagen de la película asociada a la copia.
     * Si no hay imagen o ocurre un error, se muestra una imagen vacía.
     */

    private void cargarImagenPelicula(Pelicula peli) {
        String ruta = peli.getImagen();   // ruta relativa dentro de resources

        if (ruta != null && !ruta.isBlank()) {
            try {
                var is = getClass().getResourceAsStream(ruta);
                if (is == null) {
                    System.out.println("No se encontró la imagen en: " + ruta);
                    ivImagen.setImage(null);
                    return;
                }

                Image img = new Image(is);   // InputStream
                ivImagen.setImage(img);

            } catch (Exception e) {
                e.printStackTrace();
                ivImagen.setImage(null);
            }
        } else {
            ivImagen.setImage(null);
        }
    }

}
