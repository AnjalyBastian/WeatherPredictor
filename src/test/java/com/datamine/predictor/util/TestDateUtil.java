package com.datamine.predictor.util;

import java.util.Calendar;

import org.junit.Test;

import com.datamine.predictor.util.DateUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestDateUtil extends TestCase {
	@Test
	public void testgetDateForSuccess() {
		try {
			String date = DateUtil.getDate("2016-07-22", "yyyy-MM-dd", Calendar.DAY_OF_MONTH, 1);
			Assert.assertEquals("2016-07-23", date);
		} catch (Exception exception) {
			Assert.fail("Testcase for model forecaster failed");
		}
	}
}
