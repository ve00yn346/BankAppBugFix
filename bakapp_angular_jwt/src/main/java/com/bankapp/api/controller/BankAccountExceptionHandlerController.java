package com.bankapp.api.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bankapp.exceptions.BankAccountNotFoundException;

@RestControllerAdvice
public class BankAccountExceptionHandlerController {

	@ExceptionHandler(BankAccountNotFoundException.class)
	public ResponseEntity<ProblemDetail> handle404(BankAccountNotFoundException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

		problemDetail.setTitle("Account Not Found");
		problemDetail.setDetail(e.getMessage());
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("errorCode", "NOT_FOUND");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
	}
	
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ProblemDetail> handle500(Exception e) {
//		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//
//		problemDetail.setTitle("Internal server error");
//		problemDetail.setDetail("Please try after some time");
//		problemDetail.setProperty("timestamp", Instant.now());
//		problemDetail.setProperty("errorCode", "INTERNAL_SERVER_ERROR");
//
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
//	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {

		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

}
