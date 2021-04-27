package com.tim9.bolnica.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Config {
	private List<LogConfig> logConfigs;
}
