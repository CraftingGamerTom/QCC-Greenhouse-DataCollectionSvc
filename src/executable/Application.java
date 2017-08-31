package executable;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import dataSets.Sensor;
import handlers.ConfigurationHandler;
import queues.SensorDataQueue;
import threads.ThreadPoolManager;

/**
 * @version 1.0
 * @author Thomas Rokicki This is the class that will start this entire service.
 */
@SpringBootApplication
public class Application {
	
	private static Logger logger = Logger.getLogger(Application.class);
	private ThreadPoolManager tm = new ThreadPoolManager();
	
	/** Queue for the sensor data */ 
	public static SensorDataQueue<Sensor> sdq;

	/**
	 * Starts the service.
	 * 
	 * @param args
	 *            an array of arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}
	
	/**
	 * Runs commands after the spring boot.
	 * @param ctx
	 * @return
	 */
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		
		ConfigurationHandler configReader = new ConfigurationHandler();
		configReader.read();
		
        createDatabaseTables();
        createQueues();
        createThreads();
        return args -> {
            logger.debug("Spring boot beans in use:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                logger.debug("Bean name:" + beanName);
            }
        };
    }
	
	private void createDatabaseTables() {
		// TODO implement database tables
		
	}

	private void createQueues(){
		sdq = new SensorDataQueue<Sensor>();
	}
	
	private void createThreads() {
		tm.startThreads();
	}
	
}