package com.tamkosoft.primerealty.propertyinformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tamkosoft.primerealty.common.PrimeRealtyDao;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.CountryId;
import com.tamkosoft.primerealty.common.pojo.PersonId;
import com.tamkosoft.primerealty.common.pojo.PropertyInformation;
import com.tamkosoft.primerealty.propertyinformation.pojo.PersonPropertyInformationId;
import com.tamkosoft.primerealty.propertyinformation.pojo.PropInfoPersonWithId;
import com.tamkosoft.primerealty.propertyinformation.pojo.PropertyInformationDeleted;
import com.tamkosoft.primerealty.propertyinformation.pojo.PropertyInformationId;
import com.tamkosoft.primerealty.propertyinformation.pojo.PropertyInformationWithId;


@RestController 
public class PropertyinformationService {

	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
    private PropertyinformationDao propertyinformationDao;
	
	@Autowired
    private PrimeRealtyDao primeRealtyDao;
		
	// create property information	
	@GetMapping("/countries")
	private List<Map<String, Object>> getCountries() {
	  
		return primeRealtyDao.getCountries();
	}
	
	@PostMapping("/statecodesbycountry")
	private List<Map<String, Object>> getStateCodes(@Valid @RequestBody CountryId countryId) {
		
		return primeRealtyDao.getStateCodes(countryId.getCountryId());
	}
	
	@GetMapping("/propertytypes")
	private List<Map<String, Object>> getPropertyTypes() {
		  
		return propertyinformationDao.getPropertyTypes();
	}
	
	@PostMapping("/createpropertyinformation")
	private Map<String, Object> createPropertyInformation(@Valid @RequestBody PropertyInformation propertyInformation) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(PropertyinformationService.class, "createPropertyInformation() -> propertyInformation: " + propertyInformation.getPropertyTypeId());
		
		// database entry
		resultMap = propertyinformationDao.createPropertyInformation(propertyInformation);
		
		if (resultMap.containsKey("idAddress") && resultMap.containsKey("idPropertyInformation")) { // db entry success		

			primeRealtyLogger.debug(PropertyinformationService.class, "createPropertyInformation() -> database entry: " + resultMap);			
			
			resultMap.put("message", "property information created successfully!");	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create property information, please try after sometime");
		}
		
		return resultMap;
	}
	
	@PostMapping("/editpropertyinformation")
	private Map<String, Object> editPropertyInformation(@Valid @RequestBody PropertyInformationWithId propertyInformation) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(PropertyinformationService.class, "editPropertyInformation() -> propertyInformation: " + propertyInformation.getPropertyInformationId());
		
		// database entry
		resultMap = propertyinformationDao.editPropertyInformation(propertyInformation);
		
		if (resultMap.containsKey("updatedAddress") && resultMap.containsKey("updatedPropertyInformation")) { // db update success		

			primeRealtyLogger.debug(PropertyinformationService.class, "editPropertyInformation() -> database entry: " + resultMap);			
			
			resultMap.put("message", "property information updated successfully!");	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to update property information, please try after sometime");
		}
		
		return resultMap;
	}
	
	@PostMapping("/propinfoperson")
	private Boolean isPropInfoPerson(@Valid @RequestBody PersonId personId) {
		
		return propertyinformationDao.isPropInfoPerson(personId.getPersonId());
			
	}
	
	@PostMapping("/deletepropertyinformation")
	private Map<String, Object> deletePropertyInformation(@Valid @RequestBody PropertyInformationDeleted propertyInformationDeleted) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(PropertyinformationService.class, "deletePropertyInformation() -> propertyInformationDeleted id: " + propertyInformationDeleted.getPropertyInformationId());
		
		// database entry
		resultMap = propertyinformationDao.deletePropertyInformation(propertyInformationDeleted.getPropertyInformationId(), propertyInformationDeleted.getDeletedReason(), propertyInformationDeleted.getPersonId());
		
		if (resultMap.containsKey("idDeleted")) { // db entry success		

			primeRealtyLogger.debug(PropertyinformationService.class, "deletePropertyInformation() -> database entry: " + resultMap);			
			
			resultMap.put("message", "property information deleted successfully!");	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to delete property information, please try after sometime");
		}
		
		return resultMap;
	}
	
	@PostMapping("/propertyinformation")
	private Map<String, Object> getPropertyInformation(@Valid @RequestBody PropertyInformationId propertyInformationId) {
		
		return propertyinformationDao.getPropertyInformation(propertyInformationId.getPropertyInformationId());
		
	}
	
	@PostMapping("/propertyinformationsbyperson")
	private List<Map<String, Object>> getPropertyInformationsByPerson(@Valid @RequestBody PersonId personId) {
		
		return propertyinformationDao.getPropertyInformationsByPerson(personId.getPersonId());
		
	}
	
	@PostMapping("/livetenants")
	private List<Map<String, Object>> liveTenantsForProperty(@Valid @RequestBody PropertyInformationId propertyInformationId) {
		
		return propertyinformationDao.getLiveTenantsForProperty(propertyInformationId.getPropertyInformationId());
		
	}
	
	@PostMapping("/assignedtenants")
	private List<Map<String, Object>> assignedTenantsForProperty(@Valid @RequestBody PropertyInformationId propertyInformationId) {
		
		return propertyinformationDao.getAssignedTenantsForProperty(propertyInformationId.getPropertyInformationId());
		
	}
	
	@PostMapping("/livepropmngrs")
	private List<Map<String, Object>> getLivePropMngrsForPerson(@Valid @RequestBody PersonPropertyInformationId personPropertyInformationId) {
		
		return propertyinformationDao.getLivePropMngrsForPerson(personPropertyInformationId.getPropertyInformationId(), personPropertyInformationId.getPersonId());
		
	}
	
	@PostMapping("/assignedpropmngr")
	private Map<String, Object> assignedPropMngrForProperty(@Valid @RequestBody PropertyInformationId propertyInformationId) {
		
		return propertyinformationDao.getAssignedPropMngrForProperty(propertyInformationId.getPropertyInformationId());
		
	}
	
	@PostMapping("/liveowners")
	private List<Map<String, Object>> liveOwnerForPerson(@Valid @RequestBody PersonPropertyInformationId personPropertyInformationId) {
		
		return propertyinformationDao.getLiveOwnersForPerson(personPropertyInformationId.getPropertyInformationId(), personPropertyInformationId.getPersonId());
		
	}
	
	@PostMapping("/assignedowner")
	private Map<String, Object> assignedOwnerForProperty(@Valid @RequestBody PropertyInformationId propertyInformationId) {
		
		return propertyinformationDao.getAssignedOwnerForProperty(propertyInformationId.getPropertyInformationId());
		
	}
	
	@PostMapping("/updatetenantforproperty")
	private Map<String, Object> updateTenantForProperty(@Valid @RequestBody  PropInfoPersonWithId propInfoPerson) {
				
		Map<String, Object> resultMap = propertyinformationDao.updateTenantForProperty(propInfoPerson.getPersonId(), propInfoPerson.getPropertyInformationId(), propInfoPerson.getStartDate(), propInfoPerson.getEndDate(), propInfoPerson.getUpdateByPersonId());
	
		if(resultMap.containsKey("updated")) { // db entry success
			
			primeRealtyLogger.debug(PropertyinformationService.class, "updateTenantForProperty() -> database entry: " + resultMap);			
			
			resultMap.put("updatemessage", "updated successfully!");	
			
		}
		
		if(!resultMap.containsKey("updatemessage")) {
			resultMap.put("updatemessage", "unable to update, please try after sometime");
		}
		
		return resultMap;
	}
	
	@PostMapping("/updatepropmngrforproperty")
	private Map<String, Object> updatePropMngrForProperty(@Valid @RequestBody  PropInfoPersonWithId propInfoPerson) {
				
		Map<String, Object> resultMap = propertyinformationDao.updatePropMngrForProperty(propInfoPerson.getPersonId(), propInfoPerson.getPropertyInformationId(), propInfoPerson.getStartDate(), propInfoPerson.getEndDate(), propInfoPerson.getUpdateByPersonId());
	
		if(resultMap.containsKey("updated")) { // db entry success
			
			primeRealtyLogger.debug(PropertyinformationService.class, "updatePropMngrForProperty() -> database entry: " + resultMap);			
			
			resultMap.put("updatemessage", "updated successfully!");	
			
		}
		
		if(!resultMap.containsKey("updatemessage")) {
			resultMap.put("updatemessage", "unable to update, please try after sometime");
		}
		
		return resultMap;
	}
	
	@PostMapping("/updateownerforproperty")
	private Map<String, Object> updateOwnerForProperty(@Valid @RequestBody  PropInfoPersonWithId propInfoPerson) {
				
		Map<String, Object> resultMap = propertyinformationDao.updateOwnerForProperty(propInfoPerson.getPersonId(), propInfoPerson.getPropertyInformationId(), propInfoPerson.getStartDate(), propInfoPerson.getEndDate(), propInfoPerson.getUpdateByPersonId());
	
		if(resultMap.containsKey("updated")) { // db entry success
			
			primeRealtyLogger.debug(PropertyinformationService.class, "updateOwnerForProperty() -> database entry: " + resultMap);			
			
			resultMap.put("updatemessage", "updated successfully!");	
			
		}
		
		if(!resultMap.containsKey("updatemessage")) {
			resultMap.put("updatemessage", "unable to update, please try after sometime");
		}
		
		return resultMap;
	}
}
