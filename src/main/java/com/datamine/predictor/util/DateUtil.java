package com.datamine.predictor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date parseDate(String date, String dateFormat) throws ParseException {
		return new SimpleDateFormat(dateFormat).parse(date);
	}

	public static String formatDate(Date date, String dateFormat) throws ParseException {
		return new SimpleDateFormat(dateFormat).format(date);
	}
	
	public static String formatDate(String date, String dateFormat) throws ParseException {
		Date parsedDate = parseDate(date, dateFormat);
		return new SimpleDateFormat(dateFormat).format(parsedDate);
	}

	public static String getDate(String date, String dateFormat, int field, int amount) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, dateFormat));
		calendar.add(field, amount);
		return formatDate(calendar.getTime(), dateFormat);
	}

}
