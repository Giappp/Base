package com.controller.product;

import com.controller.AlertMessages;
import com.controller.dashboard.DashboardController;
import com.controller.data;
import com.db.dao.JDBCConnect;
import com.entities.Product;
import com.model.ProductCategoryModel;
import com.model.ProductModel;
import com.model.SupplierModel;
import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductController implements Initializable {

    Parent root;

    Scene fxmlFile;

    Stage window;

    private static final int itemPerPages = 12;

    private final ProductModel productModel = new ProductModel();

    @FXML
    private AnchorPane dashboardProduct;

    @FXML
    private AnchorPane updateScene;

    @FXML
    private TableView<Product> tblvProduct;

    @FXML
    private TableColumn<Product, Integer> productColId;

    @FXML
    private TableColumn<Product, String> productColType;

    @FXML
    private TableColumn<Product, String> productColBrand;

    @FXML
    private TableColumn<Product, String> productColName;

    @FXML
    private TableColumn<Product, Integer> productColAmount;

    @FXML
    private TableColumn<Product, Double> productColPrice;

    @FXML
    private TableColumn<Product, String> productColStatus;

    @FXML
    private Text txtProductId;

    @FXML
    private Button addProductUpdatebtn;

    @FXML
    private TextField addProductId;

    @FXML
    private Button productDeleteBtn;

    @FXML
    private Pagination productPg;

    @FXML
    private TextField addProductImportedpriceTf;
    
    @FXML
    private AnchorPane addProductScene;

    @FXML
    private Button addProductAddBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> addProductBrandCb;

    @FXML
    private TextField addProductNameTf;

    @FXML
    private Button addProductNewBrandBtn;

    @FXML
    private Button addProductNewTypeBtn;

    @FXML
    private TextField addProductSalespriceTf;

    @FXML
    private ComboBox<String> addProductTypeCb;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private ImageView addproductImageView;
    @FXML
    private TextField searchTf;

    private String imageUrl;

    AlertMessages alertMessages;
    ObservableList<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setUpPagination();
        ObservableList<String> listbrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
        ObservableList<String> listtypes = FXCollections.observableArrayList(new ProductCategoryModel().getType());
        addProductBrandCb.setItems(listbrands);
        addProductTypeCb.setItems(listtypes);
        List<String> statuslist = Arrays.asList("Available", "Unavailable");
        cbStatus.setItems(FXCollections.observableList(statuslist));

        addProductNewBrandBtn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/newSupplier.fxml", "Supplier Management");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
            addProductBrandCb.setItems(listBrands);
            ObservableList<String> listCategory = FXCollections.observableArrayList(new ProductCategoryModel().getType());
            addProductTypeCb.setItems(listCategory);
        });

        addProductSalespriceTf.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });

        addProductImportedpriceTf.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });

        addProductAddBtn.setOnAction(event -> {
            String imageUrl = null;
            if (addproductImageView != null && addproductImageView.getImage() != null) {
                imageUrl = addproductImageView.getImage().getUrl();
            } else {
                String currentPath = System.getProperty("user.dir");
                imageUrl = currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg";
            }
            String brand = addProductBrandCb.getSelectionModel().getSelectedItem();
            String type = addProductTypeCb.getSelectionModel().getSelectedItem();
            String name = addProductNameTf.getText();
            String price = addProductSalespriceTf.getText();
            String importedPrice = addProductImportedpriceTf.getText();
            String status = cbStatus.getSelectionModel().getSelectedItem();
            if (brand != null && type != null && name != null && price != null
                    && status != null) {
                int brandId = new SupplierModel().getIdSupplier(brand);
                int typeId = new ProductCategoryModel().getProductCategoryId(type);
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Add " + name + " To Product lists?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        Product product = new Product(name, brandId, typeId, 0, Double.parseDouble(price), Double.parseDouble(importedPrice), imageUrl, status);
                        DBAdd(product);
                        resetTable();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert inValid = new Alert(Alert.AlertType.ERROR);
                inValid.setContentText("Please fill product information correctly");
                inValid.showAndWait();
            }
        });

        addProductUpdatebtn.setOnAction(event -> {
            Integer id = Integer.valueOf(addProductId.getText());
            String brand = addProductBrandCb.getSelectionModel().getSelectedItem();
            String type = addProductTypeCb.getSelectionModel().getSelectedItem();
            String name = addProductNameTf.getText();
            String price = addProductSalespriceTf.getText();
            String status = cbStatus.getSelectionModel().getSelectedItem();
            String importedPrice = addProductImportedpriceTf.getText();
            String imageUrl = addproductImageView.getImage().getUrl();
            if (brand != null && type != null && name != null && price != null
                    && imageUrl != null && status != null) {
                int brandId = new SupplierModel().getIdSupplier(brand);
                int typeId = new ProductCategoryModel().getProductCategoryId(type);
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Update " + name + " with id " + id);
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)) {
                        Product product = new Product(id, name, brand, brandId, type, typeId, Double.parseDouble(price), status, imageUrl, Double.parseDouble(importedPrice));
                        DBUpdate(product);
                        resetTable();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert inValid = new Alert(Alert.AlertType.ERROR);
                inValid.setContentText("Please fill product information correctly");
                inValid.showAndWait();
            }
        });

        productDeleteBtn.setOnAction(event -> {
            Integer id = Integer.valueOf(productColId.getText());
            String brand = addProductBrandCb.getSelectionModel().getSelectedItem();
            String type = addProductTypeCb.getSelectionModel().getSelectedItem();
            String name = addProductNameTf.getText();
            String price = addProductSalespriceTf.getText();
            String status = cbStatus.getSelectionModel().getSelectedItem();
            String imageUrl = addproductImageView.getImage().getUrl();
            String importedPrice = addProductImportedpriceTf.getText();
            if (brand != null && type != null && name != null && price != null
                    && imageUrl != null && status != null) {
                int brandId = new SupplierModel().getIdSupplier(brand);
                int typeId = new ProductCategoryModel().getProductCategoryId(type);
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure want to delete " + name + "\nId: " + id);
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)) {
                        Product product = new Product(id, name, brand, brandId, type, typeId, Double.parseDouble(price), status, imageUrl, Double.parseDouble(importedPrice));
                        DBDelete(product);
                        resetTable();
                    } else return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancelBtn.setOnAction(event -> {

            // Get a reference to the cancel button's stage (window)
            addProductAddBtn.setDisable(false);
            addProductUpdatebtn.setDisable(true);
            productDeleteBtn.setDisable(true);
            tblvProduct.getSelectionModel().clearSelection();
            clearTextFields(dashboardProduct);
        });

        tblvProduct.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
                addProductBrandCb.setItems(listBrands);
                List<String> status = Arrays.asList("Available", "Unavailable");
                cbStatus.setItems(FXCollections.observableList(status));
                ObservableList<String> listCategory = FXCollections.observableArrayList(new ProductCategoryModel().getType());
                addProductTypeCb.setItems(listCategory);

                addProductAddBtn.setDisable(true);
                addProductUpdatebtn.setDisable(false);
                productDeleteBtn.setDisable(false);

                if (checkImageUrl(newValue.getImage())) {
//                    Image img = new Image(newValue.getImage());
//                    addproduct_imageview.setImage(img);
                    String currentPath = System.getProperty("user.dir");
                    addproductImageView.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                } else if (newValue.getImage() == null) {
                    String currentPath = System.getProperty("user.dir");
                    addproductImageView.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                }
                addProductTypeCb.getSelectionModel().select(newValue.getProductType());
                addProductBrandCb.getSelectionModel().select(newValue.getSupplierName());
                addProductNameTf.setText(newValue.getName());
                addProductSalespriceTf.setText(String.valueOf(newValue.getSalePrice()));
                cbStatus.getSelectionModel().select(newValue.getStatus());
                addProductId.setText(String.valueOf(newValue.getId()));
                addProductImportedpriceTf.setText(String.valueOf(newValue.getImportedPrice()));
            } else {
                addProductAddBtn.setDisable(false);
                addProductUpdatebtn.setDisable(true);
                productDeleteBtn.setDisable(true);
                tblvProduct.getSelectionModel().clearSelection();
                clearTextFields(dashboardProduct);
                int pageIndex = productPg.getCurrentPageIndex();
                int pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
                productPg.setPageCount(pageCount);
            }
        });
    }

    private boolean checkImageUrl (String url){
        String regex
                = "(\\S+(\\.(?i)(jpe?g|png|gif|bmp))$)";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        if (url == null) {
            return false;
        }

        Matcher m = p.matcher(url);

        return m.matches();
    }

    private void resetTable() {
        data.reset();
        setupTable();
        setUpPagination();
        addProductAddBtn.setDisable(false);
        addProductUpdatebtn.setDisable(true);
        productDeleteBtn.setDisable(true);
        clearTextFields(dashboardProduct);
        int pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
        productPg.setPageCount(pageCount);
    }

    public void setupTable(){
        products = data.products;
        productColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productColAmount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        productColPrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        productColBrand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        productColType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        productColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        FilteredList<Product> filteredList = new FilteredList<>(products,b -> true);
        searchTf.textProperty().addListener((observable,oldvalue, newvalue) -> {
            filteredList.setPredicate(product -> {
                if(newvalue == null || newvalue.trim().isBlank()){
                    return true;
                }
                String searchKeyWord = newvalue.toLowerCase();
                return product.getProductType().toLowerCase().contains(searchKeyWord) || product.getName().toLowerCase().contains(searchKeyWord)
                        || product.getSupplierName().toLowerCase().contains(searchKeyWord);
            });
            updatePagination(filteredList,newvalue);
        });

        updatePagination(filteredList,"");
    }
    public void setUpPagination(){
        int pageCount = (products.size() + itemPerPages - 1) / itemPerPages;
        productPg.setPageCount(pageCount);
        productPg.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateProductData(newValue.intValue());
        });
    }

    private void updateProductData(int pageIndex){
        int fromIndex = pageIndex * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, products.size());
        tblvProduct.setItems(FXCollections.observableArrayList(products.subList(fromIndex, toIndex)));
    }

    private void updatePagination(FilteredList<Product> filteredList, String newvalue) {
        // Calculate the total number of items in the filtered list
        int totalItems = filteredList.size();

        // Update the page count based on the total items
        int pageCount;
        if (newvalue == null || newvalue.trim().isEmpty()) {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        } else if (totalItems == 0) {
            pageCount = 1;
        } else {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        }

        productPg.setPageCount(pageCount);
        if (productPg.getCurrentPageIndex() >= pageCount) {
            productPg.setCurrentPageIndex(pageCount - 1);
        }

        // Calculate the indices for the current page
        int fromIndex = productPg.getCurrentPageIndex() * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, totalItems);

        // Create a new FilteredList that filters the entire 'products' list
        FilteredList<Product> updatedFilteredList = new FilteredList<>(products, b -> true);
        String searchKeyWord = newvalue.toLowerCase();
        updatedFilteredList.setPredicate(product -> {
            if (newvalue == null || newvalue.trim().isBlank()) {
                return true;
            }
            return product.getProductType().toLowerCase().contains(searchKeyWord)
                    || product.getName().toLowerCase().contains(searchKeyWord)
                    || product.getSupplierName().toLowerCase().contains(searchKeyWord);
        });

        // Sort the updated filtered list
        SortedList<Product> sortedList = new SortedList<>(updatedFilteredList);
        sortedList.comparatorProperty().bind(tblvProduct.comparatorProperty());

        // Set the data to display in the table based on the updated filtered list
        tblvProduct.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    static void DBAdd(Product product) {
        boolean check = new ProductModel().addProduct(product);
        System.out.println(check);

        if (check) {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Add product successfully");
            success.showAndWait();
        } else {
            Alert failed = new Alert(Alert.AlertType.INFORMATION);
            failed.setContentText("Something went wrong. Please try again");
            failed.showAndWait();
        }
    }

    private void DBUpdate(Product product) {
        boolean check = new ProductModel().updateProduct(product);
        System.out.println("Update Product: " + product.getName());
        System.out.println("Id: " + product.getId());
        System.out.println("Status: " + check);
        if (check) {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Update product successfully");
            success.showAndWait();
        } else {
            Alert failed = new Alert(Alert.AlertType.INFORMATION);
            failed.setContentText("Something went wrong. Please try again");
            failed.showAndWait();
        }
    }

    static void DBDelete(Product product) {
        boolean check = new ProductModel().deleteProduct(product);
        if (check) {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Update product successfully");
            success.showAndWait();
        } else {
            Alert failed = new Alert(Alert.AlertType.INFORMATION);
            failed.setContentText("Something went wrong. Please try again");
            failed.showAndWait();
        }
    }

    private void openModalWindow (String resource, String title) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }

    private void clearTextFields (Parent parent){
        if (parent == null) return;

        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.clear();
            } else if (node instanceof ComboBox) {
                ComboBox<String> comboBox = (ComboBox<String>) node;
                comboBox.getSelectionModel().clearSelection();
            } else if (node instanceof Parent) {
                clearTextFields((Parent) node); // Recursively clear TextFields in child nodes
            } else if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setImage(null);
            }
        }
    }

    @FXML
    public void addProductImportImage() {
        Image image = null;
        String currentPath = System.getProperty("user.dir");
        FileChooser open = new FileChooser();
        open.setTitle("Open image file");
        open.setInitialDirectory(new File(currentPath + "\\src\\main\\resources\\controller\\images"));
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(addProductScene.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString(), 150, 130, false, true);
            addproductImageView.setImage(image);
            imageUrl = image.getUrl();
        }
    }

    private boolean isNumeric (String str){
        return str.matches("\\d*"); // Check if the given string contains only digits
    }
}
