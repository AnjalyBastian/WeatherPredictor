package com.datamine.predictor.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.datamine.predictor.dto.StationDetails;

public class StationDetailsLoader {

	public final static Logger logger = Logger.getLogger(StationDetailsLoader.class);

	public static Map<String, StationDetails> stations = new HashMap<String, StationDetails>();

	static {
		try {
			List<CSVRecord> csvRecords = FileUtil.readCSV(Constants.STATION_DETAILS_PATH);

			if (null != csvRecords && !csvRecords.isEmpty()) {
				csvRecords.remove(0);
				for (CSVRecord csvRecord : csvRecords) {
					StationDetails stationDetails = new StationDetails();
					stationDetails.setStationName(csvRecord.get(0));
					stationDetails.setIataCode(csvRecord.get(1));
					stationDetails.setLatitude(csvRecord.get(2));
					stationDetails.setLongitude(csvRecord.get(3));
					stationDetails.setAltitude(csvRecord.get(4));
					stations.put(csvRecord.get(0), stationDetails);
				}
			}else{
				logger.error("Error while parsing station details..");
			}
		} catch (Exception exception) {
			logger.error("Error while loading station details..");
		}
	}

	public static StationDetails getStationData(String StationName) {
		return stations.get(StationName);
	}

}
