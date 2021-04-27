package com.tim9.bolnica.model;

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
	private long interval;
	private String regexp;
}
