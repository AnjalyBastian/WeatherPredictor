package com.datamine.predictor.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyLoader {
	
	public final static Logger logger = Logger.getLogger(PropertyLoader.class);

	public static Properties properties = new Properties();
	
	static{
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(Constants.PROPERTIES_PATH);
			properties.load(inputStream);
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.error("Error while loading property file..");
		}finally{
			try {
				inputStream.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				logger.error("Error while closing resources for property file loaders..");
			}
		}
	}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}

}
