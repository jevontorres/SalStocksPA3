package stocks;
import java.util.concurrent.*;

public class Trade extends Thread {
	
	Schedule.Task task;
	Semaphore sem;
	public Trade(Schedule.Task t, Semaphore sem) {
		this.task = t;
		this.sem = sem;
    }

	/**
	 * Trading function using locks
	 */
	public void run() {
		int amount = Math.abs(task.getAmount());
		String company = task.getCompany();
		try {	
			sem.acquire();
			if(task.getAmount() < 0){
				System.out.println("[" + Utility.getZeroTimestamp()+ "]" + " Starting sale of " + amount + " stocks of " + company);
			}
			else {
				System.out.println("[" + Utility.getZeroTimestamp()+ "]" + " Starting purchase of " + amount + " stocks of " + company);
			}
			Thread.sleep(1000);				
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}	
		
		if(task.getAmount() < 0){
			System.out.println("[" + Utility.getZeroTimestamp()+ "]" + " Finishing sale of " + amount + " stocks of " + company);
		}
		else{
			System.out.println("[" + Utility.getZeroTimestamp() +"]" + " Finishing purchase of " + amount + " stocks of " + company);
		}
		
		sem.release();
	}
}
