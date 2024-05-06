package com.idbiintech.visitormgmtsystem.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ApiResponse<T> 
{
    private String message;
    private String status;
    private T data;
    private String token;
    private Boolean passwordExpired;

    public ApiResponse(String message, String status, T data) 
    {
        this.message = message;
        this.status = status;
        this.data = data;
    }
    
    
    public ApiResponse(String message, String status, T data, String token) 
    {
        this.message = message;
        this.status = status;
        this.data = data;
        this.token=token;   
    }
    
    
    public ApiResponse(String message, String status, T data, String token, Boolean passwordExpired) 
    {
        this.message = message;
        this.status = status;
        this.data = data;
        this.token=token;
        this.passwordExpired = passwordExpired;
    }

    
    public ApiResponse(T data) 
    {
        this.data = data;
    }

    public String getMessage() 
    {
        return message;
    }

    public String getStatus() 
    {
        return status;
    }

    
    
    
    public T getData() 
    {
        return data;
    }
     
    public String getToken() {
		return token;
	}


	public void setToken(String token) 
	{
		this.token = token;
	}
	


	

	public Boolean getPasswordExpired() 
	{
		return passwordExpired;
	}


	public void setPasswordExpired(Boolean passwordExpired) 
	{
		this.passwordExpired = passwordExpired;
	}


	@SuppressWarnings("unchecked")
	public JSONObject toJsonString() throws JsonProcessingException {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	String json = mapper.writeValueAsString(data);
        JSONObject response = new JSONObject();
    	response.put("message",message);
    	response.put("status",status);
    	response.put("token", token);
    	response.put("passwordExpired", passwordExpired);
    	response.put("data", json);
    	return response;
    }
    
    

}