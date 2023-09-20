
package data_objects;

import business_objects.Product;
import business_objects.Warehouse;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO implements IWarehouseDAO{
    private List<Warehouse> warehouseList;

    public WarehouseDAO() {
        warehouseList = new ArrayList<>();
    }

    public WarehouseDAO(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }

    @Override
    public boolean addReceipt(Warehouse receipt) {
        return warehouseList.add(receipt);
    }

    @Override
    public int getSize() {
        return warehouseList.size();
    }

    @Override
    public List<Warehouse> getReceiptList(String productCode) {
        List<Warehouse> tempList = new ArrayList<>();
        for(Warehouse warehouse: warehouseList){
            for(Product product: warehouse.getItems()){
                if(product.getProductCode().equalsIgnoreCase(productCode)){
                    tempList.add(warehouse);
                    break;
                }
            }
        }
        return tempList;
    } 

    @Override
    public boolean isProductExist(String productCode) {
        for(Warehouse warehouse: warehouseList){
            for(Product product: warehouse.getItems()){
                if(product.getProductCode().equalsIgnoreCase(productCode)){
                    return true;
                }
            }
        }
        return false;
    }
}
