package com.idbiintech.visitormgmtsystem.controller;

import com.idbiintech.visitormgmtsystem.auth.JwtUtil;
import com.idbiintech.visitormgmtsystem.model.User;
import com.idbiintech.visitormgmtsystem.model.LoginReq;
import com.idbiintech.visitormgmtsystem.model.ErrorRes;
import com.idbiintech.visitormgmtsystem.model.LoginRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;


    private JwtUtil jwtUtil;
    
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginReq loginReq)  
    {
        try 
        {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
            (loginReq.getEmail(),loginReq.getPassword()));
            String email = authentication.getName();
            System.out.println("email-----------"+email);
            System.out.println("Email------------"+loginReq.getEmail());
            System.out.println("Password-------------"+loginReq.getPassword());
            User user = new User(email,"");
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = new LoginRes(email,token);
            return ResponseEntity.ok(loginRes);
        }
        catch (BadCredentialsException e)
        {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (Exception e)
        {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}