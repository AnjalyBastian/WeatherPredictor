package com.datamine.predictor.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.datamine.predictor.dto.ModelData;
import com.datamine.predictor.dto.WeatherData;
import com.datamine.predictor.dto.WeatherParameters;
import com.datamine.predictor.model.OLSMultipleLinearRegressionModel;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestOLSMultipleLinearRegressionModel extends TestCase {

	@Test
	public void testpreprocessWeatherDataForSuccess() {
		OLSMultipleLinearRegressionModel olsMultipleLinearRegressionModel = new OLSMultipleLinearRegressionModel();
		try {
			WeatherData weatherData = createWeatherData();
			Map<String, ModelData> preprocessedData = olsMultipleLinearRegressionModel.preprocessWeatherData(weatherData);
			
			for (Entry<String, ModelData> preprocessedDataEntry : preprocessedData.entrySet()) {
				Assert.assertTrue(preprocessedDataEntry.getValue().getDependentVariableArry().length > 0);
				Assert.assertTrue(preprocessedDataEntry.getValue().getIndependentVariableArry().length > 0);
				
			}
		} catch (Exception exception) {
			Assert.fail("Testcase for preprocessorfailed");
		}
	}
	
	@Test
	public void testGetOLSMultipleLinearRegressionCoefficientsForSuccess() {
		OLSMultipleLinearRegressionModel olsMultipleLinearRegressionModel = new OLSMultipleLinearRegressionModel();
		try {
			Map<String,ModelData> modelData = createModelData();
			String modelCoefficients = olsMultipleLinearRegressionModel.evaluateModelCoeffients(modelData );
			
			Assert.assertTrue(null != modelCoefficients);
				
		} catch (Exception exception) {
			Assert.fail("Testcase for evaluating regression coefficients failed");
		}
	}
	
	private Map<String,ModelData> createModelData() {
		Map<String,ModelData> modelDatas = new HashMap<String,ModelData>();
		ModelData modelData = new ModelData();
		modelData.setIndependentVariableArry(new double[]{2.3,0.345,2.456,0.124,1.09,1.234});
		modelData.setDependentVariableArry(new double[][]{{4.3,2.3},{6.345,0.987},{2.456,8.456},{5.124,3.456},{2.09,3.45},{2.098,1.2564}});
		modelData.setHumidityVariableArry(new double[]{20.3,23.345,21.456,10.124,10.09,14.234});
		modelData.setPressureVariableArry(new double[]{22.3,5.345,2.456,5.124,1.09,1.234});
		modelDatas.put("66196", modelData);
		return modelDatas;
	}

	private WeatherData createWeatherData() {
		WeatherData weatherData = new WeatherData();
		Map<String, Map<String, WeatherParameters>> stationWiseDailyStats = new HashMap<String, Map<String, WeatherParameters>>();
		Map<String, WeatherParameters> dailyStats = new HashMap<String, WeatherParameters>();
		dailyStats.put("2016-07-20", setWeatherParameters(10.0,12.0,25.0));
		dailyStats.put("2016-07-21", setWeatherParameters(15.0,22.0,22.0));
		dailyStats.put("2016-07-22", setWeatherParameters(19.0,22.0,35.0));
		stationWiseDailyStats.put("66196", dailyStats);
		weatherData.setStationWiseDailyStats(stationWiseDailyStats );
		return weatherData;
	}

	private WeatherParameters setWeatherParameters(double temperature,double pressure,double humidity) {
		WeatherParameters weatherParameters = new WeatherParameters();
		weatherParameters.setTemperature(temperature);
		weatherParameters.setHumidity(humidity);
		weatherParameters.setPressure(pressure);
		return weatherParameters;
	}
}
