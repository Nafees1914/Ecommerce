package com.Ecommerce.Ecommerce.Security;

import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import com.Ecommerce.Ecommerce.Repository.RoleRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class JwtGenerator {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtGenerator.class);


    public String generateTokenFromUsername(Authentication authentication) {

        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .claim("ROLE", authorities)
                .setExpiration(new Date(new Date().getTime() + SecurityConstants.JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /*-->change */
    public String generateJwtToken(Authentication authentication) {
        return generateTokenFromUsername(authentication);
    }

    public boolean validateJwtToken(String authToken) throws AccessDeniedException {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            throw new AccessDeniedException("JWT TOKEN GOT EXPIRED OR INVALID!!!!");
        }

    }
}
