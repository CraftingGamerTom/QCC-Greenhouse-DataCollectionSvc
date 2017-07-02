package executable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @version 1.0
 * @author Thomas Rokicki This is the class that will start this entire service.
 */
@SpringBootApplication
public class Application {

	/**
	 * Starts the service.
	 * 
	 * @param args
	 *            an array of arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}