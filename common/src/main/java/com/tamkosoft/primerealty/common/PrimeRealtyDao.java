package com.tamkosoft.primerealty.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component("primeRealtyDao")
public class PrimeRealtyDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	public Map<String, Object> getUserByPerson(Integer personId) {
		
		String name = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
				
		try {
			resultMap = jdbcTemplate.queryForMap(NAME_BYPERSON_QUERY, personId);
			
			Object FirstName = resultMap.get("FirstName");
			Object MiddleName = resultMap.get("MiddleName");	
			Object LastName = resultMap.get("LastName");	
			name = name.concat(FirstName != null ? FirstName.toString() : "");
			name = name.concat(MiddleName != null ? " " + MiddleName.toString() : "");
			name = name.concat(LastName != null ? " " + LastName.toString() : "");
			resultMap.put("Name", name);
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(PrimeRealtyDao.class, "getNameByPerson() -> Exception : DataAccessException - " + dae.getMessage());
			
		}
		
		return resultMap;
	}
	
	public Number tableInsert(String table, Map<String, Object> params) {
		  
		return new SimpleJdbcInsert(jdbcTemplate)
					.withTableName(table)
					.usingGeneratedKeyColumns("id" + table)
					.executeAndReturnKey(params);	
	}
	
	private static String NAME_BYPERSON_QUERY = "SELECT User.FirstName as FirstName, User.MiddleName as MiddleName, User.LastName as LastName, User.EmailAddress as EmailAddress FROM Person, User WHERE Person.User_EmailAddress = User.EmailAddress AND Person.idPerson = ?";
	
}
