package com.datamine.predictor.dto;

import java.util.HashMap;
import java.util.Map;

public class WeatherData {

	// key of outer map is station id and of inner map is date
	private Map<String, Map<String, WeatherParameters>> stationWiseDailyStats;

	public WeatherData() {
		this.stationWiseDailyStats = new HashMap<String, Map<String, WeatherParameters>>();
	}

	public Map<String, Map<String, WeatherParameters>> getStationWiseDailyStats() {
		return this.stationWiseDailyStats;
	}

	public void setStationWiseDailyStats(Map<String, Map<String, WeatherParameters>> stationWiseDailyStats) {
		this.stationWiseDailyStats = stationWiseDailyStats;
	}

}
