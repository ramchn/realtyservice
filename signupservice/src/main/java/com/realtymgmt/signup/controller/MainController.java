package com.realtymgmt.signup.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.realtymgmt.signup.entity.Access;
import com.realtymgmt.signup.entity.Contact;
import com.realtymgmt.signup.entity.User;
import com.realtymgmt.signup.entity.Owner;
import com.realtymgmt.signup.entity.Tenant;
import com.realtymgmt.signup.repository.AccessRepository;
import com.realtymgmt.signup.repository.ContactRepository;
import com.realtymgmt.signup.repository.UserRepository;
import com.realtymgmt.signup.repository.OwnerRepository;
import com.realtymgmt.signup.repository.TenantRepository;

// comment added
@RestController 
public class MainController {
	
  @Autowired 
  private TenantRepository tenantRepository;
  
  @Autowired
  private OwnerRepository ownerRepository;
  
  @Autowired
  private ContactRepository contactRepository;
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private AccessRepository accessRepository;
  
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  //unauthenticated index
  @GetMapping("/index")
  public String gindex() {		
	return "GET Index";
  }
  
  @PostMapping("/index")
  public String pindex() {		
	return "POST Index";
  }
  
  //sign up services		 
  // request parameter - user type (owner or contact or tenant), 
  // name, email, password, services offered (if contact), 
  // owner email (if tenant)
  @PostMapping("/signup")
  public User createUser(@RequestParam String usertype, 
		  					@RequestParam String name, 
		  					@RequestParam String email, 
		  					@RequestParam String password, 
		  					@RequestParam String services, 
		  					@RequestParam String owneremail) {
	  	  
	  User u = new User();
	  u.setEmailAddress(email);
	  u.setUserPassword(new BCryptPasswordEncoder().encode(password));
	  userRepository.save(u);
	  	  	  
	  if(usertype.equals("contact")) {		
		  Contact c = new Contact();
		  c.setContactName(name);
		  c.setServicesOffered(services);
		  c.setUser(u);
		  contactRepository.save(c);
		  
		  Access a = new Access();
		  a.setAuthority("ROLE_CONTACT");
		  a.setUser(u);
		  accessRepository.save(a);
		  
	  } else if (usertype.equals("owner")) {
		  Owner o = new Owner();
		  o.setOwnerName(name);
		  o.setUser(u);
		  ownerRepository.save(o);
		  
		  Access a = new Access();
		  a.setAuthority("ROLE_OWNER");
		  a.setUser(u);
		  accessRepository.save(a);
		  
	  } else if (usertype.equals("tenant")) {
		  
		  Owner o = jdbcTemplate.queryForObject("select owner_id from owner where user_email_address = ?", new Object[]{owneremail}, (rs, rowNum) -> 
			new Owner(rs.getInt("owner_id")));
		
		  Optional<Owner> opt = ownerRepository.findById(Integer.valueOf(o.getOwnerId()));
		  
		  Tenant t = new Tenant();
		  t.setTenantName(name);
		  t.setUser(u);
		  t.setOwner(opt.isPresent() ? opt.get() : new Owner());	
		  tenantRepository.save(t);
		  
		  Access a = new Access();
		  a.setAuthority("ROLE_TENANT");
		  a.setUser(u);
		  accessRepository.save(a);
		  
	  }
	  
	  return u;
  }  
    
}
