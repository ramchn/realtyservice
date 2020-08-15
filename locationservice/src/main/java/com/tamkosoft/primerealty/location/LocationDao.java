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
	
	private static String CITIES_BYNAME_QUERY = "select CityName, StateCode, ZipCode from CitiesExtended";
	private static String CITIES_BYZIP_QUERY = "select CityName, StateCode, ZipCode from CitiesExtended where ZipCode = ?";
}
