/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest.util;

import java.util.Arrays;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Utility class for HTTP header creation.
 * @author Randy
 */
public class HeaderUtil {
    
    public final static String X_ALERT = "X-alert";
    public final static String X_ERROR = "X-error";
    public final static String X_PARAMS = "X-params";
    
    public final static String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public final static String ACCESS_CONTROL_ALLOW_HEADER = "Access-Control-Allow-Headers";
    public final static String ACCESS_CONTROL_ALLOW_METHOD = "Access-Control-Allow-Methods";
    public final static String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public final static String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_ALERT, message);
        headers.add(X_PARAMS, param);
        return headers;
    }
    
    public static HttpHeaders createAlert(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_ALERT, message);
        return headers;
    }

    public static HttpHeaders createEntityAlert(String message) {
        return createAlert(message);
    }
    
    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("A new " + entityName + " is created with identifier " + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is updated with identifier " + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is deleted with identifier " + param, param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_PARAMS, entityName);
        headers.add(X_ERROR, errorKey);
        return headers;
    }
    
    public static HttpEntity<String> getRequestHeaderEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        
        return new HttpEntity<>("parameters", headers);
    }
    
}
