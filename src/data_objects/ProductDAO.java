
package data_objects;

import business_objects.Product;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDAO implements IProductDAO {
    
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
    
}
