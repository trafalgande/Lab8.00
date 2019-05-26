package sample;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller implements Initializable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authButton;

    @FXML
    private Button regButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authButton.setOnAction(event -> {
            authButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/auth.fxml"));
            loader.setResources(resources);

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
            stage.showAndWait();
        });

        regButton.setOnAction(event -> {
            regButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/regist.fxml"));
            loader.setResources(resources);

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

