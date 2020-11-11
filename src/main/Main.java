package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static int partId = 1;
    public static int productId = 1;
    public static Inventory myInventory = new Inventory();

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene mainScene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
