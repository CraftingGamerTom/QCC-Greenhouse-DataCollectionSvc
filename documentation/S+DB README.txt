		**************************************************
		* ----------------- READ ME -------------------- *
		* - Server / Database Engineer: Thomas Rokicki - *
		* --------- Document By: Thomas Rokicki -------- *
		* ---------------------------------------------- *
		* -------------- Date: 5/23/2017 --------------- *
		* ------------ Revised: 5/30/2017 -------------- *
		* ---------------------------------------------- *
		* (Latest) Web Service Version: 0.0.1-SNAPSHOT - *
		* ---------------------------------------------- *
		**************************************************
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
a payload (THIS MUST BE UPDATED TO A REAL EXAMPLE WHEN AVAILABLE). The date is 
standard ISO 8601 format. Java is able to generate this for you so please make it
easy on yourself and use the standard java libraries to do it properly.

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
			

			----------     Scheduling      ----------

As stated previously, the server handles data as it is given to it. The data will 
be received as an Array of data types in json format. The data is collected once 
per minute. This number is currently to keep an efficient pace but may be changed 
later. Once the "package" of information is created with the data it is sent to 
the server for collection. 

			----------   Data Retention    ----------

All data collected is to be kept for 24 hours. Each day at midnight 
high and low values for the day will be logged in a seperate table of the database
for permanant reference. 


			---------- Sending Data to RPi ----------
			TO-DO: Explain the data sent to the pi for opening door or vent at soonest convience (must be handled by the Pi) ^^^^^^^^^^^^^^^^^^^^^^^
			
			---------- E-Mail Notification ----------
			
			TO-DO: Explain email notification for Failed database updates, improper format, and when a new table is created(security) ^^^^^^^^^^^^^^

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
