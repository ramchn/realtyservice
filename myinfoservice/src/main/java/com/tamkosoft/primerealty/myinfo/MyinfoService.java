package com.tamkosoft.primerealty.myinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.EmailAddress;
import com.tamkosoft.primerealty.common.pojo.PersonId;
import com.tamkosoft.primerealty.common.pojo.ServiceProvider;
import com.tamkosoft.primerealty.common.pojo.User;
import com.tamkosoft.primerealty.myinfo.pojo.ServiceProviderWithId;

@RestController 
public class MyinfoService {

	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
    private MyinfoDao myinfoDao;
		
	@PostMapping("/passworddefault")
	private Boolean isPasswordDefault(@Valid @RequestBody EmailAddress emailAddress) {
		
		return myinfoDao.isPasswordDefault(emailAddress.getEmailAddress());
		
	}
	
	@PostMapping("/passwordreset")
	private Map<String, Object> passwordReset(@Valid @RequestBody User user) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(MyinfoService.class, "passwordReset() -> user: " + user.getEmailAddress());
		
		// database entry
		resultMap = myinfoDao.passwordReset(user);
		
		if (resultMap.containsKey("updatedPasswordReset")) { // db update success		

			primeRealtyLogger.debug(MyinfoService.class, "passwordReset() -> database entry: " + resultMap);			
			
			resultMap.put("message", "service password resetted successfully!");	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to reset password, please try after sometime");
		}
		
		return resultMap;
	}
	
	@PostMapping("/tenantproperty")
	private Map<String, Object> getTenantProperty(@Valid @RequestBody PersonId personId) {
		
		return myinfoDao.getTenantProperty(personId.getPersonId());
		
	}
	
	@GetMapping("/servicecategories")
	private List<Map<String, Object>> getServiceCategories() {
		
		return myinfoDao.getServiceCategories();
		
	}
	
	@PostMapping("/serviceproviderbyperson")
	private Map<String, Object> getServiceProviderByPerson(@Valid @RequestBody PersonId personId) {
		
		return myinfoDao.getServiceProviderByPerson(personId.getPersonId());
		
	}
	
	@PostMapping("/createserviceprovider")	
	private Map<String, Object> createServiceProvider(@Valid @RequestBody ServiceProvider serviceProvider) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(MyinfoService.class, "createServiceProvider() -> serviceProvider: " + serviceProvider.getPersonId());
		
		// database entry
		resultMap = myinfoDao.createServiceProvider(serviceProvider);
		
		if (resultMap.containsKey("idServiceProvider")) { // db update success		

			primeRealtyLogger.debug(MyinfoService.class, "createServiceProvider() -> database entry: " + resultMap);			
			
			resultMap.put("message", "service provider created successfully!");	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create service provider, please try after sometime");
		}
		
		return resultMap;
		
	}
	
	@PostMapping("/editserviceprovider")	
	private Map<String, Object> editServiceProvider(@Valid @RequestBody ServiceProviderWithId serviceProvider) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(MyinfoService.class, "editServiceProvider() -> serviceProvider: " + serviceProvider.getServiceProviderId());
		
		// database entry
		resultMap = myinfoDao.editServiceProvider(serviceProvider);
		
		if (resultMap.containsKey("updatedServProv")) { // db update success		

			primeRealtyLogger.debug(MyinfoService.class, "editServiceProvider() -> database entry: " + resultMap);			
			
			resultMap.put("message", "service provider updated successfully!");	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to update service provider, please try after sometime");
		}
		
		return resultMap;
		
	}
	
}
