/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest.util;

import org.json.JSONObject;

/**
 * Utility class for HTTP body creation.
 * @author Randy
 */
public class BodyUtil {
    
    public static String X_RESPONSE_CODE = "response_code";
    public static String X_RESPONSE_MESSAGE = "response_message";
    
    public static String X_MESSAGE_INVALID_TOKEN = "Invalid/expired access token.";
    public static String X_MESSAGE_SUCCESS = "Success.";
    
    public static String X_MESSAGE_DATA_NULL = "Data Tidak Tersedia";
    public static String X_MESSAGE_ERROR_AUTH = "Authorization Null";
    
    public static String X_MESSAGE_NO_TOKEN = "Please enter a token for the request.";
    
    public static String X_FORMAT_DATE = "yyyy-MM-dd";
    public static String X_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    
    public static JSONObject create(String responseCode, String responseMessage) {
        JSONObject body = new JSONObject();
        body.put(X_RESPONSE_CODE, responseCode);
        body.put(X_RESPONSE_MESSAGE, responseMessage);
        return body;
    }
    
}
