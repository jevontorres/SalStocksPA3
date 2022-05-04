package stocks;
import com.google.gson.JsonParseException;

public class Stock {
    /**
	 * Here: all the needed class members and their getters and setters
	 */
	private String ticker;
	private Integer stockBrokers;
	
	//only needed for file validity check
	private String name;
	private String startDate;
	private String description;
	private String exchangeCode;
	
    public Stock(){}
    
    public String getTicker(){
    	return this.ticker;
    }
    
    public int getStockBrokers(){
    	return this.stockBrokers;
    }
    
    //validate task
    public void validate(){  
    	//check for missing parameters for each company 
    	if(ticker == null || stockBrokers == null || name == null || startDate == null || description == null|| exchangeCode == null)
    		throw new JsonParseException("Missing data parameters");
    	
    	//check positive stockbrokers
    	if(stockBrokers <= 0)
    		throw new JsonParseException("Invalid number of stockBrokers");	
    }
}

