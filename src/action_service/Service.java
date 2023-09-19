package action_service;

import data_objects.DAOFactory;
import data_objects.IProductDAO;
import data_objects.IDAOFactory;
import business_objects.Product;
import business_objects.ProductType;
import business_objects.Trade;
import business_objects.Warehouse;
import data_objects.IWarehouseDAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import utils.IValidation;
import utils.Status;

public class Service implements IService {

    static final IDAOFactory factory = new DAOFactory();
    static final IProductDAO productDAO = factory.productDAO();
    static final IWarehouseDAO warehouseDAO = factory.warehouseDAO();
    static final IValidation validator = factory.validator();

    @Override
    public void showAllProduct() {
        System.out.println("-----List of all products in store-----");
        for (Product product : productDAO.getAllProducts()) {
            System.out.println(product);
        }
    }

    @Override
    public Product addProduct() {
        String productCode = "";
        while (true) {
            productCode = validator.checkString("Input product code:", Status.NORMAL);
            if (!productDAO.isProductCodeExist(productCode)) {
                break;
            } else {
                System.err.println("Code is dulplicated! Please enter again.");
            }
        }
        String productName = validator.checkString("Input product name:", Status.NORMAL);
        int quantity = validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.NORMAL);
        double price = validator.checkDouble("Input price:", 0, Double.MAX_VALUE, Status.NORMAL);
        ProductType productType = validator.checkProductType("Input product type (DAILY/LONG):", Status.NORMAL);
        Date manufacturingDate = validator.checkBeforeDate("Input manufacturing date (dd/MM/yyyy):", Status.NORMAL);
        Date expirationDate = validator.checkAfterDate("Input expiration date (dd/MM/yyyy):", manufacturingDate, Status.NORMAL);
        Product addedProduct = new Product(productCode, productName, quantity, price, productType, manufacturingDate, expirationDate);
        if (productDAO.addProduct(addedProduct)) {
            System.out.println("Successfully add product!");
        }
        return addedProduct;
    }

    @Override
    public void updateProduct() {
        String pattern = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(pattern);
        String productCode = "";
        while (true) {
            productCode = validator.checkString("Input product code:", Status.NORMAL);
            if (productDAO.isProductCodeExist(productCode)) {
                break;
            } else {
                System.err.println("Product does not exist! Please enter again.");
            }
        }
        Product updatedProduct = productDAO.getProduct(productCode);

        String productName = validator.checkString("Input product name:", Status.UPDATE);
        if (updatedProduct.setName(productName)) {
            System.out.println("Successfully changed to: " + productName);
        }

        int quantity = validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.UPDATE);
        if (updatedProduct.setQuantity(quantity)) {
            System.out.println("Successfully changed to: " + quantity);
        }

        double price = validator.checkDouble("Input price:", 0, Double.MAX_VALUE, Status.UPDATE);
        if (updatedProduct.setPrice(price)) {
            System.out.println("Successfully changed to: " + price);
        }

        ProductType productType = validator.checkProductType("Input product type (DAILY/LONG):", Status.UPDATE);
        if (updatedProduct.setType(productType)) {
            System.out.println("Successfully changed to: " + productType);
        }

        Date manufacturingDate = validator.checkBeforeDate("Input manufacturing date (dd/MM/yyyy):", Status.UPDATE);
        if (updatedProduct.setManufacturingDate(manufacturingDate)) {
            System.out.println("Successfully changed to: " + sdf.format(manufacturingDate));
        }

        Date expirationDate = validator.checkAfterDate("Input expiration date (dd/MM/yyyy):", updatedProduct.getManufacturingDate(), Status.UPDATE);
        if (updatedProduct.setExpirationDate(expirationDate)) {
            System.out.println("Successfully changed to: " + sdf.format(expirationDate));
        }
    }

    @Override
    public void deleteProduct() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addReceipt(Trade tradeType) {
        List<Product> items = new ArrayList<>();
        do {
            Product importProduct;
            String productCode = validator.checkString("Input product code:", Status.NORMAL);
            // Case 1: non-existed product code
            if (!productDAO.isProductCodeExist(productCode)) {
                if(tradeType == Trade.EXPORT){
                    System.err.println("Product does not exist! Please enter again.");
                    continue;
                } else{
                    String productName = validator.checkString("Input product name:", Status.NORMAL);
                    int quantity = validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.NORMAL);
                    double price = validator.checkDouble("Input price:", 0, Double.MAX_VALUE, Status.NORMAL);
                    ProductType productType = validator.checkProductType("Input product type (DAILY/LONG):", Status.NORMAL);
                    Date manufacturingDate = validator.checkBeforeDate("Input manufacturing date (dd/MM/yyyy):", Status.NORMAL);
                    Date expirationDate = validator.checkAfterDate("Input expiration date (dd/MM/yyyy):", manufacturingDate, Status.NORMAL);
                    importProduct = new Product(productCode, productName, quantity, price, productType, manufacturingDate, expirationDate);
                    if (productDAO.addProduct(importProduct)) {
                        System.out.println("Successfully add product!");
                    }
                }
            } 
            // Case 2: existed product code => check quantity if EXPORT and update quantity
            else {
                Product addedProduct = productDAO.getProduct(productCode);
                int newQuantity;
                if(tradeType == Trade.EXPORT){
                    while(true){
                        newQuantity = validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.NORMAL);
                        if(addedProduct.getQuantity() - newQuantity >= 0){
                            break;
                        } else{
                            System.err.println("Not enough quantity to export! Please enter again.");
                        }
                    }
                } else{
                    newQuantity = validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.NORMAL);
                }
                String productName = addedProduct.getName();
                double price = addedProduct.getPrice();
                ProductType productType = addedProduct.getType();
                Date manufacturingDate = addedProduct.getManufacturingDate();
                Date expirationDate = addedProduct.getExpirationDate();
                importProduct = new Product(productCode, productName, newQuantity, price, productType, manufacturingDate, expirationDate);
                if(tradeType == Trade.EXPORT){
                    addedProduct.setQuantity(addedProduct.getQuantity() - newQuantity);
                } else{
                    addedProduct.setQuantity(addedProduct.getQuantity() + newQuantity);
                }
            }
            items.add(importProduct);
        } while (validator.checkYesOrNo("Continue to add product (Y/N)?"));
        int code = 1000001 + warehouseDAO.getSize();
        Date now = new Date();
        Warehouse warehouse = new Warehouse(code, tradeType, now, items);
        if (warehouseDAO.addReceipt(warehouse)) {
            System.out.println("Successfully add " + tradeType + " receipt with information:");
            System.out.println(warehouse);
        }
    }

    @Override
    public void showExpiredProducts() {
        System.out.println("-----Products that have expired-----");
        for(Product expProduct: productDAO.getExpiredProducts()){
            System.out.println(expProduct.toReportString());
        }
    }

    @Override
    public void showSellingProducts() {
        System.out.println("-----Products that the store is selling-----");
        for(Product sellingProduct: productDAO.getSellingProducts()){
            System.out.println(sellingProduct.toReportString());
        }
    }

    @Override
    public void showOutOfStockProducts() {
        System.out.println("-----Products that are running out of stock-----");
        List<Product> list = productDAO.getOutOfStockProducts();
        Collections.sort(list, Product.compareQuantity);
        for(Product outOfStockProduct: list){
            System.out.println(outOfStockProduct.toReportString());
        }
    }

    @Override
    public void showReceipt() {
        String productCode = "";
        while (true) {
            productCode = validator.checkString("Input product code:", Status.NORMAL);
            if (productDAO.isProductCodeExist(productCode)) {
                break;
            } else {
                System.err.println("Product does not exist! Please enter again.");
            }
        }
        for(Warehouse warehouse: warehouseDAO.getReceiptList(productCode)){
            String listItems = "";
            for(Product product: warehouse.getItems()){
                if(product.getProductCode().equalsIgnoreCase(productCode)){
                    listItems += product.toString() + "\n";
                }
            }
            System.out.println(warehouse.toReportString()+ "," + "\n-----List of products-----\n" + listItems + "}");
        }
    }

}