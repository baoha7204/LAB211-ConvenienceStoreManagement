
package data_objects;

import business_objects.Product;
import business_objects.Warehouse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO implements IWarehouseDAO{
    private final String WAREHOUSE_FILEPATH = "..\\..\\warehouse.dat";
    private List<Warehouse> warehouseList;

    public WarehouseDAO() {
        warehouseList = new ArrayList<>();
    }

    public WarehouseDAO(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }

    private void setWarehouseList(List<Warehouse> warehouseList) {
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

    @Override
    public boolean loadFromFile() {
        File file = new File(WAREHOUSE_FILEPATH);
        try{
            FileInputStream readData = new FileInputStream(file);
            ObjectInputStream readStream = new ObjectInputStream(readData);
            List<Warehouse> warehouseTempList = (ArrayList<Warehouse>) readStream.readObject();
            setWarehouseList(warehouseTempList);
            for(Warehouse warehouse: warehouseList){
                System.out.println(warehouse);
            }
            readStream.close();
        }catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean saveToFile() {
        File file = new File(WAREHOUSE_FILEPATH);
        try{
            FileOutputStream writeData = new FileOutputStream(file);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);
            writeStream.writeObject(warehouseList);
            writeStream.flush();
            writeStream.close();

        }catch (IOException e) {
            return false;
        }
        return true;
    }
}
