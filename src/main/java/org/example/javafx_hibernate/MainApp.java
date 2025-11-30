package org.example.javafx_hibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.javafx_hibernate.service.AuthService;
import javafx.application.Platform;
import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static AuthService authService = new AuthService();

    public static AuthService getAuthService() {
        return authService;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Películas - Login");
        setRoot("login-view");

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/org/example/javafx_hibernate/" + fxml + ".fxml")
        );
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
