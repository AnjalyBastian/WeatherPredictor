package com.datamine.predictor.model;

import org.apache.log4j.Logger;

import com.datamine.predictor.util.Constants;

public class ModelFactory {
	
	public final static Logger logger = Logger.getLogger(ModelFactory.class);

	/**
	 * Factory Pattern to find the appropriate model based on the App property file.
	 * 
	 * @param modelName - As per the configuration
	 * @return Model
	 * @throws Exception - If any error in identifying the model.
	 */
	public Model getModel(String modelName) throws Exception {

			logger.info("modelName : " + modelName);
			
			switch (modelName) {
			case Constants.OLS_MULTIPLE_LINEAR_REGRESSION:
				return new OLSMultipleLinearRegressionModel();
			default:
				logger.error("invalid model name .... please specify a supported model name");
				throw new Exception("invalid model name .... please specify a supported model name");
		}
	}
}
