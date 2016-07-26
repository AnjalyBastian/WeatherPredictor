package com.datamine.predictor.util;

import org.apache.log4j.Logger;

import com.datamine.predictor.dto.CLIArguments;
import com.datamine.predictor.exception.UtilException;

public class CLIParser {
	
	public final static Logger logger = Logger.getLogger(CLIParser.class);

	/**
	 * Command Line Argument Parser
	 * 
	 * 
	 * @param args - Program Arguments
	 * @return CLIArguments - Parsed arguments
	 * @throws UtilException - Exception if any error occurs while parsing the CLI Arguments.
	 */
	public static CLIArguments getCLIArguments(String[] args) throws UtilException {
		CLIArguments cliArguments = new CLIArguments();

		try {
			if(args.length<1){
				logger.error("Insufficient number of arguments");
				throw new UtilException("Insufficient number of arguments");
			}else{
				cliArguments.setTrigger(args[0]);
				if(args.length > 1)
					cliArguments.setForecastLimit(Integer.parseInt(args[1]));
			}
		} catch (Exception exception) {
			logger.error("Exception while parsing command line arguments");
			throw new UtilException(exception,"Exception while parsing command line arguments");
		}
		logger.info("Command line arguments parsed successfully");
		return cliArguments;
	}

}
