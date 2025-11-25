package org.example.javafx_hibernate.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Pelicula;

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

    private Copia copia;

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
    }

    @FXML
    private void onCerrar() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }
}
