package com.sigebi.util.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> exception(HttpServletRequest request, Exception e) {
		String mensaje = "Error interno del servidor. Consulte con soporte";
		ErrorInfo errorInfo = new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), request.getRequestURI());
		e.printStackTrace();
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorInfo> exception(HttpServletRequest request, DataAccessException e) {
		String mensaje = "Ocurrió un error en la base de datos. " + (e.getCause() != null && e.getCause().getCause().getMessage() != null ? e.getCause().getCause() : e.getMessage());
		ErrorInfo errorInfo = new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), mensaje, request.getRequestURI());
		e.printStackTrace();
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(SigebiException.class)
	public ResponseEntity<ErrorInfo> senacException(HttpServletRequest request, SigebiException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.status.value(), e.status.getReasonPhrase(), e.getMessage(), request.getRequestURI());
		e.printStackTrace();
		return new ResponseEntity<>(errorInfo, e.status);
	}	
	
}
