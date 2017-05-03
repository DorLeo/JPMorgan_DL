package orderProcessor;
/**
 * @author Dorianna Leontiadou
 * @date 03/05/2017
 */

public class OrderProcessor {
	
	private String productType;
    private double productPrice;
    private int productQuantity;
    private String operatorType;

  
    public OrderProcessor(String message) {
        this.productType = "";
        this.productPrice = 0.0;
        this.productQuantity = 0;
        this.operatorType = "";
        parseOrderLine(message);
    }    

    private boolean parseOrderLine(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }
        String[] messageArray = message.trim().split("\\s+");
        String firstWord = messageArray[0];
        
        if (firstWord.matches("Add|Subtract|Multiply")) {
        	//ex. Add 20p apples
        	if(messageArray.length > 3 || messageArray.length < 3) return false;
            operatorType = messageArray[0];
            productPrice = parsePrice(messageArray[1]);
            productType = parseType(messageArray[2]);
            productQuantity = 0;
            return true;
        } else if (firstWord.matches("^\\d+")) {
        	 //ex. 20 sales of oranges at 10p each
        	 if(messageArray.length > 7 || messageArray.length < 7) return false;
        	 productQuantity = Integer.parseInt(messageArray[0]);        	 
             productType = parseType(messageArray[3]);
             productPrice = parsePrice(messageArray[5]);             
             return true;
        } else if (messageArray.length == 3 && messageArray[1].contains("at")) {
        	//ex. apple at 10p
        	if(messageArray.length > 3 || messageArray.length < 3) return false;
            productType = parseType(messageArray[0]);
            productPrice = parsePrice(messageArray[2]);
            productQuantity = 1; 
            return true;
        } else {
            System.out.println("The message is of the wrong format");
        }
        return true;
    }
    
    public double parsePrice(String p) {
        double price = Double.parseDouble(p.replace("p", ""));
        if (!p.contains(".")) {
            price = Double.valueOf(Double.valueOf(price) / Double.valueOf("100"));
        }
        return price;
    }
    
    public String parseType(String t) {
    	// we have to deal with the case the product is in plural 
    	// for example we may have orange or oranges, strawberry or strawberries
    	// it is easier to keep a report of the products in plural than in singular
    	// so when we find a product in singlular we change it in plural
    	 String parsedType = "";
         String prefix = t.substring(0, t.length() - 1);
         
         if (t.endsWith("o")) {
             parsedType = String.format("%soes", prefix);
         } else if (t.endsWith("y")) {
             parsedType = String.format("%sies", prefix);
         } else if (t.endsWith("h")) {
             parsedType = String.format("%shes", prefix);
         } else if (!t.endsWith("s")) {
             parsedType = String.format("%ss", t);
         } else {
             parsedType = String.format("%s", t);
         }
         return parsedType.toLowerCase();    	
    }
    
    public String getProductType() {
        return productType;
    }    
    public double getProductPrice() {
        return productPrice;
    }
    public String getOperatorType() {
        return operatorType;
    }
    public int getProductQuantity() {
        return productQuantity;
    }

}
