package com.tim9.bolnica.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CSRDTO {

	@Pattern(regexp = "[a-zA-Z0-9][a-zA-Z0-9\\. ]*", message = "Common Name is required and can only contain alphanumerical characters, dots and spaces.")
	private String commonName;

	@Pattern(regexp = "[a-zA-Z0-9][a-zA-Z0-9 ]*", message = "Organization is required and can only contain alphanumerical characters and spaces.")
	private String organization;
	
	@Pattern(regexp = "[a-zA-Z0-9][a-zA-Z0-9 ]*", message = "Organizational Unit is required and can only contain alphanumerical characters and spaces.")
	private String organizationalUnit;
	
	@Pattern(regexp = "[A-Z][a-zA-Z ]+", message = "City/locality is required, must start with capital letter and can only contain alphabet characters and spaces.")
	private String cityLocality;
	
	@Pattern(regexp = "[A-Z][a-zA-Z ]+", message = "State/country/region is required, must start with capital letter and can only contain alphabet characters and spaces.")
	private String stateCountyRegion;
	
	@Pattern(regexp="(AF|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BO|BA|BW|BV|BR|IO|BN|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GG|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IM|IL|IT|JM|JP|JE|JO|KZ|KE|KI|KP|KR|KR|KW|KG|LA|LV|LB|LS|LR|LY|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|ME|MS|MA|MZ|MM|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|\r\n" + 
			"RE|RO|RU|RU|RW|SH|KN|LC|PM|VC|VC|VC|WS|SM|ST|SA|SN|RS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|SS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VE|VN|VN|VG|VI|WF|EH|YE|ZM|ZW)", message="Invalid country code")
	private String country;
	
	@Email(message = "Email not valid")
	private String emailAddress;
}
