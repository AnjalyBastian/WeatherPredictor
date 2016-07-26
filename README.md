# WeatherPredictor

##Introduction

WeatherPredictor is a prediction tool used to forecast the future weather conditions based on selected weather parameters - *temperature, pressure and humidity*. This tool at present forecasts weather for 3 major Australian locations using **Multiple Linear Regression Model** and **simple decision** models. 

## Software Stack

* **Language:** Java 1.7
* **Build Tool:** Maven
* **Unit Tests:** Junit 4.4
* **Logger:** Log4j 1.2.17
* **Statistics Library:** commons-math3 3.6.1

## Problem Statement

Create a toy model of the environment (taking into account things like atmosphere, topography, geography, oceanography, or similar) that evolves 
over time. Then take measurements at various locations (ie weather stations), and then have the program emit that data, as in the following:
	
	SYD|-33.86,151.21,39|2015-12-23T05:02:12Z|Rain|+12.5|1004.3|97
    MEL|-37.83,144.98,7|2015-12-24T15:30:55Z|Snow|-5.3|998.4|55
    ADL|-34.92,138.62,48|2016-01-03T12:35:37Z|Sunny|+39.4|1114.1|12

## Solution Approach

* **Model Selection** 

    * Since pressure and humidity has direct influence on temperature, tried to work out a linear relationship between the dependent variables - humidity and pressure with the 			  independent variable temperature. 
    * Considered the seasonality factor that could affect the temperature at a given point, the temperature on the previous day, temperature on the same day of the previous year, 			  temperature on the previous day of the previous year are also selected as dependent variables for finding the temperature value.
    * An year worth of data from different stations are fed to the Multiple linear regression model to evaluate the model coefficients.
    
    
* **Forecasting using Model Coefficients**
    
    * The temperature at a given point is evaluated as follows 
    
            Temp(t) = Constant + Temp(t-1)Coeff * Temp(t-1) + Temp(t-365)Coeff * Temp(t-365) + Temp(t-366)Coeff * Temp(t-366) 
            + PressureCoeff * Pressure + HumidityCoeff * Humidity
            
            Where the Pressure and Humidity at a given point is evaluated by using a weak relationship 
            x(t) = Random value between (Avg(x(t-1), x(t-365)) - Standard Deviation) and (Avg(x(t-1), x(t-365)) + Standard Deviation) for a given station.
            
            P.S : Could have used a regression algorithm but settled for a weak model for these parameters for the time being.     
       
       
   * Once the temperature is forecasted, the overall weather condition(SNOW/RAIN/SUNNY) is evaluated using a simple rule based decision model. 
      
            P.S : This could have been fairly evaluated by using a classification algorithm but settled for a weak decision rule. 

## Solution Implementation 
 
The Application is designed to work on the following 3 modes :- 

1. **Model Learner** :-
    * As per the configured number of stations in the (StationDetails.csv), the Model Learner will read in each of the station data. 
    * The model is selected as per the configuration(weatherpredictor.properties) and initiates the learning. 
    * Depending upon the model, the data is pre-processed and transformed to a new dataset which is suitable for the model.
    * As of now, the only model implemented is a Multiple Linear regression and hence after the learning process, Model coefficents will be 
      written on to the disk for the Forecaster component to make use of.
       
2. **Forecaster** : -
    * For each of the Model Coefficients available in the (StationWiseCoefficients.csv), the Learner component will read in the latest dataset. 
    * For each station, starting from the latest record, the temperatures for the next threshold limit(days) will be predicted using the equation mentioned in the Solution approach. 
    * Using the predicted temperature, pressure and humidity the Weather status is identified using the simple decision rule.
    
3. **Combined Mode** : -
    * First triggers the Model Learner component #1. 
    * Once the coefficients are available, the Forecaster component #2 will be triggered in turn. 

## Instructions to Run

* Pre-requisite 
    * Java should be configured and installed
    * Maven should be configured and installed

* Steps 
    * Clone the Project
    * Go to the cloned location. 
    * Build using maven. The test cases will be triggered along with the build and if everything is fine, the executable jar will be packaged as zip file. 
                       
            mvn clean package
   * Go to the target location and copy the zip file to any preferred location and unzip it. 
            
            cp -prf target/weatherprediction-1.0.0-project.zip <location>
            cd <location>
            unzip weatherprediction-1.0.0-project.zip
   * Trigger the application. (Learner / Forecaster / Combined )
            
            cd weatherprediction-1.0.0
            java -jar weatherprediction-1.0.0.jar all 10
            
        For Learner alone 
           
            java -jar weatherprediction-1.0.0.jar model
            
       For Forecaster alone
            
            java -jar weatherprediction-1.0.0.jar forecast 10

   * Sample Output 
        
            ADL|-34.92,138.62,48|2016-08-03|RAIN|9.283396714501311|1077.7484950360424|98.00816946032991
            ADL|-34.92,138.62,48|2016-08-04|RAIN|7.351430335451061|811.3475066988574|78.9966631673341
            MLB|-37.83,144.98,7|2016-07-26|SUNNY|11.020619595457635|782.9874482417972|29.57946470716902
            MLB|-37.83,144.98,7|2016-07-27|SUNNY|15.212161656332833|1299.1756402967023|32.71368436175436
            MLB|-37.83,144.98,7|2016-07-28|SUNNY|13.777025695427689|987.1838202345452|47.13341710990599
            MLB|-37.83,144.98,7|2016-07-29|SUNNY|14.229932206855022|1440.6430439053618|66.6450550800758
            MLB|-37.83,144.98,7|2016-08-03|RAIN|6.58560218850841|880.6653514303274|82.31367060147723
            MLB|-37.83,144.98,7|2016-08-04|RAIN|8.856191955973038|1237.8214581571597|77.37807823102179
            SYD|-33.86,151.21,39|2016-07-26|RAIN|13.404459143549369|1364.3538533900312|74.5226855956168
            SYD|-33.86,151.21,39|2016-07-27|SUNNY|11.905399500764553|1277.70448148376|44.32450692298404
            SYD|-33.86,151.21,39|2016-08-03|SUNNY|11.900238843016428|602.1823490751386|63.370493010110174
            SYD|-33.86,151.21,39|2016-08-04|SUNNY|9.655048283965616|433.28349558721106|33.95037791214641
            
            
            The output file will be generated as src/main/resources/ForecastedData.csv


## Implementation Considerations 

* The tool is designed to work as pluggable components (Learner / Forecaster / Combined)
* Any station can be easily added and make predictions without any code change. 
* Any model can be easily integrated with minimal code change.

##Scope for Improvement

1. More precise models can be incorporated with no much hassle.
2. The Data preprocessor can be designed to use a persistent layer.   
3. More Junit tests to have more code coverage and confidence.
4. Only data from 3 stations are included as of now. We could expand the number of stations and add in more historical data to make the model more efficient. 
5. Use classification algorithms to predict the weather condition. 