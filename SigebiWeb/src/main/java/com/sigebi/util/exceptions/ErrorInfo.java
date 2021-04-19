package com.sigebi.util.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {

   @JsonProperty("message")
   private String message;
   @JsonProperty("status")
   private int status;
   @JsonProperty("error")
   private String error;
   @JsonProperty("uri")
   private String uriRequested;

   public ErrorInfo(int status, String error ,String message, String uriRequested) {
       this.message = message;
       this.error = error;
       this.status = status;
       this.uriRequested = uriRequested;
   }

   public String getMessage() {
       return message;
   }

   public int getStatus() {
       return status;
   }

   public String getUriRequested() {
       return uriRequested;
   }

   public String getError() {
	   return error;
   }
}