
package data_objects;

import business_objects.Product;
import java.util.List;

public interface IProductDAO {
    List<Product> getAllProducts();
    Product getProduct(String code);
    boolean isProductCodeExist(String code);
    boolean addProduct(Product product);
    void updateProduct(Product product);
    boolean deleteProduct(Product product);
    List<Product> getExpiredProducts();
    List<Product> getSellingProducts();
    List<Product> getOutOfStockProducts();
    boolean loadFromFile();
    boolean saveToFile();
}
