package com.tamkosoft.primerealty.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("primeRealtyLogger")
public class PrimeRealtyLogger {

	private Logger getLogger(Class<?> T) {
        return LoggerFactory.getLogger(T);
    }
	
	public void trace(Class<?> T, String message) {
		getLogger(T).trace(message);
	}
	
	public void debug(Class<?> T, String message) {
		getLogger(T).debug(message);
	}
	
	public void info(Class<?> T, String message) {
		getLogger(T).info(message);
	}
	
	public void warn(Class<?> T, String message) {
		getLogger(T).warn(message);
	}
	
	public void error(Class<?> T, String message) {
		getLogger(T).error(message);
	}
	
}
