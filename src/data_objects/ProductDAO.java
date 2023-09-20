
package data_objects;

import business_objects.Product;
import business_objects.ProductType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO implements IProductDAO {
    private final String PRODUCT_FILEPATH = "..\\..\\product.dat";
    private List<Product> productList;

    public ProductDAO() {
        productList = new ArrayList<>();
    }

    public ProductDAO(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productList;
    }

    @Override
    public Product getProduct(String code) {
        for(Product product: productList){
            if(product.getProductCode().equalsIgnoreCase(code)){
                return product;
            }
        }
        return null;
    }

    @Override
    public boolean addProduct(Product product) {
        return productList.add(product);
    }

    @Override
    public void updateProduct(Product product) {
        int index = productList.indexOf(getProduct(product.getProductCode()));
        productList.set(index, product);
    }

    @Override
    public boolean deleteProduct(Product product) {
        return productList.remove(product);
    }

    @Override
    public List<Product> getExpiredProducts() {
        List<Product> expiredProducts = new ArrayList<>();
        Date now = new Date();
        for(Product product: productList){
            if(now.after(product.getExpirationDate())){
                expiredProducts.add(product);
            }
        }
        return expiredProducts;
    }

    @Override
    public List<Product> getSellingProducts() {
        List<Product> sellingProducts = new ArrayList<>();
        Date now = new Date();
        for(Product product: productList){
            if(product.getQuantity() > 0 && now.before(product.getExpirationDate())){
                sellingProducts.add(product);
            }
        }
        return sellingProducts;
    }

    @Override
    public List<Product> getOutOfStockProducts() {
        List<Product> outOfStockProducts = new ArrayList<>();
        for(Product product: productList){
            if(product.getQuantity() <= 3){
                outOfStockProducts.add(product);
            }
        }
        return outOfStockProducts;
    }

    @Override
    public boolean isProductCodeExist(String code) {
        for(Product item: productList){
            if(code.equalsIgnoreCase(item.getProductCode())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean loadFromFile() {
        File file = new File(PRODUCT_FILEPATH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(!file.exists()) {
            return false;
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = "";
                while((line = reader.readLine()) != null) {
                        String[] rows = line.split(",");
                        String productCode = rows[0].trim();
                        String productName = rows[1].trim();
                        int quantity = Integer.parseInt(rows[2].trim());
                        double price = Double.parseDouble(rows[3].trim());
                        ProductType type = ProductType.valueOf(rows[4].trim());
                        Date manufacturingDate = dateFormat.parse(rows[5].trim());
                        Date expiredDate = dateFormat.parse(rows[6].trim());
                        
                        Product productFromFile = new Product(productCode, productName, quantity, price, type, manufacturingDate, expiredDate);
                        productList.add(productFromFile);
                    }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        return true;
    }

    @Override
    public boolean saveToFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        try {
            PrintWriter printWriter = new PrintWriter(PRODUCT_FILEPATH);
            for(Product product: productList) {
                printWriter.println(product.getProductCode() + ", " + product.getName() + ", " + product.getQuantity()+ ", " + product.getPrice()+ ", " + product.getType() + ", " + dateFormat.format(product.getManufacturingDate())+ ", " + dateFormat.format(product.getExpirationDate()));
            }
            printWriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
}
