package com.tamkosoft.primerealty.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class PrimeRealtyExceptionHandler {
																																																																																																																																																																																																																																																											
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException see, HttpServletRequest request, HttpServletResponse response) {
        
    	primeRealtyLogger.error(PrimeRealtyExceptionHandler.class, "maxUploadSizeExceededExceptionHandler() -> MaxUploadSizeExceededException: " + see.getMessage());
        
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
    }
}
