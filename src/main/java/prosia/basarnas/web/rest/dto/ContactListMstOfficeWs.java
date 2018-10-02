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
public class ContactListMstOfficeWs implements Serializable {

    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "position")
    private String position;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "number")
    private String number;
}
