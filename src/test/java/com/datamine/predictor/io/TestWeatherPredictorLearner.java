package com.datamine.predictor.io;

import org.junit.Test;

import com.datamine.predictor.WeatherPredictorLearner;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestWeatherPredictorLearner extends TestCase {
	
	@Test
	public void testTriggerForSuccess(){
		
		WeatherPredictorLearner weatherPredictorLearner = new WeatherPredictorLearner();
		try {
			Assert.assertTrue(weatherPredictorLearner.trigger());
		} catch (Exception exception) {
			Assert.fail("Testcase for model learner failed");
		}
	}

}
