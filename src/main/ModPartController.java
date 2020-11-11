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


public class ModPartController {

    @FXML private Label tempName;
    @FXML private TextField machineIdCompanyName;
    @FXML private TextField partNameTextField;
    @FXML private TextField inventoryTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField partIdField;
    @FXML private RadioButton inHouse;
    @FXML private RadioButton outSourced;
    @FXML private ToggleGroup group;

    private Part oldPart;
    Alert a = new Alert(Alert.AlertType.WARNING);
    Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);

    public ModPartController() throws IOException {
    }


    public void handleOut(ActionEvent event) {
        tempName.setText("Company Name");
        machineIdCompanyName.setPromptText("Company Name");


    }
    public void handleInHouse(ActionEvent event) {
        tempName.setText("Machine ID");
        machineIdCompanyName.setPromptText("Machine ID");

    }

    public void initModPart(Part sPart){
       oldPart = sPart;
        /*System.out.println("The selected Part's id is "+ oldPart.getId() + "\n" + "\n"+ "\n"+ "\n");
        System.out.println("The selected Part's type is "+ oldPart.getClass() + "\n" + "\n"+ "\n"+ "\n");*/
        if(oldPart instanceof InHouse && oldPart != null){
            partIdField.setText(Integer.toString(oldPart.getId()));
            partNameTextField.setText(oldPart.getName());
            inventoryTextField.setText(String.valueOf(oldPart.getStock()));
            priceTextField.setText(String.valueOf(oldPart.getPrice()));
            maxTextField.setText(String.valueOf(oldPart.getMax()));
            minTextField.setText(String.valueOf(oldPart.getMin()));
            machineIdCompanyName.setText(String.valueOf(((InHouse) oldPart).getMachineId()));
            tempName.setText("Machine ID");
            machineIdCompanyName.setPromptText("Machine ID");
            inHouse.setSelected(true);

        }
        if (oldPart instanceof OutSourced && oldPart != null){
            partIdField.setText(String.valueOf(oldPart.getId()));
            partNameTextField.setText(oldPart.getName());
            inventoryTextField.setText(String.valueOf(oldPart.getStock()));
            priceTextField.setText(String.valueOf(oldPart.getPrice()));
            maxTextField.setText(String.valueOf(oldPart.getMax()));
            minTextField.setText(String.valueOf(oldPart.getMin()));
            machineIdCompanyName.setText((((OutSourced) oldPart).getCompanyName()));
            tempName.setText("Company Name");
            machineIdCompanyName.setPromptText("Company Name");
            outSourced.setSelected(true);

        }

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
        int id = -999;
        int inv = -999;
        double price = -999;
        int max = -999;
        int min = -999;
        int machId = -999;
        String companyName = "";

        try {
            if (button == inHouse) {
                id = Integer.parseInt(partIdField.getText());
                String sPartName = partNameTextField.getText();
                inv = Integer.parseInt(inventoryTextField.getText());
                price = Double.parseDouble(priceTextField.getText());
                max = Integer.parseInt(maxTextField.getText());
                min = Integer.parseInt(minTextField.getText());
                machId = Integer.parseInt(machineIdCompanyName.getText());
                if (max < min)
                    throw new OutOfBoundsException("Max must be greater than or equal to Min");
                InHouse o = new InHouse(id, sPartName, price, inv, max, min, machId);
            return o;

            }
            if (button == outSourced) {
                id = Integer.parseInt(partIdField.getText());
                String sPartName = partNameTextField.getText();
                inv = Integer.parseInt(inventoryTextField.getText());
                price = Double.parseDouble(priceTextField.getText());
                max = Integer.parseInt(maxTextField.getText());
                min = Integer.parseInt(minTextField.getText());
                companyName = machineIdCompanyName.getText();
                if (max < min)
                    throw new OutOfBoundsException("Max must be greater than or equal to Min");
                OutSourced o = new OutSourced(id, sPartName, price, inv, max, min, companyName);

            return o;

            }
        }
        catch (NullPointerException | NumberFormatException | OutOfBoundsException e) {
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
    return null;
    }

    public void handleSavePart(ActionEvent event) throws IOException {
        Part editPart;
        if (group.getSelectedToggle() == inHouse) {
            editPart = parseUserInput(inHouse);
            if (editPart != null) {
                Main.myInventory.updatePart(Main.myInventory.lookupPart(oldPart),editPart);
                Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        if (group.getSelectedToggle() == outSourced) {
            editPart = parseUserInput(outSourced);
            if (editPart != null) {
                Main.myInventory.updatePart(Main.myInventory.lookupPart(oldPart),editPart);
                Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }




    }

}

