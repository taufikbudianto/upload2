/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Taufik
 */
@Data
public class OfficeWs implements Serializable{

    @JsonProperty(value = "office_code")
    private String officeCode;
    @JsonProperty(value = "office_name")
    private String officeName;
    @JsonProperty(value = "office_address")
    private String officeAddress;
    @JsonProperty(value = "latitude")
    private String latitude;
    @JsonProperty(value = "longitude")
    private String longitude;
    @JsonProperty(value = "pic")
    private String pic;
    @JsonProperty(value = "number")
    private String phoneNumber;
    @JsonProperty(value = "picture_list")
    private List picture;
    @JsonProperty(value = "contact_list")
    private List contact;
    
}
