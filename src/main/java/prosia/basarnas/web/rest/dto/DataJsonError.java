/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Taufik
 */
@Data
public class DataJsonError implements Serializable{

    @JsonProperty(value = "response_code")
    private String code;
    @JsonProperty(value = "response_message")
    private String message;
}
