package gov.nic.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;

@Service
public class CustomExecutorService {
	
	 private final ExecutorService executor = Executors.newFixedThreadPool(5);

	    public void submitTask(Runnable task) {
	        executor.submit(task);
	    }

	    @PreDestroy
	    public void cleanup() {
	        System.out.println("[CustomExecutorService] Shutting down executor.");
	        executor.shutdown();
	        try {
	            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
	                executor.shutdownNow();
	            }
	        } catch (InterruptedException e) {
	            executor.shutdownNow();
	        }
	    }

}
