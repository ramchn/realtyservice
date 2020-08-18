package com.tamkosoft.primerealty.myinfo;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.PrimeRealtyDao;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.ServiceProvider;
import com.tamkosoft.primerealty.common.pojo.User;
import com.tamkosoft.primerealty.myinfo.pojo.ServiceProviderWithId;

@Component("myinfoDao")
public class MyinfoDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PrimeRealtyDao primeRealtyDao;
		
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	public Boolean isPasswordDefault(String EmailAddress) {
		
		Boolean isPasswordDefault = false;
		String EncPassword = new String(Base64.getEncoder().encode("password".getBytes()));
		
		try {
			jdbcTemplate.queryForObject(CHECK_DEAULT_PASSWORD_QUERY, new Object[] {EmailAddress, EncPassword}, String.class);
			isPasswordDefault = true;
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(MyinfoDao.class, "isPasswordDefault() ->  - user (email: " + EmailAddress + ") already reset password  - EmptyResultDataAccessException : " + dae.getMessage());		
			
		}
		return isPasswordDefault;
		
	}
	
	
	public Map<String, Object> passwordReset(User user) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		
		try {
			
			String EncPassword = user.getUserPassword() != null ? new String(Base64.getEncoder().encode(user.getUserPassword().getBytes())) : new String(Base64.getEncoder().encode("password".getBytes()));
			
			// create service provider			
			Object[] userParams = new Object[] {user.getFirstName(), user.getMiddleName(), user.getLastName(), EncPassword, user.getEmailAddress()};
			
			Number updatedPasswordReset  = jdbcTemplate.update(USER_PASSWORD_UPDATE, userParams);
			
			if (updatedPasswordReset.intValue() == 1) {
				
				resultMap.put("updatedPasswordReset", updatedPasswordReset);
			}
			
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to reset password - due to server issue");
			primeRealtyLogger.error(MyinfoDao.class, "passwordReset() -> DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	public Map<String, Object> getTenantProperty(Number PersonId) {
		
		Map<String, Object> tenantProperty = new HashMap<String, Object>();
		
		try {
			tenantProperty = jdbcTemplate.queryForMap(TENANT_PROPERTY_QUERY, new Object[] {PersonId});
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(MyinfoDao.class, "getTenantProperty() ->  - no prop info for this tenant (id: " + PersonId + ") - EmptyResultDataAccessException : " + dae.getMessage());		
			
		}
		
		return tenantProperty;
		
	}
	
	public List<Map<String, Object>> getServiceCategories() {
		
		return jdbcTemplate.queryForList(SERVICE_CATEGORIES_QUERY);
		
	}
	
	public Map<String, Object> getServiceProviderByPerson(Number PersonId) {
		
		Map<String, Object> serviceProvider = new HashMap<String, Object>();
		
		try {
			serviceProvider = jdbcTemplate.queryForMap(SERVICE_PROVIDER_QUERY, new Object[] {PersonId});
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(MyinfoDao.class, "getServiceProviderByPerson() ->  - no service provider info for this service provider (id: " + PersonId + ") - EmptyResultDataAccessException : " + dae.getMessage());		
			
		}
		
		return serviceProvider;
		
	}
	
	public Map<String, Object> createServiceProvider(ServiceProvider serviceProvider) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		
		try {
			
			// create service provider			
			Map<String, Object> servprovParams = new HashMap<String, Object>();
			servprovParams.put("ServiceCategory_idServiceCategory", serviceProvider.getServiceCategoryId());
			servprovParams.put("YearsOfExperience", serviceProvider.getYearsOfExperience());
			servprovParams.put("AreasOfExpertise", serviceProvider.getAreasOfExpertise());
			servprovParams.put("AreaCoverage", serviceProvider.getAreaCoverage());
			servprovParams.put("Person_idPerson", serviceProvider.getPersonId());
		
			Number idServiceProvider  = primeRealtyDao.tableInsert("ServiceProvider",servprovParams);
			
			if (idServiceProvider.intValue() > 0) {
				
				resultMap.put("idServiceProvider", idServiceProvider);
			}
			
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to create service provider - due to server issue");
			primeRealtyLogger.error(MyinfoDao.class, "createServiceProvider() -> DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	public Map<String, Object> editServiceProvider(ServiceProviderWithId serviceProvider) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		
		try {
			
			// update service provider			
			Object[] servprovParams = {serviceProvider.getServiceCategoryId(), serviceProvider.getYearsOfExperience(), serviceProvider.getAreasOfExpertise(), serviceProvider.getAreaCoverage(), serviceProvider.getPersonId(), serviceProvider.getServiceProviderId()};
			Number updatedServProv  = jdbcTemplate.update(UPDATE_SERVICE_PROVIDER, servprovParams);
			
			if (updatedServProv.intValue() == 1) {
				resultMap.put("updatedServProv", updatedServProv);
			}
			
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to edit service provider - due to server issue");
			primeRealtyLogger.error(MyinfoDao.class, "editServiceProvider() -> DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	private static String CHECK_DEAULT_PASSWORD_QUERY = "SELECT FirstName FROM User WHERE EmailAddress = ? AND UserPassword = ?";
	private static String USER_PASSWORD_UPDATE = "UPDATE User SET FirstName = ?, MiddleName = ?, LastName = ?, UserPassword = ? WHERE EmailAddress = ?";
	private static String SERVICE_CATEGORIES_QUERY = "SELECT idServiceCategory, ServiceCategory FROM ServiceCategory";
	private static String SERVICE_PROVIDER_QUERY = "SELECT ServiceProvider.idServiceProvider, ServiceCategory.idServiceCategory, ServiceCategory.ServiceCategory, ServiceProvider.YearsOfExperience, ServiceProvider.AreasOfExpertise, ServiceProvider.AreaCoverage FROM ServiceProvider, ServiceCategory, Person WHERE ServiceProvider.Person_idPerson = Person.idPerson AND ServiceProvider.ServiceCategory_idServiceCategory = ServiceCategory.idServiceCategory AND Person.idPerson = ?";
	private static String UPDATE_SERVICE_PROVIDER = "UPDATE ServiceProvider SET ServiceCategory_idServiceCategory = ?, YearsOfExperience = ?, AreasOfExpertise = ?, AreaCoverage = ?, Person_idPerson = ? WHERE idServiceProvider = ?";
	private static String TENANT_PROPERTY_QUERY = "SELECT Address.Address1, Address.Address2, Address.CityName, State.StateCode FROM PropertyInformationPerson, PropertyInformation, Person, Address, State WHERE PropertyInformationPerson.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformation.Address_idAddress = Address.idAddress AND Address.State_idState = State.idState AND PropertyInformationPerson.Person_idPerson = ? AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = 2 AND PropertyInformation.idPropertyInformation NOT IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationDeleted)";
	
}
