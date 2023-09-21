
package action_service;

import business_objects.Product;
import business_objects.Trade;

public interface IService {
    // Product repository
    void showAllProduct();
    Product addProduct();
    void updateProduct();
    void deleteProduct();
    // Warehouse repository
    void addReceipt(Trade tradeType);
    // Report repository
    void showExpiredProducts();
    void showSellingProducts();
    void showOutOfStockProducts();
    void showReceipt();
    // File handling repository
    boolean loadProductsFromFile();
    boolean saveProductsToFile();
    boolean loadWarehouseFromFile();
    boolean saveWarehouseToFile();
}