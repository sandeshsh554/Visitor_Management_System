package com.idbiintech.visitormgmtsystem.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idbiintech.visitormgmtsystem.model.ApiResponse;
import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;
import com.idbiintech.visitormgmtsystem.util.AESCrypt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
	JwtUtil jwtUtil;
    
    ObjectMapper mapper = new ObjectMapper();
    
	
	String strespdata="";
	
	String token="";
	
	@Autowired 
	AESCrypt aescrypt;
	
	
	String encresp = "";
	
	
	String decresp = "";
	
	
	String strseckeyreq2="";
	
	


    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
    {
        
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null ) 
            {
                filterChain.doFilter(request, response);
                return;
            }
            
            System.out.println("token : "+accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            if(claims != null & jwtUtil.validateClaims(claims))
            {
                String email = claims.getSubject();
                System.out.println("email : "+email);  
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        
        catch (Exception e)
        {
            //errorDetails.put("message", "Authentication Error");
            //errorDetails.put("details",e.getMessage());   
            //response.setStatus(HttpStatus.FORBIDDEN.value());
			ApiResponse errorResponse = new ApiResponse("Authentication Error Invalid Token", "Failed", null);
			strespdata = mapper.writeValueAsString(errorResponse);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(401);
			JSONObject json = new JSONObject();
			json.put("message", "Authentication Error Invalid Token");
			json.put("status", "Failed");
			json.put("DATA", null);
            mapper.writeValue(response.getWriter(), json);
        }
        filterChain.doFilter(request, response);
    }
    
    
}
