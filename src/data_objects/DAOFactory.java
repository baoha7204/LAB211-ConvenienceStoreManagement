
package data_objects;

import utils.IValidation;
import utils.Validation;

public class DAOFactory implements IDAOFactory {

    @Override
    public IProductDAO productDAO() {
        return new ProductDAO();
    }

    @Override
    public IValidation validator() {
        return new Validation();
    }

    @Override
    public IWarehouseDAO warehouseDAO() {
        return new WarehouseDAO();
    }
    
}
