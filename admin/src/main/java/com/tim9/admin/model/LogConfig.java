package com.tim9.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogConfig {
	
	private String filePath;
	private int interval;
	private String regexp;
	private String hospital;
}
