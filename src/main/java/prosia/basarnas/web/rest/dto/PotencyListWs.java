/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 *
 * @author PROSIA
 */
@Getter
@Setter
@ToString
public class PotencyListWs implements Serializable{
    @JsonProperty(value = "potency_name")
    private String potencyname;
    @JsonProperty(value = "potency_address")
    private String potencyaddress;
    @JsonProperty(value = "latitude")
    private String latitude;
    @JsonProperty(value = "longitude")
    private String longitude;
    @JsonProperty(value = "potency_phone_number")
    private String potencyphonenumber;
    @JsonProperty(value = "province")
    private String province;
    @JsonProperty(value = "contact_list")
    private List<PotencyContactList> contactList;
    @JsonProperty(value = "picture_list")
    private List<PotencyPicturesListWs> pictureList;
    
    
}
