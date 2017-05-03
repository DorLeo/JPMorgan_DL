package orderProcessor;
/**
 * @author Dorianna Leontiadou
 * @date 03/05/2017
 */

public class Order {

    public OrderLog log;
    private ProductOperations prodOp;
    private Product product;

    public Order() {
        log = new OrderLog();
    }

   
    public boolean processOrder(String orderLine) {
    	
        OrderProcessor orderProcessor = new OrderProcessor(orderLine);
        String productType = orderProcessor.getProductType(); // remember it is in plural
        if (productType.isEmpty()) {
            return false;
        }
        this.product = log.getProduct(productType);
        this.prodOp = new ProductOperations(product);
        this.product.setProductQuantity(orderProcessor.getProductQuantity());
        this.product.setTotalQuantity(orderProcessor.getProductQuantity());
        this.product.setProductPrice(orderProcessor.getProductPrice());
        this.product.setAdjustmentOperator(orderProcessor.getOperatorType());

        setProductTotalPrice();

        log.setLog(orderLine);

        log.updateProduct(product);
        return true;
    }

   
    private void setProductTotalPrice() {
        double adjustedPrice;
        double productValue;
        
        if (!product.getAdjustmentOperator().isEmpty()) {
        	// it's an adjustment
            adjustedPrice = prodOp.adjustPrice();
            log.setAdjustmentLog(prodOp.adjustmentReport());
            product.setTotalPrice(adjustedPrice);
        } else {
            productValue = product.calculatePrice(product.getProductQuantity(), product.getProductPrice());
            product.appendTotalPrice(productValue);
        }
    }
}
