package stocks;
import java.util.*;

public class Schedule {

    /** 
     * Stock Trades Schedule 
     * Keep the track of tasks
    */
	
	private ArrayList<Task> tasks;
	//start time to task
	private HashMap<Integer, ArrayList<Task>> sortedTasks;
	
	
    public Schedule() {
        tasks = new ArrayList<Task>(); 
        sortedTasks = new HashMap<Integer, ArrayList<Task>>();
    }
    
    //Insert a task to the schedule
    public void insertTask(Task t) {
    	tasks.add(t);
    }
    
    //pop task
    public Task removeTask() {
    	if(!tasks.isEmpty())
    		 return tasks.remove(0);
    	return null;
    }
    
    //check if schedule is empty
    public boolean isEmpty() {
    	return tasks.isEmpty();
    }
    
    //return tasks
    public ArrayList<Task> getTasks(){
    	return this.tasks;
    }
    
    //sort schedule
    public HashMap<Integer, ArrayList<Task>> sortTasks(){
    	
    	for(Task t: tasks) {
    		if(sortedTasks.containsKey(t.getStartTime())) {
    			 ArrayList<Task> temp = sortedTasks.get(t.getStartTime());
    			 temp.add(t);  			 
    			 sortedTasks.put(t.getStartTime(), temp);
    		}
    		else {
    			 ArrayList<Task> temp = new ArrayList<Task>();
    			 temp.add(t);  	
    			 sortedTasks.put(t.getStartTime(), temp);		 
    		}
    	}
    	
    	return this.sortedTasks;
    }
    

    /**
     * Inner class to store task object
     */

    public static class Task {
    	private int start_time;
    	private String company;
    	private int amount;
    	
        public Task(int time, String ticker, int amount){
        		this.start_time = time;
        		this.company = ticker;
        		this.amount = amount;
        }
        
        public int getStartTime() {
        	return this.start_time;
        }
        public String getCompany(){
        	return this.company;
        }
        
        public int getAmount(){
        	return this.amount;
        }
    }
    
}
