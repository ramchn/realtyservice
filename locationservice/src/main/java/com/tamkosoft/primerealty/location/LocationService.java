package com.tamkosoft.primerealty.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.Location;

@RestController 
public class LocationService {
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
	LocationDao locationDao;
	
	// minimum 5 characters required
	@PostMapping("/citiesbyname")
	private List<Map<String, Object>> getCitiesByName(@Valid @RequestBody Location location) {
		
		primeRealtyLogger.debug(LocationService.class, "getCitiesByName() -> cityName: " + location.getCityName() + ", stateCode: " + location.getStateCode());
				
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Map<String, Object> columns = new HashMap<String, Object>();
		
		if(location.getCityName()!= null && location.getCityName().length() >= 5) {	
			
			rows = locationDao.getCitiesByName(location.getCityName(), location.getStateCode());
			
			primeRealtyLogger.debug(LocationService.class, "getCitiesByName() -> databse fetch size: " + rows.size());
					
		} else {
			columns.put("CityName", "minimum 5 characters required");
			columns.put("StateCode", null);
			columns.put("ZipCode", null);
			rows.add(columns);
		}
		
		return rows;
	}
		
	@PostMapping("/citybyzip")
	private Map<String, Object> getCityByZip(@Valid @RequestBody Location location) {
		
		Map<String, Object> columns = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(LocationService.class, "getCityByZip() -> zipCode: " + location.getZipCode());
						
		try {		  
			columns = locationDao.getCityByZip(location.getZipCode()!= null ? Integer.parseInt(location.getZipCode()) : 0);
			
			primeRealtyLogger.debug(LocationService.class, "getCityByZip() -> databse fetch: " + columns);
		
		} catch (EmptyResultDataAccessException e) {
			columns.put("CityName", null);
			columns.put("StateCode", null);
			columns.put("ZipCode", "Enter valid ZipCode");
			  
		}	
		return columns;
	}
}
