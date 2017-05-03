package orderProcessor;
/**
 * @author Dorianna Leontiadou
 * @date 03/05/2017
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import orderProcessor.Product;

public class ProductOperations {
	private double adjustedPrice;

    private Product product;
    
    public ProductOperations(Product product) {
        this.product = product;
        this.adjustedPrice = 0.0;
    }

    
    public double adjustPrice() {
    	String str = "Price";
        String adjustmentMethod = product.getAdjustmentOperator().toLowerCase().concat(str);        
        try {
            Method method = this.getClass().getMethod(adjustmentMethod,null);
            method.invoke(this,null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return adjustedPrice;
    }


    public void addPrice() {
        this.adjustedPrice = this.product.getTotalPrice() + 
        					( this.product.getTotalQuantity() * this.product.getProductPrice() );
    }

    public void subtractPrice() {
        this.adjustedPrice = this.product.getTotalPrice() - 
        					(this.product.getTotalQuantity() * this.product.getProductPrice());
    }

    public void multiplyPrice() {
    	
        this.adjustedPrice = this.product.getTotalPrice() +
			                (this.product.getTotalPrice() * this.product.getProductPrice()) +
			                (this.product.getTotalQuantity() * this.product.getProductPrice());
    }

    // Value returned: Adjustment of type Add 10p performed on 10 oranges. Price changed from 0.10p to 0.20p 
    public String adjustmentReport(){
        String adjustmentReport = String.format(
                "Adjustment of type %s %.2fp performed on %d %s. Price changed from %.2fp to %.2fp",
                this.product.getAdjustmentOperator(),
                this.product.getProductPrice(),
                this.product.getTotalQuantity(),
                this.product.getProductType(),
                this.product.getTotalPrice(),
                this.adjustedPrice
        );
        return adjustmentReport;
    }
}
