/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prosia.app.security.jwt.JWTConfigurer;
import prosia.app.security.jwt.TokenProvider;
import prosia.app.web.rest.dto.JWTToken;
import prosia.app.web.rest.dto.LoginParam;
import prosia.app.web.rest.dto.Response;
import prosia.app.web.rest.util.HeaderUtil;
import prosia.app.web.rest.util.ResponseCode;

/**
 *
 * @author Randy
 */
@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenProvider tokenProvider;
    
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginParam loginParam, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginParam.getUserName(), loginParam.getPassword());
        List<String> data =new ArrayList<>();

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            data.add("PROFILE");
            data.add("SETTING");
            data.add("POLICY");
            data.add("FEEDBACK");
            String jwt = tokenProvider.createToken(authentication);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, jwt);
            
            JWTToken responseData = new JWTToken();
            responseData.setResponseCode(ResponseCode.SUCCESS);
            responseData.setAccessToken(jwt);
            responseData.setData(data);
            return ResponseEntity.ok(responseData);
            
        } catch (AuthenticationException exception) {
            
            if (exception instanceof BadCredentialsException 
                    || exception instanceof UsernameNotFoundException) {
                
                log.warn("Username or password not match : {}", loginParam.getUserName());
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .headers(HeaderUtil.createAlert(
                                "Username or password not match", loginParam.getUserName()))
                        .body(new Response(ResponseCode.INVALID_TOKEN, "Username or password not match."));
            }
            
            if (exception instanceof LockedException) {
                
                log.warn("User is Inactive : {}", loginParam.getUserName());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .headers(HeaderUtil.createAlert(
                                "User is inactive, please contact administrator..", loginParam.getUserName()))
                        .body(new Response(ResponseCode.INVALID_TOKEN, "User is inactive."));
            }
            
            log.warn("AuthenticationException for : " + loginParam.getUserName(), exception);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(HeaderUtil.createAlert("AuthenticationException", exception.getLocalizedMessage()))
                    .body(new Response(ResponseCode.INVALID_TOKEN, "Username or password not match."));
        }
    }
    
}
