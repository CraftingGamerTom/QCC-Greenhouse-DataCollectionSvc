# QCC-Greenhouse-DataCollectionSvc
QCC Live and Learn Greenhouse data collection service to be ran on a server.

                                 MIT LICENSE

Copyright 2017 Thomas Rokicki

Permission is hereby granted, free of charge, to any person obtaining a copy of 
this software and associated documentation files (the "Software"), to deal in the 
Software without restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION 
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

                            OTHER LICENSES
- This software uses MongoDB and is intended to be ran alongside it. I do not own
anything that MongoDB has written. Big thank you to the MongoDB team for being
awesome.
- This software uses Apache Tomcat to run as a REST Client. I do not own anything
written by Apache but would like to thank them for all their hard work.
- This software uses Spring.io to boot and run Apache Tomcat. I do not own anything
written by the Spring.io folks. You guys are pretty cool though, nice work.

                            THINGS TO KNOW

This will help explain how this jar file works to all of you whom continue this
task. Be sure to read the java documentation as this will help explain the code
as well.
This file will be updated over time. Thank you for your patience. 

**A NOTE TO FUTURE AUTHORS: You MUST include documentation for your changes. It
is CRITICAL to the growth and maintainability of our project. 

This service is intended to be hosted on the back-end (meaning the end-user 
will not see or access this). 

*** START-UP ***
This service begins by starting Spring and running a Tomcat server that will 
wait until a request is sent to it. [To change the port that the server runs on
please see 'main/resources/application.properties' : 'server.port'].


		**************************************************
		* ----------------- READ ME -------------------- *
		* - Server / Database Engineer: Thomas Rokicki - *
		* --------- Document By: Thomas Rokicki -------- *
		* ---------------------------------------------- *
		* -------------- Date: 5/23/2017 --------------- *
		* ------------ Revised: 7/25/2017 -------------- *
		* ---------------------------------------------- *
		**************************************************

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
	~Puts sensor data into the raw data database table
	~Puts sensor data in persistent tables for UI (hourly, daily, weekly, monthly, 
	yearly)
	~Puts sensor data in for live data viewing (Keeps latest sensor info)
	~Creates database to handle friendly sensor names


			----------   Data Collection   ---------- 

The types of sensor objects are not limited. They are determined by the JSON sent 
to the REST client. The seperation of Objects is mearly for maintainability and 
understanding. These values are derived from json formatted text. They are stored 
as SensorData.java Objects in the Listener Service. Once the JSON string is seperated 
into objects the database is updated and the objects are forgotten from memory. 
After this point all data must be accessed via the User Interface (UI) on the front-end 
or via console.

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
	"date": "2017-06-02T03:16:46-04:00",
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
		"type": "temperature",
		"value": 110
	}, {
		"id": "a02-02",
		"type": "heat",
		"value": 86
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
available procedures for viewing data.

 - Raw Data
 Raw data is all the data that is ever passed into the database - never purged
 - Hourly Data
 The High and Low value based on a given hour for each sensor
 - Daily Data
 The High and Low value based on a given day for each sensor
 - Weekly Data
 The High and Low value based on a given week for each sensor
 - Monthly Data
 The High and Low value based on a given month for each sensor
 - Yearly Data
 The High and Low value based on a given year for each sensor

The collection(table) names are maintained in another collection to be given a friendly
name for the front end.

The collection names are defined in the configuration.cfg file that is read by the 
ConfigurationHandler Object.

Viewing on UI:
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
E-mails are meant to be sent by the raspberry pi to admin email account to be notified
when data pushes are missed. That is going to be handled by the raspberry pi according
to Brendan Russel.

In the case the Raspberry Pi becomes disabled entirely, I plan to enable a way to make
sure data is sent to the REST API. If no data is recieved for say, 1 hour, then an email
alert will go out to administrators.
