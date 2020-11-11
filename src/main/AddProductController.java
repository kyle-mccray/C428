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

public class AddProductController implements Initializable {

    @FXML private TextField productNameField;
    @FXML private TextField productInvField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMaxField;
    @FXML private TextField productMinField;

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> partPriceCostColumn;
    @FXML private TextField searchPart;

    @FXML private TableView associatedPartTableView;
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceCostColumn;
    private Product newProduct;

    Alert a = new Alert(Alert.AlertType.WARNING);
    Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);
    Alert delete = new Alert(Alert.AlertType.CONFIRMATION);
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();




    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializePart();
        initializeAssociatedParts();

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
    public Product parseUserInput(){
        String productName = "";
        int inv = -999;
        double price = -999;
        int max = -999;
        int min = -999;
        int machId = -999;

        try{
            productName = productNameField.getText();
            inv = Integer.parseInt(productInvField.getText());
            price = Double.parseDouble(productPriceField.getText());
            max = Integer.parseInt(productMaxField.getText());
            min = Integer.parseInt(productMinField.getText());
            if (max < min)
                throw new OutOfBoundsException("Max must be greater than or equal to Min");
            Product o = new Product(Main.productId, productName, price, inv, max, min );
            Main.productId++;
            return o;


        }
        catch(NullPointerException | NumberFormatException | OutOfBoundsException e) {
            productNameField.clear();
            productInvField.clear();
            productPriceField.clear();
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
    public void handleAssociateParts(ActionEvent event){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        associatedParts.addAll(selectedPart);

    }
    public void deleteAssociatedPart(){
        Object selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        associatedParts.remove(selectedPart);

    }
    public void handleDeleteAssociatedPart(ActionEvent event){
        Object selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        if(selectedPart!= null){
            delete.setTitle("Delete Part?");
            delete.setContentText("Are you sure you want to delete this Part?");
            delete.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> deleteAssociatedPart());
        }

    }
    public void handleNewProduct(ActionEvent event) throws IOException {
        newProduct = parseUserInput();
        if(newProduct != null){
            for(int i = 0; i < associatedParts.size(); i++){
                newProduct.addAssociatedPart(associatedParts.get(i));
            }
            Main.myInventory.addProduct(newProduct);
            Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }

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
        associatedPartTableView.setItems(associatedParts);
    }


}