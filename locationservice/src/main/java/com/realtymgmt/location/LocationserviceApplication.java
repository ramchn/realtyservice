package com.realtymgmt.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController 
public class LocationserviceApplication {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
		
	public static void main(String[] args) {
		SpringApplication.run(LocationserviceApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}
	
	@GetMapping("/countries")
	private List<Map<String, Object>> getCountries() {
	  
		return jdbcTemplate.queryForList(COUNTRIES_QUERY);
	}
	
	// US country Id is 1
	@GetMapping("/usstates")
	private List<Map<String, Object>> getUSStates() {
		
		return jdbcTemplate.queryForList(USSTATES_QUERY);
	}
	  
	// minimum 5 characters required
	@GetMapping("/citiesbyname")
	private List<Map<String, Object>> getCitiesByName(@RequestParam String chars, @RequestParam(required = false) String StateCode) {
		
		String stateQuery = "";
		if (StateCode != null) {
			stateQuery = " and StateCode = '"+StateCode+"'";
		} 
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Map<String, Object> columns = new HashMap<String, Object>();
		
		if(chars.length() >= 5) {		  
			rows = jdbcTemplate.queryForList(CITIES_QUERY + " where CityName like '"+ chars +"%'" + stateQuery);
		
		} else {
			columns.put("CityName", "minimum 5 characters required");
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
			columns = jdbcTemplate.queryForMap(CITIES_QUERY + " where ZipCode = "+ZipCode);
		
		} catch (EmptyResultDataAccessException e) {
			columns.put("CityName", null);
			columns.put("StateCode", null);
			columns.put("ZipCode", "Enter valid ZipCode");
			  
		}	
		return columns;
	}
	
	private static String COUNTRIES_QUERY = "select idCountry, CountryName, CountryCode from Country";
	private static String USSTATES_QUERY = "select idState, StateName, StateCode, CountryName, CountryCode from State, Country where State.Country_idCountry = Country.idCountry and Country.idCountry = 1";
	private static String CITIES_QUERY = "select CityName, StateCode, ZipCode from CitiesExtended";
	

}
