package com.idbiintech.visitormgmtsystem.util;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;
import com.idbiintech.visitormgmtsystem.model.User;
import com.idbiintech.visitormgmtsystem.model.VisitorDtls;
import com.idbiintech.visitormgmtsystem.service.VisitorMgmtService;

import de.taimos.totp.TOTP;

@Component
public class Util {
	
	
	@Autowired
	VisitorMgmtService visitormgmtservice;
	
	@Autowired
	AESCrypt aescrypt;
	
	@Autowired
	Environment env;
	
	String statickey="";
	
	String strseckeyreq2="";
	
	String strsecKeyreq1="";
	
	String strdecreq="";
	
	ObjectMapper mapper1 = new ObjectMapper();
	
	static String reqjsntostr="";
	
	String encdeckey = "";




    public static String generateSecretKey() 
    {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String getTOTPCode(String secretKey) 
    {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) 
    {
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
                width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

    public static void infinityGeneratingCodes(String secretKey) 
    {
        String lastCode = null;
        while (true) {
            String code = getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {};
        }
    }
     
    public static String getencreq(JSONObject vstdtls) throws JsonMappingException, JsonProcessingException
    {
		ObjectMapper mapper = new JsonMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	    JsonNode json = mapper.readTree(vstdtls.toJSONString());
	     
	    String encreq1 = json.get("req1").toString();
	    String encreq2 = json.get("req2").toString();
		
	    encreq1 = encreq1.replaceAll("^\"|\"$", "");
	    encreq2 = encreq2.replaceAll("^\"|\"$", "");
	   
	   return encreq1+"-"+encreq2;
	   

    }	
    
    public  VisitorDtls getdecreq(String reqId,String encreq1, String encreq2, String strseckeyreq2) throws ParseException, JsonMappingException, JsonProcessingException
    {
		JSONParser parser = new JSONParser();
		JSONObject decreq2 = null;
		try 
		{
			decreq2 = (JSONObject) parser.parse(aescrypt.decrypt(encreq2, strseckeyreq2));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		VisitorDtls visitordtls = mapper1.readValue(decreq2.toJSONString(), VisitorDtls.class);
		
        return visitordtls;
    }
    
    
    	public  String getseckey(String reqId,String encreq1) throws ParseException, JsonMappingException, JsonProcessingException
    	{
    		try
    		{
    		strsecKeyreq1 = BcpIdForAES.getsecretkey("VSTMT1234", "4321TMTSV", reqId);
    		strdecreq = aescrypt.decrypt(encreq1, strsecKeyreq1);
    		System.out.println("strdecreq---in getsec key method----------"+strdecreq);
    		MstVisitorDtls empdata = visitormgmtservice.findDetailsById(Integer.parseInt(strdecreq));
    		strseckeyreq2 = BcpIdForAES.getsecretkey(empdata.getMobileno(), strdecreq, reqId);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
   		    }
    		return strseckeyreq2;
    	}
    	
    	
    	public  String getstatickey(String reqId) throws ParseException, JsonMappingException, JsonProcessingException
    	{
    		
    		try
    		{
    		 statickey = BcpIdForAES.getsecretkey("VSTMT1234", "4321TMTSV", reqId);
     		 System.out.println("statickey---in getstatickey method----------"+statickey);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			
    		}
    		return statickey;
    		
    	}
    	
    	public  String getempdata(String einno, String strpass) throws Exception
    	{
    		
    		
    		RestTemplate restTemplate = new RestTemplate(); 
    		
    		String custIdKey = env.getProperty("custIdKey");
            
    		System.out.println("custIdkey------------"+custIdKey);
    		
    		StringBuilder mobNoKey = new StringBuilder();
    		
    		mobNoKey.append(custIdKey);

    		String reverseKey = mobNoKey.reverse().toString();
    		
    		String strusrname = StringEncryptionAES.performFinalEncryption(custIdKey, reverseKey, einno, "", "");
    		
    		String strencpass = StringEncryptionAES.performFinalEncryption(custIdKey, reverseKey, strpass, "", "");

    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_JSON);
    		
    		
    		            
            JSONObject jsobj = new JSONObject();
            jsobj.put("ein", strusrname);
            jsobj.put("password", strencpass);
            
    		String url = env.getProperty("ibusadurl");
    		
    		System.out.println("url-----------"+url);
    		
    		headers.setContentType(MediaType.APPLICATION_JSON);
    		reqjsntostr = jsobj.toJSONString();
    		
    		
    		HttpEntity<String> entity = new HttpEntity<String>(reqjsntostr,headers);
    		JSONObject answer = restTemplate.postForObject(url, entity, JSONObject.class);
    		System.out.println(answer);
    		String resCode = (String) answer.get("responseCode");
    		
    		if(resCode.equals("00"))
    		{
    			return "Success";
    		}
    		
    		else
    		{
    			return "failure";
    		}
    	}

        public static String getencreq1(JSONObject vstdtls) throws JsonMappingException, JsonProcessingException
        {
    		ObjectMapper mapper = new JsonMapper();
    		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    	    JsonNode json = mapper.readTree(vstdtls.toJSONString());
    	    String encreq1 = json.get("req1").toString();
    	    encreq1 = encreq1.replaceAll("^\"|\"$", "");
    	    return encreq1;
        }	
        
        
    	public  String getencdeckey() throws ParseException, JsonMappingException, JsonProcessingException
    	{
    		String enckey = env.getProperty("encdeckey");
    		
    		try
    		{
    		 encdeckey = BcpIdForAES.getsecretkey("VSTMT1234", "4321TMTSV", enckey);
     		 System.out.println("encdeckey---in getencdeckey method----------"+encdeckey);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			
    		}
    		return encdeckey;
    		
    	}

    

}
