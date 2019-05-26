package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LangController {

    @FXML
    private Button enBut;

    @FXML
    private Button ruBut;

    @FXML
    private Button spaBut;

    @FXML
    private Button serbBut;

    @FXML
    void initialize() {
        enBut.setOnAction(event -> {
            enBut.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.locale", new Locale("en"));
            loader.setLocation(getClass().getResource("/sample/auth.fxml"));
            loader.setResources(resourceBundle);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/image/image.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.NONE);
            stage.show();
        });
        ruBut.setOnAction(event -> {
            ruBut.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.locale", new Locale("ru"));
            loader.setLocation(getClass().getResource("/sample/auth.fxml"));
            loader.setResources(resourceBundle);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/image/image.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.NONE);
            stage.show();
        });
        spaBut.setOnAction(event -> {
            spaBut.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.locale", new Locale("spa"));
            loader.setLocation(getClass().getResource("/sample/auth.fxml"));
            loader.setResources(resourceBundle);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/image/image.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.NONE);
            stage.show();
        });
        serbBut.setOnAction(event -> {
            serbBut.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.locale", new Locale("srp"));
            loader.setLocation(getClass().getResource("/sample/auth.fxml"));
            loader.setResources(resourceBundle);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/image/image.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.NONE);
            stage.show();
        });
    }
}