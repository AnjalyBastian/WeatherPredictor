package com.datamine.predictor.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.datamine.predictor.exception.UtilException;

public class FileUtil {

	public final static Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * Util to write content on to a File
	 * 
	 * @param filePath
	 * @param content
	 * @return status of the I/O Operation.
	 * @throws Exception if any error has occurred. 
	 */
	public static boolean writeFile(String filePath, String content) throws Exception {
		FileWriter fileWriter = null;
		try {
			if (!new File(filePath).exists())
				new File(filePath).createNewFile();

			fileWriter = new FileWriter(filePath);
			fileWriter.append(content);
		} catch (Exception exception) {
			throw new Exception("Exception while  writing data");
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				logger.info("successfully wrote the content in the csv file :" + filePath);
				fileWriter = null;
			} catch (Exception exception) {
				throw new Exception("Exception while closing file resources");
			}
		}

		return true;
	}

	public static List<CSVRecord> readCSV(String filePath) throws IOException {
		CSVParser csvParser = null;
		try {
			csvParser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT);
			List<CSVRecord> csvRecords = csvParser.getRecords();
			logger.info("successfully parsed the csv file :" + filePath);
			return csvRecords;
		} finally {
			csvParser.close();

		}
	}

	public static File[] listFiles(String path) throws UtilException {
		if (null != path) {
			File[] fileNames = {};
			File file = new File(path);
			if (file.isDirectory()) {
				fileNames = file.listFiles();
			} 
			return fileNames;
		} else {
			throw new UtilException("Exception occured file listing files in the path : " + path);
		}
	}
}
