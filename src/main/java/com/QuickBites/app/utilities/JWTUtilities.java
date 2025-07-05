package com.QuickBites.app.utilities;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.QuickBites.app.services.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTUtilities{	
	private SecretKey secretKey;
	
	@Value("${jwt.accessKey.expiration.time}")
	private Long ACCESSKEY_EXPIRATION_TIME;
	
	@Value("${jwt.refreshKey.expiration.time}")
	private Long REFRESHKEY_EXPIRATION_TIME;
	
	@Value("${jwt.secretKey}")
	private String base64SecretKey;
	
	@PostConstruct					//this runs after bean is initialized such that @value field is populated
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64SecretKey));
	}

	//Generating a JWT Access Token 
	public String generateAccessToken(Authentication customUserObj) {
		CustomUserDetails customUser = (CustomUserDetails) customUserObj.getPrincipal();
		Map<String,Object> myClaims = new HashMap<>(); // to add as a claims in payload in jwt token
		
		
		//Extracting userRoles as a String
	 	List<String> userRoles = customUser.getAuthorities()
	 										.stream()
	 										.map(grantedAuthority->grantedAuthority.getAuthority())
	 										.collect(Collectors.toList());
	 	String jti = UUID.randomUUID().toString();
		myClaims.put("roles", userRoles);
		myClaims.put("jti", jti);
	 		
		return 
				Jwts.builder()
				.setHeaderParam("type", "JWT")
				.setSubject(customUser.getUsername())
				.addClaims(myClaims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+ACCESSKEY_EXPIRATION_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	//Generate a JWT Refresh Token
	public String generateRefreshToken(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Map<String,Object> claims = new HashMap<>();
		claims.put("tokenType","refreshToken");
		String jti = UUID.randomUUID().toString();
		claims.put("jti", jti);
		return Jwts.builder()
				.setHeaderParam("type", "JWT")
				.setSubject(userDetails.getUsername())
				.addClaims(claims)
				.setIssuedAt(new Date())	
				.setExpiration(new Date(System.currentTimeMillis()+REFRESHKEY_EXPIRATION_TIME))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	//Validate a token
	public boolean isValidToken(String token, UserDetails userDetails) {
		try {
			Claims claims = Jwts.parserBuilder()
							.setSigningKey(secretKey)
							.build()
							.parseClaimsJws(token)
							.getBody();
			
			String extractedUserName = claims.getSubject();
			Date expirationTime = claims.getExpiration();
			
			return (extractedUserName.equals(userDetails.getUsername()) && !expirationTime.before(new Date()));
		}
		catch(Exception ex) {
			return false;
		}		
	}
	
	
	public Claims extractTokenBody(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
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
	
	private Key getSigningKey() {
		byte[] decoded = Decoders.BASE64.decode(base64SecretKey);
		return Keys.hmacShaKeyFor(decoded);
	}
	
	
	public boolean isRefreshToken(String token ) {
		try {
			Claims claimBody =  Jwts.parserBuilder()
						.setSigningKey(getSigningKey())
						.build()
						.parseClaimsJws(token)
						.getBody();
		 return "refreshToken".equals(claimBody.get("tokenType"));
		}catch(Exception ex) {
			return false;
		}
			
	}

	public boolean isTokenExpired(String token) {
		return extractTokenBody(token).getExpiration().before(new Date());
	}
	}
	
	
