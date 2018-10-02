package prosia.app.security.jwt;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import prosia.app.model.User;
import prosia.app.service.UserService;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.validityInSeconds}")
    private long tokenValidityInSeconds;
    
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
    }

    public String createToken(Authentication authentication) {
        return this.createToken(authentication, Boolean.FALSE);
    }
    
    public String createToken(Authentication authentication, Boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInSeconds * 1000*24);
        
        return Jwts.builder()
            .setSubject(authentication.getName())
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        
        User principal = (User) userService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException | SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }
    
}
