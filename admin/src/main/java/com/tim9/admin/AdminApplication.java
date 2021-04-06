package com.tim9.admin;


import java.util.ArrayList;
import java.util.List;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tim9.admin.model.Authority;
import com.tim9.admin.model.User;
import com.tim9.admin.services.AuthorityService;
import com.tim9.admin.services.CertificateService;
import com.tim9.admin.services.UserService;


@SpringBootApplication
public class AdminApplication {
    
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext appContext = SpringApplication.run(AdminApplication.class, args);
		
		UserService service = appContext.getBean(UserService.class);
		AuthorityService authService = appContext.getBean(AuthorityService.class);
		
		Authority a = new Authority();
		a.setName("ROLE_SUPER_ADMIN");
		authService.create(a);
		
	    User u = new User("jelena@gmail.com", "sifra123", "Jelena", "Cupac");
	    List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(a);
	    u.setAuthorities(authorities);
	    try {
	    	if (service.findByEmail(u.getEmail()) == null)
	    		service.create(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    CertificateService cert = appContext.getBean(CertificateService.class);
	    cert.deleteAllExceptRoot();
	}

}