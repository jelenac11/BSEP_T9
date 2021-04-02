package com.tim9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CSRResponseDTO {

	private Long id;
	private String commonName;
	private String organization;
	private String organizationalUnit;
	private String cityLocality;
	private String stateCountyRegion;
	private String country;
	private String emailAddress;
}
