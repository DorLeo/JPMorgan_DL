package orderProcessor;
/**
 * @author Dorianna Leontiadou
 * @date 03/05/2017
 */

/** 
 *      A product class contains the product details:
 *      - type
 *      - price
 *      - quantity
 *      - adjustment operation for single product (last performed adjustment Add, Subtract and Multiply)
 *      - total price value 
 *      - total quantity
 */
public class Product {

    private double productPrice;
    private int productQuantity;
    private String adjustmentOperator;
    private String productType;
    private int totalQuantity;
    private double totalPrice;

    
    public Product(String type) {
        this.totalPrice = 0.0;
        this.totalQuantity = 0;
        this.productType = type;
        this.adjustmentOperator = null;
    }
    
    
    public String getProductType() {
        return this.productType;
    }
    public void setProductType(String type) {
        this.productType = type;
    }    
    
    public int getProductQuantity() { 
    	return productQuantity; 
    }
    public void setProductQuantity(int productQuantity) { 
    	this.productQuantity = productQuantity; 
    }    
    
    public void setTotalQuantity(int quantity){
        this.totalQuantity += quantity;
    }
    public int getTotalQuantity() {
        return this.totalQuantity;
    }
    
    public double getProductPrice() { 
    	return productPrice; 
    }
    public void setProductPrice(double productPrice) { 
    	this.productPrice = productPrice; 
    }    
    
    public double calculatePrice(int productQuantity, double productPrice){
        return productQuantity * productPrice;
    }
    
    public void setTotalPrice(double totalPrice) { 
    	this.totalPrice = totalPrice;  
    }
    
    public void appendTotalPrice(double productPrice) {
        this.totalPrice += productPrice;
    }
    
    public double getTotalPrice() { 
    	return this.totalPrice; 
    }

    public String getAdjustmentOperator() { 
    	return adjustmentOperator; 
    }
    public void setAdjustmentOperator(String adjustmentOperator) { 
    	this.adjustmentOperator = adjustmentOperator; 
    }

}
