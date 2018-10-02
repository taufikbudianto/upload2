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
public class ListContactAssetWs implements Serializable{

    @JsonProperty(value = "pic")
    private String pic;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "cellphone_number")
    private String cellPhone;
}
