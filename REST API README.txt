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

This Document is sopposed to mirror certain information from the "SERVER README.txt"
file. It is important to check that file for updates incase someone forgot to
update this file. In which case, please report it to us and we will fix it.

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
