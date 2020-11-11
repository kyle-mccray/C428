package main;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

public class InventoryController implements Initializable {

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> partPriceCostColumn;
    @FXML private TextField searchPart;

    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryLvlColumn;
    @FXML private TableColumn<Product, Double> productPriceCostColumn;
    @FXML private TextField searchProduct;
    @FXML private Button addPartButtonInv;
    Alert exit = new Alert(Alert.AlertType.CONFIRMATION);
    Alert delete = new Alert(Alert.AlertType.CONFIRMATION);


    @FXML
    public void addProductHandle(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    @FXML
    public void modPartHandle(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModPart.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ModPartController controller = loader.getController();
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        controller.initModPart(selectedPart);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void modProductHandle(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModProduct.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ModProductController controller = loader.getController();
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        controller.initOldProduct(selectedProduct);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleExit(){
        exit.setTitle("Exit");
        exit.setContentText("Are you sure you want to exit the App?");
        exit.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> closeApplication());
    }

    public void closeApplication(){
        Platform.exit();
    }


    public void deletePart(){
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        Main.myInventory.deletePart(selectedPart);
    }

    public void deleteProduct(){
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        Main.myInventory.deleteProduct(selectedProduct);
    }
    @FXML
    private void handleDeletePart(){
        delete.setTitle("Delete Part?");
        delete.setContentText("Are you sure you want to delete this Part?");
        delete.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> deletePart());

    }

    @FXML private void handleDeleteProduct(){
        delete.setTitle("Delete Product?");
        delete.setContentText("Are you sure you want to delete this Product?");
        delete.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> deleteProduct());

    }

    @FXML
    public void addPartHandle(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializePart();
        initializeProduct();

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

    public void initializeProduct(){

            productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            productInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            productPriceCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            FilteredList<Product> filteredProductData = new FilteredList<>(Main.myInventory.getAllProducts(), p ->true);

            searchProduct.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredProductData.setPredicate(Product -> {
                    if(newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (String.valueOf(Product.getId()).toLowerCase().contains(lowerCaseFilter)){
                        //filter matches part ID
                        return true;
                    }
                    else if (String.valueOf(Product.getName()).toLowerCase().contains(lowerCaseFilter)){
                        //filter matches part name
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Product> sortedProductData = new SortedList<>(filteredProductData);

            sortedProductData.comparatorProperty().bind(productsTableView.comparatorProperty());
            productsTableView.setItems(sortedProductData);

    }








}


