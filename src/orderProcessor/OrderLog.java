package orderProcessor;
/**
 * @author Dorianna Leontiadou
 * @date 03/05/2017
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


public class OrderLog {
	HashMap products = new HashMap();
	Vector adjustmentLog;
	Vector log;
	
	public OrderLog() {
		this.adjustmentLog = new Vector();
		this.log = new Vector();		
	}
	
	public Product getProduct(String type) {
		if (products.containsKey(type)) 
			return (Product)products.get(type);
		else {	
			Product prd = new Product(type);
			return prd;			
		}	
    }
	
	public void updateProduct(Product prd) {
		products.put(prd.getProductType(), prd);
	}
	
	public void setAdjustmentLog(String log_txt) {
		adjustmentLog.add(log_txt);
	}
	public Vector getAdjustmentLog() {
		return adjustmentLog;
	}
	
	public void setLog(String log_txt) {
		log.add(log_txt);
	}
	public Vector getLog() {
		return log;
	}
	
	
	public int report(boolean processRest) {
		int result = 0;
		double totalSales = 0;
		try {
			if (!processRest) {
				// After every 10th message received the application should log a report 
				// detailing the number of sales of each product and their total value.
				// The problem is when for ex we have 19 orders 
				// we log the first 10 and what happens to the rest 9?
				if (log.size()!=0 && (log.size() % 10) == 0) {
					System.out.println("10 sales appended to log");
		            System.out.println("****************** Log Report **********************");
		            System.out.println(String.format("%-18s%-11s%-13s","product", "quantity", "total price"));
		            Iterator it = products.entrySet().iterator();
		            while (it.hasNext()) {
		                Map.Entry mapPrd = (Map.Entry)it.next();
		                Product prd = (Product)mapPrd.getValue();
		                formatReportLine((String)mapPrd.getKey(), prd);
		                totalSales += prd.getTotalPrice();
		                it.remove(); 
		            }
					
		            System.out.println("Total sales value:" + String.format("%-11.2f",totalSales));
		            System.out.println("*************** End of Log Report ******************\n");
				}
				
				if (log.size()!=0  && (log.size() % 50) == 0) {
					System.out.println("Application reached 50 messages and will stop accepting new messages.\n");
					for (int i=0; i<adjustmentLog.size();i++) {
						System.out.println(adjustmentLog.get(i));
					}
				    result = 1;		    
				}
			} else {
				System.out.println("Final (less than 10) sales appended to log");
	            System.out.println("****************** Log Report **********************");
	            System.out.println(String.format("%-18s%-11s%-13s","product", "quantity", "total price"));
	            Iterator it = products.entrySet().iterator();
	            while (it.hasNext()) {
	                Map.Entry mapPrd = (Map.Entry)it.next();
	                Product prd = (Product)mapPrd.getValue();
	                formatReportLine((String)mapPrd.getKey(), prd);
	                totalSales += prd.getTotalPrice();
	                it.remove(); 
	            }
				
	            System.out.println("Total sales value:" + String.format("%-11.2f",totalSales));
	            System.out.println("*************** End of Log Report ******************\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void formatReportLine(String type, Product product) {
        String lineItem = String.format("%-18s%-11d%-11.2f", product.getProductType(), product.getTotalQuantity(), product.getTotalPrice());        
        System.out.println(lineItem);
    }

}
