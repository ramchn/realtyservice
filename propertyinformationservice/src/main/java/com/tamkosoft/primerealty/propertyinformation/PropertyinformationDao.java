package com.tamkosoft.primerealty.propertyinformation;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.PrimeRealtyDao;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.PropertyInformation;
import com.tamkosoft.primerealty.propertyinformation.pojo.PropertyInformationWithId;

@Component("propertyinformationDao")
public class PropertyinformationDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	PrimeRealtyDao primeRealtyDao;
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	
	public List<Map<String, Object>> getPropertyTypes() {
		
		return jdbcTemplate.queryForList(PROPERTY_TYPES_LIST_QUERY);
		
	}
	
	public Map<String, Object> createPropertyInformation(PropertyInformation propertyInformation) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		
		try {
			
			// create address			
			Map<String, Object> addressParams = new HashMap<String, Object>();	
			addressParams.put("Address1", propertyInformation.getAddress1());
			addressParams.put("Address2", propertyInformation.getAddress2());
			addressParams.put("Address3", propertyInformation.getAddress3());
			addressParams.put("Zip", propertyInformation.getZip());
			addressParams.put("CityName", propertyInformation.getCityName());
			addressParams.put("State_idState", propertyInformation.getStateId());
			addressParams.put("Country_idCountry", propertyInformation.getCountryId());
			addressParams.put("AddressType_idAddressType", 4); // property address type is always 4 (hard coded)
			
			Number idAddress = primeRealtyDao.tableInsert("Address",addressParams);
			
			if(idAddress.intValue() >= 0) {				
				resultMap.put("idAddress", idAddress);	
				
				// create property information
				Map<String, Object> propInfoParams = new HashMap<String, Object>();
				propInfoParams.put("SquareFeet", propertyInformation.getSquareFeet());
				propInfoParams.put("Beds", propertyInformation.getBeds());				
				propInfoParams.put("Baths", propertyInformation.getBaths());
				propInfoParams.put("Stories", propertyInformation.getStories());
				propInfoParams.put("Lotsize", propertyInformation.getLotSize());
				propInfoParams.put("YearBuilt", propertyInformation.getYearBuilt());
				propInfoParams.put("YearRenovated", propertyInformation.getYearRenovated());
				propInfoParams.put("APN", propertyInformation.getApn());
				propInfoParams.put("Community", propertyInformation.getCommunity());
				propInfoParams.put("HOADues", propertyInformation.getHoaDues());
				propInfoParams.put("PropertyType_idPropertyType", propertyInformation.getPropertyTypeId());
				propInfoParams.put("Address_idAddress", idAddress);
				propInfoParams.put("CreatePerson_idPerson", propertyInformation.getPersonId());
				propInfoParams.put("CreateDate", new Date());				
				
				Number idPropertyInformation = primeRealtyDao.tableInsert("PropertyInformation",propInfoParams);
				
				if(idPropertyInformation.intValue() > 0) {
					resultMap.put("idPropertyInformation", idPropertyInformation);			
					
				}	else {					
					//rollback address if it is there
					jdbcTemplate.update(DELETE_ADDRESS, new Object[]{idAddress});					
				}				
			}		
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to create property information - due to server issue");
			primeRealtyLogger.error(PropertyinformationDao.class, "createPropertyInformation() -> DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	public Map<String, Object> editPropertyInformation(PropertyInformationWithId propertyInformation) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		
		try {
			
			// update address			
			Object[] addressParams = {propertyInformation.getAddress1(), propertyInformation.getAddress2(), propertyInformation.getAddress3(), propertyInformation.getZip(), propertyInformation.getCityName(), propertyInformation.getStateId(), propertyInformation.getCountryId(), propertyInformation.getAddressId()};
			Number updatedAddress  = jdbcTemplate.update(UPDATE_ADDRESS, addressParams);
			
			if (updatedAddress.intValue() == 1) {
				resultMap.put("updatedAddress", updatedAddress);
				
				// update property information			
				Object[] propInfoParams = {propertyInformation.getSquareFeet(), propertyInformation.getBeds(), propertyInformation.getBaths(), propertyInformation.getStories(), propertyInformation.getLotSize(), propertyInformation.getYearBuilt(), propertyInformation.getYearRenovated(), propertyInformation.getApn(), propertyInformation.getCommunity(), propertyInformation.getHoaDues(), propertyInformation.getPropertyTypeId(),	propertyInformation.getPersonId(), new Date(), propertyInformation.getPropertyInformationId()};
				Number updatedPropertyInformation  = jdbcTemplate.update(UPDATE_PROPERTYINFORMATION, propInfoParams);
				
				if (updatedPropertyInformation.intValue() == 1) {
					resultMap.put("updatedPropertyInformation", updatedPropertyInformation);			
					
				}	else {					
					//rollback address if it is there								
				}
			}
					
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to edit property information - due to server issue");
			primeRealtyLogger.error(PropertyinformationDao.class, "editPropertyInformation() -> DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	public Boolean isPropInfoPerson(Number PersonId) {
		
		Boolean IsPropInfoPerson = false;
		
		try {
			jdbcTemplate.queryForObject(PERSON_IN_PROPINFOPERSON_QUERY, new Object[] {PersonId}, Number.class);
			IsPropInfoPerson = true;
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "isRootPerson() -> (person exist in pip) EmptyResultDataAccessException - " + dae.getMessage());			
		}
		return IsPropInfoPerson;
	}
	
	public Map<String, Object> deletePropertyInformation(Number propertyInformationId, String deletedReason, Number personId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		
		try {
						
			// make an entry in deleted table			
			Map<String, Object> deletedParams = new HashMap<String, Object>();	
			deletedParams.put("PropertyInformation_idPropertyInformation", propertyInformationId);
			deletedParams.put("DeletedReason", deletedReason);
			deletedParams.put("DeletedBy_idPerson", personId);
			deletedParams.put("DeletedDate", new Date());
						
			Number idDeleted = primeRealtyDao.tableInsert("PropertyInformationDeleted",deletedParams);
			
			if(idDeleted.intValue() > 1) {
				resultMap.put("idDeleted", idDeleted);
			}
			
					
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to delete property information - due to server issue");
			primeRealtyLogger.error(PropertyinformationDao.class, "deletePropertyInformation() -> DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	public Map<String, Object> getPropertyInformation(Number PropertyInformationId) {
		
		Map<String, Object> propertyInformation = new HashMap<String, Object>();
		
		try {
			propertyInformation = jdbcTemplate.queryForMap(PROPERTY_INFORMATION_QUERY, new Object[] {PropertyInformationId});
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getPropertyInformation() -> (no property information for this id " + PropertyInformationId + ") EmptyResultDataAccessException - " + dae.getMessage());			
		}
		
		return propertyInformation;
	}
	
	public List<Map<String, Object>> getPropertyInformationsByPerson(Number PersonId) {
		
		// property infos by owner/property manager
		List<Map<String, Object>> properties1 = new ArrayList<Map<String, Object>>();
		try {
					
			properties1 = jdbcTemplate.queryForList(PROPERTIES_BYOWNERPM_QUERY, new Object[] {PersonId});
			
			for(Map<String, Object> property : properties1) {
				property.put("Tenants", getAssignedTenantsForProperty((Number)property.get("idPropertyInformation")));
			}
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getPropertyInformationsByPerson() -> (owner/property manager - property infos) DataAccessException - " + dae.getMessage());			
		}
		
		if(!properties1.isEmpty()) {
			return properties1;
		}
		
		// property infos by owner
		List<Map<String, Object>> properties2 = new ArrayList<Map<String, Object>>();
		try {					
			properties2 = jdbcTemplate.queryForList(PROPERTIES_BYOWNER_QUERY, new Object[] {PersonId});	
			
			for(Map<String, Object> property : properties2) {
				property.put("Tenants", getAssignedTenantsForProperty((Number)property.get("idPropertyInformation")));
				Map<String, Object> propertyManager = getAssignedPropMngrForProperty((Number)property.get("idPropertyInformation"));
				if(!propertyManager.isEmpty()) {
					property.put("PropertyManager", propertyManager);
				}
			}
		
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getPropertyInformationsByPerson() -> (owner - property infos) DataAccessException - " + dae.getMessage());			
		}
		
		if(!properties2.isEmpty()) {
			return properties2;
		}
		
		// property infos by property manager
		List<Map<String, Object>> properties3 = new ArrayList<Map<String, Object>>();		
		try {
			properties3 = jdbcTemplate.queryForList(PROPERTIES_BYPROPMNGR_QUERY, new Object[] {PersonId});	
			
			for(Map<String, Object> property : properties3) {
				property.put("Tenants", getAssignedTenantsForProperty((Number)property.get("idPropertyInformation")));
				Map<String, Object> owner = getAssignedOwnerForProperty((Number)property.get("idPropertyInformation"));
				if(!owner.isEmpty()) {
					property.put("Owner", owner);
				}
			}
		
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getPropertyInformationsByPerson() -> (property manger - property infos) DataAccessException - " + dae.getMessage());			
		}
		
		if(!properties3.isEmpty()) {
			return properties3;
		}
		
		// property infos by prop info person
		List<Map<String, Object>> properties4 = new ArrayList<Map<String, Object>>();
		try {					
			properties4 = jdbcTemplate.queryForList(PROPERTIES_BYPROPINFOPERSON_QUERY, new Object[] {PersonId});		
			
			for(Map<String, Object> property : properties4) {
				property.put("Tenants", getAssignedTenantsForProperty((Number)property.get("idPropertyInformation")));
			}
		
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getPropertyInformationsByPerson() -> (prop info person - property infos) DataAccessException - " + dae.getMessage());			
		}
		
		if(!properties4.isEmpty()) {
			return properties4;
		}
		
		return new ArrayList<Map<String, Object>>();
	}
	
	public List<Map<String, Object>> getAssignedTenantsForProperty(Number PropertyInformationId) {
		
		return jdbcTemplate.queryForList(ASSIGNED_PROPERTYTENANTS_QUERY, new Object[] {PropertyInformationId});
	}
	
	public Map<String, Object> getAssignedPropMngrForProperty(Number PropertyInformationId) {
		
		Map<String, Object> assignedPropMngr = new HashMap<String, Object>();
		
		try {
			assignedPropMngr = jdbcTemplate.queryForMap(ASSIGNED_PROPERTYPROPMNGR_BYOWNER_QUERY, new Object[] {PropertyInformationId});
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getAssignedPropMngrForProperty() -> (no property manager assigned) EmptyResultDataAccessException - " + dae.getMessage());			
		}
		
		return assignedPropMngr;
	}

	public Map<String, Object> getAssignedOwnerForProperty(Number PropertyInformationId) {
		
		Map<String, Object> assignedOwner = new HashMap<String, Object>();
		
		try {		
			assignedOwner = jdbcTemplate.queryForMap(ASSIGNED_PROPERTYOWNER_BYPROPMNGR_QUERY, new Object[] {PropertyInformationId});
		
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(PropertyinformationDao.class, "getAssignedOwnerForProperty() -> (no owner assigned) EmptyResultDataAccessException - " + dae.getMessage());			
		}
		
		return assignedOwner;
		
	}
	
	public List<Map<String, Object>> getLiveTenantsForProperty(Number PropertyInformationId) {
		
		return jdbcTemplate.queryForList(LIVE_PROPERTYTENANTS_QUERY, new Object[] {PropertyInformationId});
	}
	
	public List<Map<String, Object>> getLivePropMngrsForPerson(Number PropertyInformationId, Number PersonId) {
		
		// get the property managers for a given property id
		List<Map<String, Object>> PropMngrs = jdbcTemplate.queryForList(LIVE_PROPERTYPROPMGRS_BYOWNER_QUERY, new Object[] {PropertyInformationId});	
		
		// get the list of all properties associated with this person (owner or company owner)
		Set<Integer> OtherPropInfoIds = new HashSet<Integer>();
		List<Map<String, Object>> properties = jdbcTemplate.queryForList(PROPERTIES_BYOWNER_QUERY, new Object[] {PersonId});	
				
		// get the list of property ids other than given property id
		for(Map<String, Object> property : properties) {
			Number idPropInfo = (Number)property.get("idPropertyInformation");
			if(PropertyInformationId.intValue() != idPropInfo.intValue()) {
				OtherPropInfoIds.add(idPropInfo.intValue());
			}			
		}
		
		List<Map<String, Object>> OtherPropMngrs = new ArrayList<Map<String, Object>>();
		if(!OtherPropInfoIds.isEmpty()) {
			// get the property managers for property ids		
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids", OtherPropInfoIds);
			OtherPropMngrs = namedParameterJdbcTemplate.queryForList(LIVE_OTHER_PROPERTYPROPMNGRS_BYOWNER_QUERY, parameters);	
		}		
		// if the property managers from other property ids are available in a given property id then mark them as duplicate
		// if the property managers from other property ids are not available in a given property id then set start and end date as null
		for(int i = 0; i < PropMngrs.size(); i++) {
			Map<String, Object> PropMngr = PropMngrs.get(i);
			Number idPropInfoPerson = (Number)PropMngr.get("idPerson");
			
			for(Map<String, Object> OtherPropMngr : OtherPropMngrs) {
				Number idOtherPropInfoPerson = (Number)OtherPropMngr.get("idPerson");				
				if(idOtherPropInfoPerson.intValue() == idPropInfoPerson.intValue()) {
					OtherPropMngr.put("Duplicate", "YES");
				} 
			}
		}
				
		// finally add those property managers to the first property managers list (got for a given property id)
		for(Map<String, Object> OtherPropMngr : OtherPropMngrs) {
			if(!OtherPropMngr.containsKey("Duplicate")) {
				OtherPropMngr.put("StartDate", null);
				OtherPropMngr.put("EndDate", null);
				PropMngrs.add(OtherPropMngr);
			}
		}
		primeRealtyLogger.debug(PropertyinformationDao.class, "getLivePropMngrsForPerson() ->  - PropMngrs final : " + PropMngrs);	
				
		return PropMngrs;
	}

	public List<Map<String, Object>> getLiveOwnersForPerson(Number PropertyInformationId, Number PersonId) {
		
		// get the owners for a given property id
		List<Map<String, Object>> Owners = jdbcTemplate.queryForList(LIVE_PROPERTYOWNERS_BYPROPMNGR_QUERY, new Object[] {PropertyInformationId});	
		
		// get the list of all properties associated with this person (property manager)
		Set<Integer> OtherPropInfoIds = new HashSet<Integer>();
		List<Map<String, Object>> properties = jdbcTemplate.queryForList(PROPERTIES_BYPROPMNGR_QUERY, new Object[] {PersonId});	
				
		// get the list of property ids other than given property id
		for(Map<String, Object> property : properties) {
			Number idPropInfo = (Number)property.get("idPropertyInformation");
			if(PropertyInformationId.intValue() != idPropInfo.intValue()) {
				OtherPropInfoIds.add(idPropInfo.intValue());
			}			
		}
		List<Map<String, Object>> OtherOwners = new ArrayList<Map<String, Object>>();
		if(!OtherPropInfoIds.isEmpty()) {
			// get the owners for property ids		
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids", OtherPropInfoIds);
			OtherOwners = namedParameterJdbcTemplate.queryForList(LIVE_OTHER_PROPERTYOWNERS_BYPROPMNGR_QUERY, parameters);	
		}
		// if the owners from other property ids are available in a given property id then mark them as duplicate
		// if the owners from other property ids are not available in a given property id then set start and end date as null
		for(int i = 0; i < Owners.size(); i++) {
			Map<String, Object> Owner = Owners.get(i);
			Number idPropInfoPerson = (Number)Owner.get("idPerson");
			
			for(Map<String, Object> OtherOwner : OtherOwners) {
				Number idOtherPropInfoPerson = (Number)OtherOwner.get("idPerson");				
				if(idOtherPropInfoPerson.intValue() == idPropInfoPerson.intValue()) {
					OtherOwner.put("Duplicate", "YES");
				} 
			}
		}
				
		// finally add those property managers to the first property managers list (got for a given property id)
		for(Map<String, Object> OtherOwner : OtherOwners) {
			if(!OtherOwner.containsKey("Duplicate")) {
				OtherOwner.put("StartDate", null);
				OtherOwner.put("EndDate", null);
				Owners.add(OtherOwner);
			}
		}
		primeRealtyLogger.debug(PropertyinformationDao.class, "getLiveOwnersForPerson() ->  - Owners final : " + Owners);	
				
		return Owners;
	}
	
	public Map<String, Object> updateTenantForProperty(Number PersonId, Number PropertyInformationId, Date StartDate, Date EndDate, Number UpdateByPersonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {			
			// do the assignment
			resultMap = updatePropInfoPersonForProperty(new HashMap<String, Object>(), PersonId, PropertyInformationId, StartDate, EndDate, UpdateByPersonId);
						
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(PropertyinformationDao.class, "updatePropManagerForProperty() ->  - issue in updating property manager for property - DataAccessException : " + dae.getMessage());				
		
		}
				
		return resultMap;
	}
	
	public Map<String, Object> updatePropMngrForProperty(Number PersonId, Number PropertyInformationId, Date StartDate, Date EndDate, Number UpdateByPersonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// reset the startdate and enddate to null to existing assigned person before do the new assignment
			Map<String, Object> asignedPropMngr = getAssignedPropMngrForProperty(PropertyInformationId);
			
			// do the assignment
			resultMap = updatePropInfoPersonForProperty(asignedPropMngr, PersonId, PropertyInformationId, StartDate, EndDate, UpdateByPersonId);
						
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(PropertyinformationDao.class, "updatePropMngrForProperty() ->  - issue in updating property manager for property - DataAccessException : " + dae.getMessage());				
		
		}
				
		return resultMap;
	}
	
	public Map<String, Object> updateOwnerForProperty(Number PersonId, Number PropertyInformationId, Date StartDate, Date EndDate, Number UpdateByPersonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// reset the startdate and enddate to null to existing assigned person before do the new assignment
			Map<String, Object> asignedOwner = getAssignedOwnerForProperty(PropertyInformationId);
			
			// do the assignment
			resultMap = updatePropInfoPersonForProperty(asignedOwner, PersonId, PropertyInformationId, StartDate, EndDate, UpdateByPersonId);
						
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(PropertyinformationDao.class, "updateOwnerForProperty() ->  - issue in updating owner for property - DataAccessException : " + dae.getMessage());				
			
		}
			
		return resultMap;
	}
	
	public Map<String, Object> updatePropInfoPersonForProperty(Map<String, Object> asignedPerson, Number PersonId, Number PropertyInformationId, Date StartDate, Date EndDate, Number UpdateByPersonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
				
		try {			
			if(!asignedPerson.isEmpty()) {
				
				Number ResetUpdated = jdbcTemplate.update(RESET_DATE_PROP_INFO_PERSON, new Object[] {null, null, (Number)asignedPerson.get("idPropertyInformationPerson")});
				
				if(ResetUpdated.intValue() == 1) { // if reset successfull
					primeRealtyLogger.debug(PropertyinformationDao.class, "updatePropInfoPersonForProperty() ->  - resetting previous assignment if exist : " + ResetUpdated);	
				}
			}
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(PropertyinformationDao.class, "updatePropInfoPersonForProperty() ->  - issue in resetting previous assignment - DataAccessException : " + dae.getMessage());				
		}
				
		// check if the assignment is an insert or update
		Boolean recordExist = false;			
		try {
			jdbcTemplate.queryForMap(PROP_INFO_PERSON_QUERY, new Object[] {PersonId, PropertyInformationId});
			recordExist = true;
			
		} catch (EmptyResultDataAccessException dae) {
			// record not exist, insert a new record
			primeRealtyLogger.debug(PropertyinformationDao.class, "updatePropInfoPersonForProperty() ->  - person not exist in prop info person table, so inserting new record - DataAccessException : " + dae.getMessage());				
			
			Map<String, Object> propInfoPersonParams = new HashMap<String, Object>();
			propInfoPersonParams.put("StartDate", StartDate);
			propInfoPersonParams.put("EndDate", EndDate);
			propInfoPersonParams.put("UpdateBy_idPerson", UpdateByPersonId);
			propInfoPersonParams.put("UpdateDate", new Date());
			propInfoPersonParams.put("Person_idPerson", PersonId);
			propInfoPersonParams.put("PropertyInformation_idPropertyInformation", PropertyInformationId);			
			Number idPropInfoPerson = primeRealtyDao.tableInsert("PropertyInformationPerson",propInfoPersonParams);
			
			if(idPropInfoPerson.intValue() > 1) {
				resultMap.put("updated", idPropInfoPerson);
			}						
		}
		
		if(recordExist) { // record exist, so update it
			try {				
				Object[] PropInfoPersonParams = {StartDate, EndDate, UpdateByPersonId, new Date(), PersonId, PropertyInformationId};
				Number Updated  = jdbcTemplate.update(UPDATE_PROP_INFO_PERSON, PropInfoPersonParams);
				
				if(Updated.intValue() == 1) {					
					resultMap.put("updated", Updated);
				}			
				
			} catch (DataAccessException dae) {
				primeRealtyLogger.error(PropertyinformationDao.class, "updatePropInfoPersonForProperty() ->  - unable to update person - DataAccessException : " + dae.getMessage());				
			}
		}
		
		return resultMap;
	}
				
	private static String UPDATE_PROPERTYINFORMATION = "UPDATE PropertyInformation SET SquareFeet = ?, Beds = ?, Baths = ?, Stories = ?, Lotsize = ?, YearBuilt = ?, YearRenovated = ?, APN = ?, Community = ?, HOADues = ?, PropertyType_idPropertyType = ?, UpdatePerson_idPerson = ?, UpdateDate = ? WHERE idPropertyInformation = ?";
	private static String UPDATE_ADDRESS = "UPDATE Address SET Address1 = ?, Address2 = ?, Address3 = ?, Zip = ?, CityName = ?, State_idState = ?, Country_idCountry = ? WHERE idAddress = ?";
	private static String DELETE_ADDRESS = "DELETE FROM Address WHERE idAddress=?";
	private static String PROPERTY_TYPES_LIST_QUERY = "SELECT idPropertyType, PropertyType FROM PropertyType";
	private static String PROPERTY_INFORMATION_QUERY = "SELECT PropertyInformation.SquareFeet, PropertyInformation.Beds, PropertyInformation.Baths, PropertyInformation.Stories, PropertyInformation.Lotsize, PropertyInformation.YearBuilt, PropertyInformation.YearRenovated, PropertyInformation.APN, PropertyInformation.Community, PropertyInformation.HOADues, PropertyType.idPropertyType, PropertyType.PropertyType, Address.idAddress, Address.Address1, Address.Address2, Address.Address3, Address.Zip, Address.CityName, State.idState, State.StateCode, Country.idCountry, Country.CountryName FROM PropertyInformation, PropertyType, Address, State, Country WHERE PropertyInformation.PropertyType_idPropertyType = PropertyType.idPropertyType AND PropertyInformation.Address_idAddress = Address.idAddress AND Address.State_idState = State.idState AND Address.Country_idCountry = Country.idCountry AND PropertyInformation.idPropertyInformation = ?";
	private static String PERSON_IN_PROPINFOPERSON_QUERY = "SELECT DISTINCT Person_idPerson FROM PropertyInformationPerson WHERE Person_idPerson = ?";
	private static String PROPERTIES_BYOWNERPM_QUERY = "SELECT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.Beds, PropertyInformation.Baths, PropertyInformation.Stories, PropertyInformation.Lotsize, PropertyInformation.YearBuilt, PropertyInformation.YearRenovated, PropertyInformation.APN, PropertyInformation.Community, PropertyInformation.HOADues, PropertyType.idPropertyType, PropertyType.PropertyType, Address.idAddress, Address.Address1, Address.Address2, Address.Address3, Address.Zip, Address.CityName, State.idState, State.StateCode, Country.idCountry, Country.CountryName FROM PropertyInformation, Person, PropertyType, Address, State, Country WHERE PropertyInformation.PropertyType_idPropertyType = PropertyType.idPropertyType AND PropertyInformation.Address_idAddress = Address.idAddress AND Address.State_idState = State.idState AND Address.Country_idCountry = Country.idCountry AND PropertyInformation.CreatePerson_idPerson = Person.idPerson AND Person.PersonType_idPersonType = 6 AND PropertyInformation.CreatePerson_idPerson = ? AND PropertyInformation.idPropertyInformation NOT IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationDeleted)";
	private static String ASSIGNED_PROPERTYTENANTS_QUERY = "SELECT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate, PropertyInformationPerson.idPropertyInformationPerson FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PersonType.idPersonType = 2 AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = ?";
	private static String PROPERTIES_BYOWNER_QUERY = "SELECT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.Beds, PropertyInformation.Baths, PropertyInformation.Stories, PropertyInformation.Lotsize, PropertyInformation.YearBuilt, PropertyInformation.YearRenovated, PropertyInformation.APN, PropertyInformation.Community, PropertyInformation.HOADues, PropertyType.idPropertyType, PropertyType.PropertyType, Address.idAddress, Address.Address1, Address.Address2, Address.Address3, Address.Zip, Address.CityName, State.idState, State.StateCode, Country.idCountry, Country.CountryName FROM PropertyInformation, Person, PropertyType, Address, State, Country WHERE PropertyInformation.PropertyType_idPropertyType = PropertyType.idPropertyType AND PropertyInformation.Address_idAddress = Address.idAddress AND Address.State_idState = State.idState AND Address.Country_idCountry = Country.idCountry AND PropertyInformation.CreatePerson_idPerson = Person.idPerson AND (PersonType_idPersonType = 1 OR PersonType_idPersonType = 7) AND PropertyInformation.CreatePerson_idPerson = ? AND PropertyInformation.idPropertyInformation NOT IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationDeleted)";
	private static String ASSIGNED_PROPERTYPROPMNGR_BYOWNER_QUERY = "SELECT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate, PropertyInformationPerson.idPropertyInformationPerson FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PersonType.idPersonType = 3 AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = ?";	
	private static String PROPERTIES_BYPROPMNGR_QUERY = "SELECT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.Beds, PropertyInformation.Baths, PropertyInformation.Stories, PropertyInformation.Lotsize, PropertyInformation.YearBuilt, PropertyInformation.YearRenovated, PropertyInformation.APN, PropertyInformation.Community, PropertyInformation.HOADues, PropertyType.idPropertyType, PropertyType.PropertyType, Address.idAddress, Address.Address1, Address.Address2, Address.Address3, Address.Zip, Address.CityName, State.idState, State.StateCode, Country.idCountry, Country.CountryName FROM PropertyInformation, Person, PropertyType, Address, State, Country WHERE PropertyInformation.PropertyType_idPropertyType = PropertyType.idPropertyType AND PropertyInformation.Address_idAddress = Address.idAddress AND Address.State_idState = State.idState AND Address.Country_idCountry = Country.idCountry AND PropertyInformation.CreatePerson_idPerson = Person.idPerson AND Person.PersonType_idPersonType = 3 AND PropertyInformation.CreatePerson_idPerson = ? AND PropertyInformation.idPropertyInformation NOT IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationDeleted)";
	private static String ASSIGNED_PROPERTYOWNER_BYPROPMNGR_QUERY = "SELECT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate, PropertyInformationPerson.idPropertyInformationPerson FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND (PersonType.idPersonType = 1 OR PersonType.idPersonType = 7) AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = ?";	
	private static String PROPERTIES_BYPROPINFOPERSON_QUERY = "SELECT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.Beds, PropertyInformation.Baths, PropertyInformation.Stories, PropertyInformation.Lotsize, PropertyInformation.YearBuilt, PropertyInformation.YearRenovated, PropertyInformation.APN, PropertyInformation.Community, PropertyInformation.HOADues, PropertyType.idPropertyType, PropertyType.PropertyType, Address.idAddress, Address.Address1, Address.Address2, Address.Address3, Address.Zip, Address.CityName, State.idState, State.StateCode, Country.idCountry, Country.CountryName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate FROM PropertyInformationPerson, PropertyInformation, PropertyType, Address, State, Country WHERE PropertyInformation.PropertyType_idPropertyType = PropertyType.idPropertyType AND PropertyInformation.Address_idAddress = Address.idAddress AND Address.State_idState = State.idState AND Address.Country_idCountry = Country.idCountry AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformationPerson.Person_idPerson = ? AND (PropertyInformationPerson.StartDate <= CURRENT_DATE AND (PropertyInformationPerson.EndDate IS NULL OR PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PropertyInformation.idPropertyInformation NOT IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationDeleted)";
	private static String LIVE_PROPERTYTENANTS_QUERY = "SELECT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate IS NULL AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.EndDate IS NULL OR PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PersonType.idPersonType = 2 AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = ?";
	private static String LIVE_PROPERTYPROPMGRS_BYOWNER_QUERY = "SELECT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate IS NULL AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.EndDate IS NULL OR PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PersonType.idPersonType = 3 AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = ?";	
	private static String LIVE_OTHER_PROPERTYPROPMNGRS_BYOWNER_QUERY = "SELECT DISTINCT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate IS NULL AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.EndDate IS NULL OR PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PersonType.idPersonType = 3 AND PropertyInformationPerson.PropertyInformation_idPropertyInformation IN (:ids)";
	private static String LIVE_PROPERTYOWNERS_BYPROPMNGR_QUERY = "SELECT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName, PropertyInformationPerson.StartDate, PropertyInformationPerson.EndDate FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate IS NULL AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.EndDate IS NULL OR PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND (PersonType.idPersonType = 1 OR PersonType.idPersonType = 7) AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = ?";	
	private static String LIVE_OTHER_PROPERTYOWNERS_BYPROPMNGR_QUERY = "SELECT DISTINCT Person.idPerson, User.EmailAddress, User.FirstName, User.LastName FROM User, Person, PersonType, PropertyInformationPerson WHERE Person.User_EmailAddress = User.EmailAddress AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND ((PropertyInformationPerson.StartDate IS NULL AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.EndDate IS NULL OR PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND (PersonType.idPersonType = 1 OR PersonType.idPersonType = 7) AND PropertyInformationPerson.PropertyInformation_idPropertyInformation IN (:ids)";	
	private static String RESET_DATE_PROP_INFO_PERSON = "UPDATE PropertyInformationPerson SET StartDate = ?, EndDate = ? WHERE idPropertyInformationPerson = ?";
	private static String PROP_INFO_PERSON_QUERY = "SELECT idPropertyInformationPerson FROM PropertyInformationPerson WHERE  Person_idPerson = ? AND PropertyInformation_idPropertyInformation = ?";
	private static String UPDATE_PROP_INFO_PERSON = "UPDATE PropertyInformationPerson SET StartDate = ?, EndDate = ?, UpdateBy_idPerson = ?, UpdateDate = ? WHERE Person_idPerson = ? AND PropertyInformation_idPropertyInformation = ?";
	
}
