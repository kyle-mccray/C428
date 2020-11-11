package main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPartController {
    @FXML private ToggleGroup group;
    @FXML private RadioButton inHouse;
    @FXML private RadioButton outSourced;
    @FXML private Label tempName;
    @FXML private TextField machineIdCompanyName;
    @FXML private TextField partNameTextField;
    @FXML private TextField inventoryTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;

    Alert a = new Alert(Alert.AlertType.WARNING);
    Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);


    public void handleOut(ActionEvent event) {
        tempName.setText("Company Name");
        machineIdCompanyName.setPromptText("Company Name");


    }
    public void handleInHouse(ActionEvent event) {
        tempName.setText("Machine ID");
        machineIdCompanyName.setPromptText("Machine ID");

    }
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
    public void handleCancel(ActionEvent event){
        cancel.setTitle("Return to Main Menu?");
        cancel.setContentText("Are you sure you want to return to the Main Menu?");
        cancel.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        returnToMainMenu(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }
    public Part parseUserInput(RadioButton button) {

        int inv = -999;
        double price = -999;
        int max = -999;
        int min = -999;
        int machId = -999;
        String companyName = "";

        try {
            if (button == inHouse) {
                String sPartName = partNameTextField.getText();
                inv = Integer.parseInt(inventoryTextField.getText());
                price = Double.parseDouble(priceTextField.getText());
                max = Integer.parseInt(maxTextField.getText());
                min = Integer.parseInt(minTextField.getText());
                machId = Integer.parseInt(machineIdCompanyName.getText());
                if (max < min)
                    throw new OutOfBoundsException("Max must be greater than or equal to Min");
                InHouse o = new InHouse(Main.partId, sPartName, price, inv, max, min, machId);
                Main.partId++;
                return  o;
            }
            if (button == outSourced) {
                String sPartName = partNameTextField.getText();
                inv = Integer.parseInt(inventoryTextField.getText());
                price = Double.parseDouble(priceTextField.getText());
                max = Integer.parseInt(maxTextField.getText());
                min = Integer.parseInt(minTextField.getText());
                companyName = machineIdCompanyName.getText();
                if (max < min)
                    throw new OutOfBoundsException("Max must be greater than or equal to Min");
                OutSourced o = new OutSourced(Main.partId, sPartName, price, inv, max, min, companyName);
                Main.partId++;
                return o;
            }
             else
                return null;
        }
        catch (NullPointerException | NumberFormatException | OutOfBoundsException e)
        {
            partNameTextField.clear();
            inventoryTextField.clear();
            priceTextField.clear();
            maxTextField.clear();
            minTextField.clear();
            machineIdCompanyName.clear();
            if (e instanceof OutOfBoundsException)
            {
                a.setContentText("Max must be greater than or equal to Min");
                a.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
            return null;

        }

    }
    public void handleNewPart(ActionEvent event) throws IOException {
            if (group.getSelectedToggle() == inHouse) {
                Part newPart = parseUserInput(inHouse);
                if (newPart != null) {
                    Main.myInventory.addPart(newPart);
                    Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }
            }
            if (group.getSelectedToggle() == outSourced) {
                Part newPart = parseUserInput(outSourced);
                if (newPart != null) {
                    Main.myInventory.addPart(newPart);
                    Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }
            }




    }

}





