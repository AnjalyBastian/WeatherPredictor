package com.datamine.predictor.io;

import org.junit.Test;

import com.datamine.predictor.WeatherPredictorForecaster;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestWeatherPredictorForecaster extends TestCase {
	
	@Test
	public void testTriggerForSuccess(){
		int forecastLimit = 10;
		WeatherPredictorForecaster weatherPredictorForecaster = new WeatherPredictorForecaster(forecastLimit);
		try {
			Assert.assertTrue(weatherPredictorForecaster.trigger());
		} catch (Exception exception) {
			Assert.fail("Testcase for model forecaster failed");
		}
	}

}
