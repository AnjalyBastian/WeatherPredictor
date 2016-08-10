package com.datamine.predictor.model;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.log4j.Logger;

import com.datamine.predictor.exception.ModelLearnerException;

public class Statistics {
	public final static Logger logger = Logger.getLogger(Statistics.class);
	
	public static double  getStandardDeviation(double[] dataArray) throws ModelLearnerException{
		return new StandardDeviation().evaluate(dataArray);
	}
}
