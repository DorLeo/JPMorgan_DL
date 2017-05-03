package servers;
/**
 * @author Dorianna Leontiadou
 * @date 03/05/2017
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

import orderProcessor.Order;

public class SingleThreadedServer implements Runnable{

	public static SingleThreadedServer server;
	protected int          		serverPort   = 8080;
    protected ServerSocket 		serverSocket = null;
    protected boolean      		isStopped    = false;
    protected Thread       		runningThread= null;
    protected boolean 			forceToStop  = false;
    
    private static String urlParameters		 = "";    
	private static String url 				 = "http://localhost:" ;
    	

    public SingleThreadedServer(int port){
        this.serverPort = port;
        this.url += port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        
        while(! isStopped() && !this.forceToStop){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            try {
                processClientRequest(clientSocket);
                
            } catch (IOException e) {
                
            }
        }
        
        System.out.println("Server Stopped.");
    }

    private void processClientRequest(Socket clientSocket) throws IOException {
        InputStream  input  = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        String action = "";
        String result = "";
        String filename = "";
        
        try {
        
	        BufferedReader br = new BufferedReader(new InputStreamReader(input));
	        // here we have to find query string parameters
	        // if no parameters are passed, thread goes back to sleep
	        while(true) {
	            String s = br.readLine();
	            if(s == null || s.trim().length() == 0) {
	            	result = "No input found";
	                break;
	            }
	            if (s.contains("POST") || s.contains("GET")) {
	            	// in case we receive a request from browser
	            	String[] postparams = s.split("\\s");
	            	action = postparams[0].toUpperCase();
	            	
	            	String[] queryString = postparams[1].substring(1, postparams[1].length()).split("&");
	            	HashMap params = new HashMap();
	            	for (String str : queryString) {
	            		int idx = str.indexOf("=");
	            		params.put(str.substring(0, idx).toLowerCase(), str.substring(idx+1));
	            	}
	            	
	            	filename = params.get("folder") + "/" + params.get("file");
	            	break;
	            }
	            
	        }
	        input.close();
	        
	        if (!filename.equals("")) {
	        	int r = this.processFile(filename);
	        	switch (r) {
	        	case 0:
	        		result = "Order processed successfully";
	        		this.forceToStop = true;
	        		break;
	        	case 1: 
	        		//After 50 messages your application should log that it is pausing, stop accepting new messages
	        		this.forceToStop = true;
	        		break;
	        	case 99:
	        		result = "An error occured" ;
	        		break;
	        	}	
	        }
	        System.out.println(result); 
	        
		        
	        
        } catch (Exception e) {
        	e.printStackTrace() ;
        }
        
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
    
    private void sendPost(String url) {
    	try {
    		
			URL obj = new URL(url);
    		
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	
			// Send post request
			con.setDoOutput(true);
			
	
			if (server.isStopped())
				return;
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	
			//print result
			System.out.println(response.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private int processFile(String filename) {
    	// if result = 0  --> success
    	// if result = 1  --> stop server
    	// if result = 99 --> error 
    	int result = 0;
    	Order order = new Order();
    	int counter = 0; // to check if order has less than 10 order lines
    	try {
    		String line;
			BufferedReader inputFile = new BufferedReader(new FileReader(filename));
            while((line = inputFile.readLine()) != null) {
            	counter ++;
                order.processOrder(line);
                int returnValue = order.log.report(false);
                if (returnValue == 1) {
                	result = 1;
                	break;
                }
            }
            // We found end of file and we have logged everything so far
            // but we still have to log remaining order lines -- if they are less than 50
            if (counter<10 || (counter %10 >0) && (counter <50)) {
            	int returnValue = order.log.report(true);
               result = 1;
            }
		} catch (Exception e) {
			result = 99;
			e.printStackTrace();
		}
    	return result;
    }

    	
    public static void main(String args[]) {
    	// parameters should be of type --> "/folder=input&file=input.txt"
    	// there are two input files to be tested
    	// - input/input.txt
    	// - input/input2.txt
    	urlParameters = "/folder=input&file=input.txt";
    	//urlParameters = "/folder=input&file=input2.txt";
    	
    	server = new SingleThreadedServer(9000);
    	
    	System.out.println("Starting server");
    	new Thread(server).start();
    	
    	if (!server.isStopped)
    		server.sendPost(url+urlParameters);
    	try {
    	    Thread.sleep(10 * 1000);
    		
    	} catch (InterruptedException e) {
    	    e.printStackTrace();  
    	}
    	
    }
}