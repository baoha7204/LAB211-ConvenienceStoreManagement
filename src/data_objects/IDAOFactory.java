 
package data_objects;

import utils.IValidation;

public interface IDAOFactory {
    IProductDAO productDAO();
    IValidation validator();
    IWarehouseDAO warehouseDAO();
}
