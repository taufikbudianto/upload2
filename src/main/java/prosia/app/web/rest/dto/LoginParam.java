/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representing an user's credential parameters.
 * @author Randy
 */
@Getter
@Setter
@ToString
public class LoginParam implements Serializable {
    
    @NotNull
    @JsonProperty(value = "username")
    private String userName;

    @NotNull
    @JsonProperty(value = "password")
    private String password;
    
}
