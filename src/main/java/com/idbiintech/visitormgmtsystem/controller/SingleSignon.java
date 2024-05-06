package com.idbiintech.visitormgmtsystem.controller;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.idbiintech.visitormgmtsystem.util.StringEncryptionAES;

@RestController
public class SingleSignon 
{
	
	String strencdata="";
	
	String strurl = "https://ibusUAT.idbibank.com:1919/iBus/utility/IntechValidateUser";
	
	String requestJson = "";
	
	@GetMapping("/encempdata")
	public String getempdata() throws Exception
	{
		
		System.out.println("in get emp data");
		
		RestTemplate restTemplate = new RestTemplate(); 
		String custIdKey = "2981012620";
		
		StringBuilder mobNoKey = new StringBuilder();
		
		mobNoKey.append(custIdKey);

		String reverseKey = mobNoKey.reverse().toString();
		
		String strusrname = StringEncryptionAES.performFinalEncryption(custIdKey, reverseKey, "INT7481", "", "");
		
		String strencpass = StringEncryptionAES.performFinalEncryption(custIdKey, reverseKey, "Global@20231", "", "");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		            
        JSONObject jsobj = new JSONObject();
        jsobj.put("ein", strusrname);
        jsobj.put("password", strencpass);
        
        
		String url = "https://ibusUAT.idbibank.com:1919/iBus/utility/IntechValidateUser";
		headers.setContentType(MediaType.APPLICATION_JSON);
		requestJson = jsobj.toJSONString();
		
		HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
		JSONObject answer = restTemplate.postForObject(url, entity, JSONObject.class);
		System.out.println(answer);
		String resCode = (String) answer.get("responseCode");
		
		System.out.println("resCode------------"+resCode);
		
		
		return "";
	}
	

}
