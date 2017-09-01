package executable;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.mongodb.MongoClient;

import dataSets.SensorData;
import database.MongoClientConnection;
import handlers.ConfigurationHandler;
import queues.SensorDataQueue;
import threads.ThreadPoolManager;

/**
 * @version 1.0
 * @author Thomas Rokicki This is the class that will start this entire service.
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages = {"controllers"}) // NEEDED to map end points in controllers package
public class Application {
	
	private static Logger logger = Logger.getLogger(Application.class);
	private ThreadPoolManager tm = new ThreadPoolManager();
	
	/** Mongo Client that is used to put data into the database */
	public static MongoClient client; // May not be needed in Application due to use of instances - not sure.
	/** Queue for the sensor data */ 
	public static SensorDataQueue<SensorData> sdq;

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
		
		ConfigurationHandler configHandler = new ConfigurationHandler();
		configHandler.read();
		configHandler.verify();
		
        createDatabaseConnection();
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
	
	private void createDatabaseConnection() {
		client = MongoClientConnection.getInstance(); // Creates connection once (Singleton Object)
		
	}

	private void createQueues(){
		sdq = new SensorDataQueue<SensorData>();
	}
	
	private void createThreads() {
		tm.startThreads();
	}
	
}