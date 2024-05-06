package com.idbiintech.visitormgmtsystem.model;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResponse2<T> 
{
    
	private String message;
    private String status;
    private T data;
    private T employeedata;


	public ApiResponse2(String message, String status, T data, T employeedata) 
    {
        this.message = message;
        this.status = status;
        this.data =data;
        this.employeedata = employeedata;
    }

    

	public T getEmployeedata() {
		return employeedata;
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

    @SuppressWarnings("unchecked")
	public JSONObject toJsonString() throws JsonProcessingException {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	String strdata = mapper.writeValueAsString(data);
    	String stremp = mapper.writeValueAsString(employeedata);

    	
        JSONObject response = new JSONObject();
    	response.put("message",message);
    	response.put("status",status);
    	response.put("data", strdata);
    	response.put("employeedata", stremp);
    	return response;
    }


}