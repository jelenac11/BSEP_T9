package com.tim9.bolnica.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.model.Log;

public class SimulatorLogParser extends LogParser {

	@Override
	public Log parse(String log) {
		String[] tokens = log.split(" ");

		Date date = null;
		try {
			date = DateUtil.parse(tokens[0] + " " + tokens[1]);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		String[] host = tokens[2].split("-");
		String source = host[0];
		String ip = host[1];
		String[] fS = tokens[3].split("_");
		LogFacility lf = LogFacility.valueOf(fS[0]);
		LogSeverity ls = LogSeverity.valueOf(fS[1]);
		String message = String.join(" ", Arrays.asList(tokens).subList(4, Arrays.asList(tokens).size()));

		return new Log(null, date, source, lf, ls, ip, message, "");
	}

}
