
package data_objects;

import business_objects.Warehouse;
import java.util.List;

public interface IWarehouseDAO {
    boolean addReceipt(Warehouse receipt);
    int getSize();
    List<Warehouse> getReceiptList(String productCode);
}
