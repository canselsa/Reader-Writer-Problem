package sync;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Test {
	public static void main(String [] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ReadWriteLock RW = new ReadWriteLock();
	
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
	}
}

class ReadWriteLock{
	private Semaphore S; 
	private Semaphore RC; 
	private int total_reader; 
  
    public ReadWriteLock() {
    total_reader = 0; 
    RC = new Semaphore(1);
    S = new Semaphore(1);
    }
    
	public void readLock() {
	     try{
	    	 RC.acquire(); 
	         }
	             catch (InterruptedException e) {  }
	          System.out.println("Writer cannot write."); 
	       
              total_reader++; 
              
	         if (total_reader == 1){ 
	            try{
	  	           S.acquire();
	              }
	            catch (InterruptedException e) {}
	         }
	   
	         System.out.println("<Reader is reading.> Total Reader = " + total_reader);
	         System.out.println("Writer wants to write.");
	         RC.release();
	         
	         try {
				  Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	       
	}
	public void readUnLock() {
		 try{
	  			 RC.acquire(); 
	         }
	             catch (InterruptedException e) {}
	      
	         total_reader--;
	         
	         if (total_reader == 0){ 
	        	 S.release();
	         }
	      
	         System.out.println("Reader is done with reading. Total Reader = " + total_reader);
	         RC.release(); 
	         
	         try {
				  Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	}

	public void writeLock() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Reader wants to read");
		 try{
			 S.acquire(); }
	             catch (InterruptedException e) {}

	         System.out.println("<Writer is writing.>");
	      } 
	
	public void writeUnLock() {
		System.out.println("Writer is done with writing.");
        S.release();
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Writer implements Runnable
{
   private ReadWriteLock RW_lock;
   
    public Writer(ReadWriteLock rw) {
    	RW_lock = rw;
   }
    public void run() {
      while (true){
    	  RW_lock.writeLock();
    	  RW_lock.writeUnLock();
      }
   }
}

class Reader implements Runnable
{
   private ReadWriteLock RW_lock;
   
   public Reader(ReadWriteLock rw) {
    	RW_lock = rw;
   }
    public void run() {
      while (true){ 	    	  
    	  RW_lock.readLock();
    	  RW_lock.readUnLock();
      }
   }
}