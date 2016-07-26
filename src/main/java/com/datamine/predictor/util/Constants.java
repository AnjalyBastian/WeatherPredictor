package com.datamine.predictor.util;

public class Constants {
	
	public static final String MODEL_COEFFICENT_PATH = "src/main/resources/StationWiseCoefficients.csv";
	public static final String WEATHER_DATA_PATH = "src/main/resources/StationData";
	public static final String STATION_DETAILS_PATH = "src/main/resources/StationDetails.csv";
	public static final String FORECASTED_DATA_PATH = "src/main/resources/ForecastedData.csv";
	public static final String PROPERTIES_PATH = "src/main/resources/weatherpredictorprops.properties";
	public static final String DELIMITER_COMMA = ",";
	public static final String DELIMITER_PIPE = "|";
	public static final String NEW_LINE_SEPARATOR = "\n";
	public static final String FILE_HEADER = "station_id,Constant,HumidityCoefficient,PressureCoefficient,TemperatureT-1Coefficent,TemperatureT-365Coefficient,TemperatureT-366Coefficient,Pressure StdDev,Humidity StdDev";
	public static final String FORECAST_FILE_HEADER = "Station,Location,Date,Conditions,Temperature,Pressure,Humidity";
	public static final int DEFAULT_FORECAST_DAYS = 10;
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String OLS_MULTIPLE_LINEAR_REGRESSION = "OLSMultipleLinearRegression";
	public static final String PROPERTY_NAME_MODEL = "Model";
	public static final String APP_TRIGGER_MODEL = "model";
	public static final String APP_TRIGGER_FORECAST = "forecast";
	public static final String APP_TRIGGER_ALL = "all";
	

}
