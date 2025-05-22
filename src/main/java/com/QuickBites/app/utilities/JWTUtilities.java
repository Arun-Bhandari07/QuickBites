package com.QuickBites.app.utilities;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.QuickBites.app.services.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTUtilities{	
	private SecretKey secretKey;
	private final long EXPIRATION_TIME= 1000*60*60;
	
	@Value("${jwt.secretKey}")
	private String base64SecretKey;
	
	@PostConstruct					//this runs after bean is initialized such that @value field is populated
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64SecretKey));
	}

	//Generating a JWT token 
	public String generateToken(Authentication customUserObj) {
		CustomUserDetails customUser = (CustomUserDetails) customUserObj.getPrincipal();
		Map<String,Object> myClaims = new HashMap<>(); // to add as a claims in payload in jwt token
		
		
		//Extracting userRoles as a String
	 	List<String> userRoles = customUser.getAuthorities()
	 										.stream()
	 										.map(grantedAuthority->grantedAuthority.getAuthority())
	 										.collect(Collectors.toList());
		myClaims.put("roles", userRoles);
	 		
		return 
				Jwts.builder()
				.setSubject(customUser.getUsername())
				.addClaims(myClaims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	//Validate a token
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
			return true;
		}
		catch(Exception ex) {
			return false;
		}		
	}
	
	
	
	
	public Claims extractTokenBody(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}
	
	public Long extractDate(String token) {
		return extractTokenBody(token).getExpiration().getTime();
	}
	
	public String extractUsername(String token) {
		return extractTokenBody(token).getSubject();
	}
	
	public List<String> extractClaims(String token){
		return (List<String>)  extractTokenBody(token).get("roles", List.class);
	}
	
	
	
	
	
	}
	
	
