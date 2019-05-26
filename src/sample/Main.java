package sample;

import client.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {

    //ad Gga3 - может всё
    //qw S6tL
    //zx 642G
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("language.fxml"));
        primaryStage.setTitle("App You");
        primaryStage.getIcons().add(new Image("/image/image.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 270, 240));
        primaryStage.show();
        try {
            new Client().doCom("1", "", "");
        } catch (Exception ex){
            AlertInfo.showAlert(Alert.AlertType.ERROR, primaryStage, "Stop", "System error");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
