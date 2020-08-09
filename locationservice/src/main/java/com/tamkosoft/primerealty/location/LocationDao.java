package com.tamkosoft.primerealty.location;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("locationDao")
public class LocationDao {
	

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getCountries() {
		  
		return jdbcTemplate.queryForList(COUNTRIES_QUERY);
	}
	
	// US country Id is 1
	public List<Map<String, Object>> getUSStates() {
		
		return jdbcTemplate.queryForList(USSTATES_QUERY);
	}
	
	public List<Map<String, Object>> getCitiesByName(String CityChars, String StateCode) {
		
		String StateQuery = "";
		if (StateCode != null) {
			StateQuery = " and StateCode = '" + StateCode + "'";
		} 
		
		return jdbcTemplate.queryForList(CITIES_BYNAME_QUERY + " where CityName like '"+ CityChars +"%'" + StateQuery);
		
	}
	
	public Map<String, Object> getCityByZip(Integer ZipCode) {
		
		return jdbcTemplate.queryForMap(CITIES_BYZIP_QUERY, ZipCode);
	
	}
	
	private static String COUNTRIES_QUERY = "select idCountry, CountryName, CountryCode from Country";
	private static String USSTATES_QUERY = "select idState, StateName, StateCode, CountryName, CountryCode from State, Country where State.Country_idCountry = Country.idCountry and Country.idCountry = 1";
	private static String CITIES_BYNAME_QUERY = "select CityName, StateCode, ZipCode from CitiesExtended";
	private static String CITIES_BYZIP_QUERY = "select CityName, StateCode, ZipCode from CitiesExtended where ZipCode = ?";
}
