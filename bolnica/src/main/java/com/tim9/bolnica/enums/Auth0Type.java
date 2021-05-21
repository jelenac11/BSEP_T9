package com.tim9.bolnica.enums;

public enum Auth0Type {
	
	api_limit("The maximum number of requests to the Authentication or Management APIs in given time has reached."), 
	du("User has been deleted"),
	f("Failed Login"),
	fcp("Failed Change Password"),
	fcpr("Failed Change Password Request"),
	fcu("Failed to change username"),
	fd("Failed to generate delegation token"),
	feacft("Failed to exchange authorization code for Access Token"),
	feccft("Failed exchange of Access Token for a Client Credentials Grant"),
	fepft("Failed exchange of Password for Access Token"),
	fertft("Failed Exchange of Refresh Token for Access Token"),
	ferrt("Failed Exchange of Rotating Refresh Token"),
	flo("User logout failed"),
	fn("Failed to send email notification"),
	fp("Failed Login (Incorrect Password)"),
	fsa("Failed Silent Auth"),
	fu("Failed Login (Invalid Email/Username)"),
	fv("Failed to send verification email"),
	fvr("Failed to process verification email request"),
	limit_mu("An IP address is blocked with 100 failed login attempts using different usernames, all with incorrect passwords in 24 hours, or 50 sign-up attempts per minute from the same IP address."),
	limit_wc("An IP address is blocked with 10 failed login attempts into a single account from the same IP address."),
	limit_sul("A user is temporarily prevented from logging in because more than 20 logins per minute occurred from the same IP address"),
	s("Successful login event."),
	sapi("Success API Operation"),
	sce("Success Change Email"),
	scp("Success Change Password"),
	scpr("Success Change Password Request"),
	scu("Success Change Username"),
	ss("Success Signup"),
	fs("Failed Signup"),
	sd("Success Delegation"),
	sdu("User successfully deleted"),
	seacft("Successful exchange of authorization code for Access Token"),
	seccft("Successful exchange of Access Token for a Client Credentials Grant"),
	sede("Successful exchange of device code for Access Token"),
	sepft("Successful exchange of Password for Access Token"),
	sertft("Successful exchange of Refresh Token for Access Token"),
	si("Successfully accepted a user invitation"),
	srrt("Successfully revoked a Refresh Token"),
	slo("User successfully logged out"),
	ssa("Success Silent Auth"),
	sv("Success Verification Email"),
	svr("Success Verification Email Request"),
	w("Warnings During Login");
	
	private String value;

	Auth0Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
