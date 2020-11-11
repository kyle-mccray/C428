package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ModProductController implements Initializable {

    @FXML private TextField productNameField;
    @FXML private TextField productInvField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMaxField;
    @FXML private TextField productMinField;
    @FXML private TextField productIdField;

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> partPriceCostColumn;
    @FXML private TextField searchPart;

    @FXML private TableView<Part> associatedPartTableView;
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceCostColumn;

    ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    Alert a = new Alert(Alert.AlertType.WARNING);
    Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);
    Alert delete = new Alert(Alert.AlertType.CONFIRMATION);
    private Product oldProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializePart();
        initializeAssociatedParts();

    }

    public void initializePart(){

        partIdColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        FilteredList<Part> filteredPartData = new FilteredList<>(Main.myInventory.getAllParts(), p ->true);

        searchPart.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPartData.setPredicate(Part -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(Part.getId()).toLowerCase().contains(lowerCaseFilter)){
                    //filter matches part ID
                    return true;
                }
                else if (String.valueOf(Part.getName()).toLowerCase().contains(lowerCaseFilter)){
                    //filter matches part name
                    return true;
                }
                return false;
            });
        });

        SortedList<Part> sortedPartData = new SortedList<>(filteredPartData);

        sortedPartData.comparatorProperty().bind(partTableView.comparatorProperty());
        partTableView.setItems(sortedPartData);

    }
    public void initializeAssociatedParts(){
        associatedPartIdColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartTableView.setItems(associatedPartsList);



    }
    public void handleAddAssociatedParts(ActionEvent event){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        associatedPartsList.addAll(selectedPart);
        //this line updates the view
        associatedPartTableView.setItems(associatedPartsList);
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
    public void deleteAssociatedPart(){
        Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        associatedPartsList.remove(selectedPart);
        associatedPartTableView.setItems(associatedPartsList);

    }
    public void handleDeleteAssociatedPart(ActionEvent event){
        Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        if(selectedPart!= null){
            delete.setTitle("Delete Part?");
            delete.setContentText("Are you sure you want to delete this Part?");
            delete.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> deleteAssociatedPart());
        }

    }
    public void handleSaveProduct(ActionEvent event) throws IOException {
        Product newProduct;
        newProduct = parseUserInput();
        if(newProduct != null){
            for(int i = 0; i < associatedPartsList.size(); i++) {
                newProduct.addAssociatedPart(associatedPartsList.get(i));
            }
            Main.myInventory.updateProduct(Main.myInventory.lookupProduct(oldProduct), newProduct);
            Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }


    }
    public Product parseUserInput(){
        int id = -999;
        int inv = -999;
        double price = -999;
        int max = -999;
        int min = -999;
        String name ="";
        try{
            id = Integer.parseInt(productIdField.getText());
            name = productNameField.getText();
            inv = Integer.parseInt(productInvField.getText());
            price = Double.parseDouble(productPriceField.getText());
            max = Integer.parseInt(productMaxField.getText());
            min = Integer.parseInt(productMinField.getText());
            if (max < min)
                throw new OutOfBoundsException("Max must be greater than or equal to Min");
            Product o = new Product(id, name, price, inv, max, min);
        return o;

        }
        catch(NullPointerException | NumberFormatException | OutOfBoundsException e){
            productNameField.clear();
            productInvField.clear();
            productPriceField.clear();
            productInvField.clear();
            productMaxField.clear();
            productMinField.clear();

            if (e instanceof OutOfBoundsException) {
                a.setContentText("Max must be greater than or equal to Min");
                a.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
        return null;
        }
    }
    public void initOldProduct(Product modProduct){
        oldProduct = modProduct;
       /* System.out.println(oldProduct.getName());
        System.out.println(oldProduct.getMin());
        System.out.println(oldProduct.getMax());*/
        productIdField.setText(Integer.toString(oldProduct.getId()));
        productNameField.setText(oldProduct.getName());
        productInvField.setText(Integer.toString(oldProduct.getStock()));
        productPriceField.setText(Double.toString(oldProduct.getPrice()));
        productMaxField.setText(Integer.toString(oldProduct.getMax()));
        productMinField.setText(Integer.toString(oldProduct.getMin()));
        associatedPartsList.setAll(oldProduct.getAllAssociatedParts());


    }

}
