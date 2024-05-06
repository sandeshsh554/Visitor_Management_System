package com.idbiintech.visitormgmtsystem.auth;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.idbiintech.visitormgmtsystem.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil 
{

    public  final String secret_key = "mysecretkey";
    
    //public long accessTokenValidity = 24*60*60*10000;
    
    public long accessTokenValidity = 24*60*60*10000;

    public final JwtParser jwtParser;

    public final String TOKEN_HEADER = "Authorization";
    public final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil()
    {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(User user) 
    {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    public Claims parseJwtClaims(String token) 
    {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try 
        {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) 
    {
        System.out.println(" in resolve Token ");
        String bearerToken = request.getHeader(TOKEN_HEADER);
        System.out.println("bearerToken--------------"+bearerToken);
        
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) 
        {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) 
    {
        return claims.getSubject();
    }
 
    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }


    

}
