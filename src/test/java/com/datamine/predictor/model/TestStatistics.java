package com.datamine.predictor.model;

import org.junit.Test;

import com.datamine.predictor.model.Statistics;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestStatistics extends TestCase {
	@Test
	public void testGetStandardDeviationForSuccess() {
		try {
			double[] dataArray = new double[]{20.3,23.345,21.456,10.124,10.09,14.234};
			double stdDev = Statistics.getStandardDeviation(dataArray );
			Assert.assertEquals(5.876767129978862, stdDev, 0);
				
		} catch (Exception exception) {
			Assert.fail("Testcase for getting standard deviation failed");
		}
	}
}
