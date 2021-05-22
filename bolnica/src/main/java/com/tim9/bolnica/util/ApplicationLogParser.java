package com.tim9.bolnica.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.model.Log;

public class ApplicationLogParser extends LogParser {

	@Override
	public Log parse(String log) {
		String[] tokens = log.split(" ");

		Date date = null;
		try {
			date = DateUtil.parse(tokens[0] + " " + tokens[1]);
		} catch (ParseException e) {
			return null;
		}
		String source = tokens[4];
		String ip = "";
		String severity = tokens[2];
		if (severity.equals("INFO")) {
			severity = "INFORMATIONAL";
		} else if (severity.equals("WARN")) {
			severity = "WARNING";
		}
		String message = String.join(" ", Arrays.asList(tokens).subList(6, Arrays.asList(tokens).size()));

		return new Log(null, date, source, LogFacility.LOCAL0, LogSeverity.valueOf(severity), ip, message);
	}

}
