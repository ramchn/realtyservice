package com.realtymgmt.location.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class MainController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/index")
	public String gindex() {		
		return "GET Index";
	}
	
	@GetMapping("/countries")
	private List<Map<String, Object>> getCountries() {
	  
		return jdbcTemplate.queryForList("select idCountry, CountryName, CountryCode from Country");
	}
	  
	// US country Id is 1
	@GetMapping("/usstates")
	private List<Map<String, Object>> getUSStates() {
		  
		return jdbcTemplate.queryForList("select idState, StateName, StateCode, CountryName, CountryCode from State, Country where State.Country_idCountry = Country.idCountry and Country.idCountry = 1");
	}
	  
	// minimum 3 characters required
	@GetMapping("/citiesbyname")
	private List<Map<String, Object>> getCitiesByName(@RequestParam String chars, @RequestParam(required = false) String StateCode) {
		
		String stateQuery = "";
		if (StateCode != null) {
			stateQuery = " and StateCode = '"+StateCode+"'";
		} 
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Map<String, Object> columns = new HashMap<String, Object>();
		
		if(chars.length() >= 3) {		  
			rows = jdbcTemplate.queryForList("select CityName, StateCode, ZipCode from CitiesExtended where CityName like '"+chars+"%'"+stateQuery);
		
		} else {
			columns.put("CityName", "minimum 3 characters required");
			columns.put("StateCode", null);
			columns.put("ZipCode", null);
			rows.add(columns);
		}
		
		return rows;
	}
		
	@GetMapping("/citybyzip")
	private Map<String, Object> getCityByZip(@RequestParam Integer ZipCode) {
		
		Map<String, Object> columns = new HashMap<String, Object>();
				
		try {		  
			columns = jdbcTemplate.queryForMap("select CityName, StateCode, ZipCode from CitiesExtended where ZipCode = "+ZipCode);
		
		} catch (EmptyResultDataAccessException e) {
			columns.put("CityName", null);
			columns.put("StateCode", null);
			columns.put("ZipCode", "Enter valid ZipCode");
			  
		}	
		return columns;
	}
		
}
