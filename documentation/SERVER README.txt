		**************************************************
		* ----------------- READ ME -------------------- *
		* - Server / Database Engineer: Thomas Rokicki - *
		* --------- Document By: Thomas Rokicki -------- *
		* ---------------------------------------------- *
		* -------------- Date: 5/23/2017 --------------- *
		* ------------ Revised: 7/25/2017 -------------- *
		* ---------------------------------------------- *
		**************************************************
		
			----------   Latest Versions   ----------
Data Collector
	- GreenhouseServer-0.0.1-SNAPSHOT.jar
	
			----------      Overview       ----------

I will do my absolute best to update this document frequently and include as 
much information about the subject as possible. I understand that those whom
take on the task of maintaining this code, server, and database may have 
different skill sets or may not have experience at all and I will try to 
document absolutely everything.

			---------- Overall Description ----------
						
This aspect of the overall greenhouse project was created to handle any and all 
data collected by an "outside entity" (Arduino and Raspberry Pi). The job of the
Server is to sort data into a database (MongoDB) when it has been given such data
by the Raspberry Pi (which is scheduled). The data in the Mongo database is to be
accessible via the internet via a User Interface (UI) available on [QCC Server?]. 
This mentioned UI is designed to have multiple ways to Data Mine the database as 
well as provide live feedback. In addtion, in the event of a temperature higher 
than specified by the server, vents are to be open by the server automatically.
E-mail notification for overheating, unsafe humidity levels, motor failure, 
hydroponic water levels, and data transfer error reports are to be implemented 
(***This may also be handled by the Raspberry Pi*** TO BE DETERMINED). 

			----------   REST Endpoints    ----------
The DataCollector service handles requests based on endpoints and the information
in JSON format (See below). Endpoints can be over simplified by calling them links
that are typed into a browser. Below are a list of the current endpoints in the 
service and what they do
POST:
 - /sensorData
	Puts sensor data into the raw data database table
 - /observationData
	Puts observation data into the observation database table
 - /sensorData/{ard}/{sens}
	Currently only returns a response code
 - /sensorData/{id}
	Currently only returns a response code

			----------   Data Collection   ----------

There are currently five types of sensor objects. The seperation of Objects is 
mearly for maintainability and understanding. These values are derived from json
formatted text. They are stored as Sensor.java Objects in the Listener Service.
Once the JSON string is seperated into objects the database is updated and the
objects are forgotten from memory. After this point all data must be accessed via
the User Interface (UI) on the front-end or via console.

 - Door Object
	~Type (sensor type)
	~ID (unique identification maintained from sensor)
	~boolean (Open/Close)

 - Motor Object
 	~Type (sensor type)
	~ID (unique identification maintained from sensor)
	~boolean (Open/Close)
	
 - Temperature Object
 	~Type (sensor type)
	~ID (unique identification maintained from sensor)
	~double (value)
	
 - Humidity Object
	~Type (sensor type)
	~ID (unique identification maintained from sensor)
	~double (value)
	
 - Water Level Object
 	~Type (sensor type)
	~ID (unique identification maintained from sensor)
	~integer/double (value)
	
 - Ph Level Object
 	~Type (sensor type)
	~ID (unique identification maintained from sensor)
	~double (value)

			----------     JSON Format     ----------
An example is given below for how the data sent should be formatted to be sent as
a payload. The date is standard ISO 8601 format. Java is able to generate this for
you so please make it easy on yourself and use the standard java libraries to do 
it properly.

{
	"date": "2017-06-02T03:16:46+04:00",
	"sensors": [{
		"id": "rp1-01",
		"type": "temperature",
		"value": 67.2
	}, {
		"id": "rp1-02",
		"type": "humidity",
		"value": 60.2
	}, {
		"id": "a01-01",
		"type": "temperature",
		"value": 65.3
	}, {
		"id": "a01-02",
		"type": "humidity",
		"value": 61.0
	}, {
		"id": "a01-03",
		"type": "ph",
		"value": 22.3
	}, {
		"id": "a02-01",
		"type": "motor",
		"value": false
	}, {
		"id": "a02-01",
		"type": "door",
		"value": true
	}, {
		"id": "a02-03",
		"type": "water",
		"value": 99.1
	}]
}
			

			---------- Collection Schedule ----------

As stated previously, the server handles data as it is given to it. The data will 
be received as an Array of data types in json format. The data is (presumably) 
collected once per minute. This number is currently to keep an efficient pace but 
may be changed later. Once the "package" of information is created with the data 
it is sent to the server for collection. 

			----------   Data Retention    ----------

Data is to be collected and stored at the rate it is given to it. There will be two
available procedures for viewing data [Past 24 hours] & [Stored Data].

 - Past 24 Hours
This is the "raw data" it has the real time stamps, and exact data. We cannot store 
this permanantly because it will be rather redundant. Maybe we will just collect less 
often, who knows. In the meantime it is quite nice to see everything at a practically
real-time rate. These charts will be reset every day at midnight.

 - Stored Data
This data is set every night at midnight. Only averages, highs, and lows are kept in 
the database This is our way of viewing everything that has been logged in the past. 
There will be an option to view the following options.
 Hourly - Shows Average, high, and low values on a chart (Up to 1 week)
 Daily - Shows Average, high, and low values on a chart (Up to 1 month)
 Weekly - Shows Average, high, and low values on a chart (Up to 1 year)
 Monthly - Shows Average, high, and low values on a chart (Indefinite)
These will come with check boxes to allow up to the designated time to show data.
This prevents overloading the system with a large request. A date and time will be 
requested from a user and the database will work from that.


			---------- Sending Data to RPi ----------
A user whom needs to speak to the raspberry pi from the Website UI will go through a 
REST API. The only reason for this currently is to control motors (Vents and door). 
This is hidden behind an authentication software. Users must be authorized to use that 
part of the site. The persumed POST request sent to the Raspberry pi must be handled 
by the raspberry pi accordingly.	

			---------- E-Mail Notification ----------
E-mails are ment to be sent by the raspberry pi to admin email account to be notified
when data pushes are missed. That is going to be handled by the raspberry pi according
to Brendan Russel.

In the case the Raspberry Pi becomes disabled entirely, I plan to enable a way to make
sure data is sent to the REST API. If no data is recieved for say, 1 hour, then an email
alert will go out to administrators.
			
			----------    Webserver Use    ----------
Front End will be divided into three parts to some degree. 
 - Guest
 Anyone in the world can go to the site and view our data and notes available on 
 the UI. They cannot edit anything. Mearly just view the data.
 - User
 User accounts will be maintained by Admins. The Users may post notes to the database
 Their neames will be recorded as well. These notes are available on the front end. 
 Users cannot edit data, or remote control the motors in the greenhouse.
 - Admin
 Admins may control the motors through the webserver. In the future it would be nice
 to implement a way to edit notes, delete notes, send emails, etc through some sort
 of admin panel.
			
			  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
			  |       More Useful Information       |
			  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
Data Handling	
	- Send and Recieve json formatted data on a webserver
		https://www.webucator.com/how-to/how-send-receive-json-data-from-the-server.cfm
	- Understanding REST API
		http://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration
	- Understanding a RESTful Web Service
		https://spring.io/guides/gs/rest-service/
	-Using Jackson Parser
		http://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial

Data Handling (Less important)	
	- How to use use a Mongo Database with JavaScript (Node.js)
		https://www.w3schools.com/nodejs/nodejs_mongodb.asp (FULL TUTORIAL)
	- Understanding JSON.stringify();
		https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/JSON/stringify
	- Java API for JSON Intro
		http://www.oracle.com/technetwork/articles/java/json-1973242.html
	- Encoding JSON in Java
		https://www.tutorialspoint.com/json/json_java_example.htm
	- Using JSON with Java (video)
		https://www.youtube.com/watch?v=4rBeFDnw_oo

Passing information on
	- Mongo Chef (Studio3T)
		https://studio3t.com/#_product
	- Installing Node.js and MongoDB on Raspberry Pi
		http://yannickloriot.com/2016/04/install-mongodb-and-node-js-on-a-raspberry-pi/
	- OSI 8601 standard
		https://en.wikipedia.org/wiki/ISO_8601#Time_offsets_from_UTC
	- Validating JSON FORMAT
		https://jsonlint.com/

Webserver Management
	- How to setup TomCat on Linux
		https://www.mulesoft.com/tcat/tomcat-linux
	- How to build a Web Servlet in java
		https://www.sitepoint.com/tutorial-building-web-app-with-java-servlets/
		
Authentication
	- How to use Keycloak part 1 (Setup)
		https://www.youtube.com/watch?v=z-sUzl9eG6M
	- How to use Keycloak with a TomCat Server
		https://www.youtube.com/watch?v=FdYAdJkwynA
	- How to use Keycloak with Spring (Security Adapter)
		https://keycloak.gitbooks.io/documentation/securing_apps/topics/oidc/java/spring-security-adapter.html
	- How to use Keycloak with Spring (Boot Adapter)
		https://keycloak.gitbooks.io/documentation/securing_apps/topics/oidc/java/spring-boot-adapter.html
		
	