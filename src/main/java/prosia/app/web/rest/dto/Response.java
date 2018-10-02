/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Default template object to return as body.
 * @author Randy
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Response implements Serializable {
    
    @JsonProperty(value = "response_code")
    private String responseCode;
    
    @JsonProperty(value = "response_message")
    private String responseMessage;
    
}
