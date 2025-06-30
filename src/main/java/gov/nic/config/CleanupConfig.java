package gov.nic.config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;

@Configuration
public class CleanupConfig {

	@PreDestroy
	public void onShutdown() {
		System.out.println("[CleanupConfig] Running shutdown hooks...");

		// JDBC driver deregistration
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if (driver.getClass().getClassLoader() == cl) {
				try {
					System.out.println("Deregistering JDBC driver: " + driver);
					DriverManager.deregisterDriver(driver);
				} catch (SQLException e) {
					System.err.println("Error deregistering driver: " + driver);
					e.printStackTrace();
				}
			}
		}

		// Shut down loggers
		try {
			System.out.println("Shutting down Log4j2...");
			LogManager.shutdown();
		} catch (Exception e) {
			System.err.println("Error shutting down Log4j2.");
			e.printStackTrace();
		}

		// Flush Java introspector caches (optional)
		java.beans.Introspector.flushCaches();
	}

}
