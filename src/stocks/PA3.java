package stocks;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.*;

public class PA3 {

	private static Schedule schedule;
	class StockHolder{
		ArrayList<Stock> data;
	}
	private static StockHolder stocks;
	private static HashSet<String> tickers = new HashSet<String>();
	private static HashMap<String, Semaphore> StockBrokers;
     /**
      * Read Stock Json File inputed by user using GSON
      */
    private static void readStockFile() {
    	Scanner input = new Scanner(System.in);
    	System.out.print("What's the name of the company file? ");
		String fileName = input.nextLine();	
		Reader reader = null;
		Gson gson = new Gson();
		
		try {	
			if(fileName.equals("")) {
				throw new IOException();
			}
			reader = Files.newBufferedReader(Paths.get(fileName));
			stocks = gson.fromJson(reader, StockHolder.class);
			if(stocks == null || stocks.data.size() == 0)
				throw new JsonParseException("Empty file");
			// validate Json file
			for(Stock s: stocks.data) {
				s.validate();
				tickers.add(s.getTicker());			
			}
			reader.close();
			System.out.println();
		}
		catch(IOException e) {
			System.out.println(fileName + " could not be found.");
			readStockFile();
		}		
		catch(JsonSyntaxException e) {
			System.out.println("Data cannot be converted to the proper type");
			readStockFile();
		}
		catch(JsonParseException e) {
			e.printStackTrace();
			System.out.println("Error while parsing");
			readStockFile();
		}

    }

    /**
     * Read Stock Trades CSV File inputed by user
     */
    private static void readScheduleFile() {
    	
    	Scanner input = new Scanner(System.in);
    	schedule = new Schedule();
    	System.out.print("What's the name of the schedule file? ");
		String fileName = input.nextLine();
		String line = "";  
		String comma = ",";  
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))){	
			
			if(fileName.compareTo("")==0) {
				throw new IOException();	
			}
			
			while ((line = br.readLine()) != null)  { 
				String[] trade = line.split(comma);
				String company = trade[1];
				
//				for (String s:trade) {
//					System.out.println(s);
//				}
				
				int amount = Integer.parseInt(trade[2]);
				//weird things happening with 0 - comparing to 0 gives 65231
				int start = 0;
				//System.out.println(trade[0].compareToIgnoreCase("0"));
				if (trade[0].compareTo("0") != 65231) {
					start = Integer.parseInt(trade[0]);
				}
				//check if each trade is valid
				if(trade.length < 3) {
					throw new Exception("The file contains invalid trade that is missng parameter.");
				}
				if(start < 0) {
					throw new Exception("Invalid trade time for " + company);
				}
				if(!tickers.contains(company)) {
					throw new Exception("Invalid trade, no " + company + " in the csv.");
				}
				schedule.insertTask(new Schedule.Task(start, company, amount));
			}   
			
			br.close();
			System.out.println();
			
		}
		
		//exception with file input
		catch(IOException e){
			System.out.println(fileName + " could not be found.");
			readScheduleFile();
		}
		
		//exception from parseint stuff on strings
		catch(NumberFormatException e){
			e.printStackTrace();
			System.out.println("The file contains invalid start time or trading quantity");
			readScheduleFile();
		}
		
		//custom exceptions, can print messages stored earlier
		catch(Exception e){ 
			System.out.println(e.getMessage());
			readScheduleFile();
		}
    	
    }

    /**
     *Set up Semaphore for Stock Brokers
     */

    private static void initializeSemaphore(){
   
    	StockBrokers = new HashMap<String, Semaphore>();
    	for(Stock s: stocks.data) {
    		StockBrokers.put(s.getTicker(), new Semaphore(s.getStockBrokers()));
    	}
    	
    }

    private static void executeTrades() throws InterruptedException {
    	
    	System.out.println("Starting execution of program...");
    	HashMap<Integer, ArrayList<Schedule.Task>> sorted_schedule = schedule.sortTasks();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(schedule.getTasks().size());
        
    	while(!schedule.isEmpty()){	
    		Schedule.Task temp = schedule.removeTask();		
    		ArrayList<Schedule.Task> s = sorted_schedule.get(temp.getStartTime());
    		
     		for(Schedule.Task t: s){
	    		Trade trade = new Trade(t, StockBrokers.get(t.getCompany()));	
	    		scheduler.schedule(trade, t.getStartTime(),  TimeUnit.SECONDS);		
    		}	
     		
    		for(int i = 0; i < s.size()-1; i++){
    			schedule.removeTask();		
    		}
    	}
    	scheduler.shutdown();
    	
    	while (!scheduler.isTerminated()){
    		Thread.yield();
    	}
    	
    	System.out.println("All trades completed!");
    	
    }
    
    public static void parse(){
    	readStockFile();
    	readScheduleFile();
    }
    
    public static void main(String[] args) throws InterruptedException {	
    	parse();
    	initializeSemaphore();
    	executeTrades();
    }
}
