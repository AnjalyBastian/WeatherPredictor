package com.datamine.predictor.io;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.datamine.predictor.dto.WeatherData;
import com.datamine.predictor.dto.WeatherParameters;
import com.datamine.predictor.exception.WeatherParseException;
import com.datamine.predictor.util.Constants;
import com.datamine.predictor.util.DateUtil;
import com.datamine.predictor.util.FileUtil;

public class WeatherParser {

	public final static Logger logger = Logger.getLogger(WeatherParser.class);

	/**
	 * Parses the CSV data and converts the valid data to POJO
	 * 
	 * @param weatherStatsPath - Input data location - directory
	 * @return WeatherData
	 * @throws WeatherParseException - if any error in parsing the source records. 
	 */
	public WeatherData parse(String weatherStatsPath) throws WeatherParseException {
		WeatherData weatherData = new WeatherData();
		Map<String, Map<String, WeatherParameters>> stationWiseDailyWeatherStats = weatherData
				.getStationWiseDailyStats();
		
		try {

			// Reads each station data one by one. Easily pluggable, as new station data only needs to be added to the directory. 
			for (File fileName : FileUtil.listFiles(weatherStatsPath)) {

				List<CSVRecord> csvRecords = FileUtil.readCSV(fileName.getPath());
				if (null != csvRecords && !csvRecords.isEmpty()) {
					// to remove header from processing
					csvRecords.remove(0);
					for (CSVRecord csvRecord : csvRecords) {
						Map<String, WeatherParameters> dailyWeatherStats = null;
						WeatherParameters weatherParameters = null;

						// to skip the record if any of the fields are missing.
						if (isRecordMissing(csvRecord)) {
							continue;
						}

						String stationId = csvRecord.get(0);
						String formattedDate = DateUtil.formatDate(csvRecord.get(1), Constants.DEFAULT_DATE_FORMAT);
						double temperature = Double.parseDouble(csvRecord.get(2));
						double humidity = Double.parseDouble(csvRecord.get(3));
						double pressure = Double.parseDouble(csvRecord.get(4));
						
						if (stationWiseDailyWeatherStats.containsKey(stationId)) {
							dailyWeatherStats = stationWiseDailyWeatherStats.get(stationId);
							if (dailyWeatherStats.containsKey(formattedDate)) {
								// Removes duplicate records
								logger.error("Duplicate Records for the date :" + formattedDate
										+ " and for the station : " + stationId);
							} else {
								weatherParameters = setWeatherParameters(temperature, humidity, pressure);
							}

						} else {
							dailyWeatherStats = new HashMap<String, WeatherParameters>();
							weatherParameters = setWeatherParameters(temperature, humidity, pressure);
						}
						dailyWeatherStats.put(formattedDate, weatherParameters);
						stationWiseDailyWeatherStats.put(stationId, dailyWeatherStats);
					}
				}
			}

		} catch (Exception exception) {
			logger.error("Unable to parse weather data");
			throw new WeatherParseException(exception, "Unable to parse weather data");
		}
		logger.info("Weather data parsed successfully");
		return weatherData;
	}

	private boolean isRecordMissing(CSVRecord csvRecord) {
		return null == csvRecord || null == csvRecord.get(0) || csvRecord.get(0).isEmpty() || null == csvRecord.get(1)
				|| csvRecord.get(1).isEmpty() || null == csvRecord.get(2) || csvRecord.get(2).isEmpty()
				|| null == csvRecord.get(3) || csvRecord.get(3).isEmpty() || null == csvRecord.get(4)
				|| csvRecord.get(4).isEmpty() ;
	}

	private WeatherParameters setWeatherParameters(double temperature, double humidity, double pressure) {
		WeatherParameters weatherParameters = new WeatherParameters();
		weatherParameters.setTemperature(temperature);
		weatherParameters.setHumidity(humidity);
		weatherParameters.setPressure(pressure);
		return weatherParameters;
	}

}