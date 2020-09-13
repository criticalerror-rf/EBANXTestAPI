package com.ebanx.api.controller;

import java.awt.PageAttributes.MediaType;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ebanx.api.dto.Account;
import com.ebanx.api.dto.Event;

@RestController
public class RestAPIController {
	private Event eventDTO=new Event();
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private HashMap<String,Account> result=null;

	@PostMapping(path = "/event", consumes = "application/json", produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public HashMap<String, Account> processEvent(
			@RequestBody Event eventReq) throws Exception {
		
			result=eventDTO.eventProcess(eventReq.getType(), eventReq.getAmount(), eventReq.getDestination(), eventReq.getOrigin());
			if(null==result)
			{
				throw new Exception("Account Not Found");
			}
			
			return result; 
	}
	@GetMapping("/balance")
	public int getBalance(@RequestParam(value = "account_id") int accountID) throws Exception {
		Account result=eventDTO.balance(accountID);
		if(null==result)
			throw new Exception("Account Not Found");
		else
			return result.getBalance();
			
	}
	@PostMapping("/reset")
	@ResponseStatus(HttpStatus.OK)
	public String reset() throws Exception {

			eventDTO.reset();
			return "OK";
			
	}
	
	@RestControllerAdvice
	public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	    @ExceptionHandler(Exception.class)
	    protected ResponseEntity<Object> handleMyException(Exception ex, WebRequest req) {
	        Object resBody = "0";
	        return handleExceptionInternal(ex, resBody, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
	    }

	}
}