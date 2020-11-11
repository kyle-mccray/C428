package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public Inventory(){
        //add dummy partData data
/*      InHouse myPart = new InHouse(123,"SuperCoolPart",1,2,7,4,5);
        allParts.add(myPart);
        allParts.addAll(new InHouse(124,"Not a Part",4.89,2,7,4,4));
        allParts.addAll(new OutSourced(125,"Testing part",1,89,7,4,"TestingInc"));
        allParts.addAll(new OutSourced(126,"Wow its a part",1,2,7,4,"Testing"));

        Product myProduct = new Product(123,"This product can be modded", 123,7,9,8);
        myProduct.addAssociatedPart(myPart);
        allProducts.addAll(myProduct);
        allProducts.addAll(new Product(124,"xc Really Cool Product", 4,7,9,8));
        allProducts.addAll(new Product(125,"gh Really Cool Product", 3,7,9,8));
        allProducts.addAll(new Product(126,"My Really Cool Product", 2,7,9,8));*/



    }

    public void addPart(Part newPart){
        allParts.addAll(newPart);
    }
    public void addProduct(Product newProduct){allProducts.add(newProduct);}
    public ObservableList<Part> getAllParts(){
        return allParts;
    }
    public ObservableList<Product> getAllProducts(){return allProducts;}
    public int lookupPart(Part partToSearchFor){
        return allParts.indexOf(partToSearchFor);
    }
    public int lookupProduct(Product productToSearchFor){
        return allProducts.indexOf(productToSearchFor);
    }
    public void updatePart(int index, Part newPart){
       allParts.set(index,newPart);
    }
    public void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);}
    public Boolean deletePart(Part selectedPart){
      return allParts.remove(selectedPart);
    }
    public Boolean deleteProduct(Product selectedProduct){return allProducts.remove(selectedProduct);}

}
