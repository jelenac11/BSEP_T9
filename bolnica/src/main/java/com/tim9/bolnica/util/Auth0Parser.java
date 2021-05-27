package com.tim9.bolnica.util;

import com.google.gson.GsonBuilder;
import com.tim9.bolnica.enums.Auth0Type;
import com.tim9.bolnica.enums.LogFacility;
import com.tim9.bolnica.enums.LogSeverity;
import com.tim9.bolnica.model.Auth0Log;
import com.tim9.bolnica.model.Log;

public class Auth0Parser extends LogParser {

	@Override
	public Log parse(String log) {
		log = "{" + log + "}";
		Auth0Log auth0Log = new GsonBuilder().create().fromJson(log, Auth0Log.class);
		String hostname = "Auth0";
		String ip = "";
		String message = Auth0Type.valueOf(auth0Log.getType()).getValue();
		System.out.println(message);
		if (auth0Log.getHostname() != null) {
			if (!auth0Log.getHostname().equals("")) {
				hostname = auth0Log.getHostname();	
			}
		}
		if (auth0Log.getUser_name() != null) {
			if (!auth0Log.getUser_name().equals("")) {
				message = message + " Username: " + auth0Log.getUser_name();
			}
		}
		if (auth0Log.getIp() != null) ip = auth0Log.getIp();
		return new Log(null, auth0Log.getDate(), hostname, LogFacility.AUTH, LogSeverity.INFORMATIONAL, ip, message, "");
	}

}
