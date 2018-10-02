/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Object to return as body in JWT Authentication.
 * @author Randy
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JWTToken implements Serializable {
    
    @JsonProperty(value = "response_code")
    private String responseCode;
    
    @JsonProperty(value = "access_token")
    private String accessToken;
    
    @JsonProperty(value = "menu_list")
    private List data;
    
}
