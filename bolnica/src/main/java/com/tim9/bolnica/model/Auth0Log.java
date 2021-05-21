package com.tim9.bolnica.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auth0Log {

	private String log_id;
	private Date date;
	private String type;
	private String ip;
	private String user_name;
	private String hostname;
}
