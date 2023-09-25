package business_objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Product implements Serializable{
    private String productCode;
    private String name;
    private int quantity;
    private double price;
    private ProductType type;
    private Date manufacturingDate, expirationDate;

    public Product() {
    }

    public Product(String productCode, String name, int quantity, double price, ProductType type, Date manufacturingDate, Date expirationDate) {
        this.productCode = productCode;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public ProductType getType() {
        return type;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public boolean setName(String name) {
        if(!name.isEmpty()){
            this.name = name;
            return true;
        }
        return false;
    }

    public boolean setQuantity(int quantity) {
        if(quantity >= 0){
            this.quantity = quantity;
            return true;
        }
        return false;
    }

    public boolean setPrice(double price) {
        if(price >= 0){
            this.price = price;
            return true;
        }
        return false;
    }

    public boolean setType(ProductType type) {
        if(type != null){
            this.type = type;
            return true;
        }
        return false;
    }

    public boolean setManufacturingDate(Date manufacturingDate) {
        if(manufacturingDate != null){
            this.manufacturingDate = manufacturingDate;
            return true;
        }
        return false;
    }

    public boolean setExpirationDate(Date expirationDate) {
        if(expirationDate != null){
            this.expirationDate = expirationDate;
            return true;
        }
        return false;
    }
    
    public static Comparator compareQuantity = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Product product1 = (Product) o1;
            Product product2 = (Product) o2;
            return product1.getQuantity() - product2.getQuantity();
        }
    };

    @Override
    public String toString() {
        String pattern = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(pattern);
        return "{" + "productCode=" + productCode + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", type=" + type + ", manufacturingDate=" + sdf.format(manufacturingDate) + ", expirationDate=" + sdf.format(expirationDate) + "}";
    }
    
    public String toReportString() {
        String pattern = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(pattern);
        return "{" + "productCode=" + productCode + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", manufacturingDate=" + sdf.format(manufacturingDate) + ", expirationDate=" + sdf.format(expirationDate) + "}";
    }
}
