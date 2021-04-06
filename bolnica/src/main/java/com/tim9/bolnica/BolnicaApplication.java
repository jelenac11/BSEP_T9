package com.tim9.bolnica;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tim9.bolnica.model.Authority;
import com.tim9.bolnica.model.User;
import com.tim9.bolnica.services.AuthorityService;
import com.tim9.bolnica.services.UserService;

@SpringBootApplication
public class BolnicaApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext appContext = SpringApplication.run(BolnicaApplication.class, args);
		
		UserService service = appContext.getBean(UserService.class);
		AuthorityService authService = appContext.getBean(AuthorityService.class);
		
		Authority a = new Authority();
		a.setName("ROLE_ADMIN");
		authService.create(a);
		
	    User u = new User("aleksa@gmail.com", "wH090210.", "Aleksa", "Goljovic");
	    List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(a);
	    u.setAuthorities(authorities);
	    try {
	    	if (service.findByEmail(u.getEmail()) == null)
	    		service.create(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
