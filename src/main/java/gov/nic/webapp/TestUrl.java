package gov.nic.webapp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUrl {

	 public static void main(String[] args) {
	        String url = "http://localhost:8080/ChatBot/api/v1/gstin/details?gstin=19AIXPD2657H1ZS";
	        String apiKey = "your-api-key";  // Replace with your actual API key
	        int numberOfRequests = 100;
	        int concurrentThreads = 10;

	        ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);

	        for (int i = 0; i < numberOfRequests; i++) {
	            executor.submit(() -> {
	                HttpURLConnection conn = null;
	                try {
	                    URL endpoint = new URL(url);
	                    conn = (HttpURLConnection) endpoint.openConnection();
	                    conn.setRequestMethod("GET");

	                    // Set API Key Header
	                    conn.setRequestProperty("X-API-KEY", apiKey);  // Adjust header name if needed
	                    conn.setConnectTimeout(5000);
	                    conn.setReadTimeout(5000);

	                    int responseCode = conn.getResponseCode();
	                    System.out.println("Thread: " + Thread.currentThread().getName() + ", Response Code: " + responseCode);
	                } catch (Exception e) {
	                    System.err.println("Error: " + e.getMessage());
	                    e.printStackTrace();
	                } finally {
	                    if (conn != null) {
	                        conn.disconnect();
	                    }
	                }
	            });
	        }

	        executor.shutdown();
	    }

}
