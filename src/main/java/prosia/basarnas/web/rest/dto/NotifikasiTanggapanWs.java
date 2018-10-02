/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Taufik
 */
@Getter
@Setter
@ToString
public class NotifikasiTanggapanWs implements Serializable{
 
    @JsonProperty(value = "comment")
    private String comment;
    @JsonProperty(value = "gender")
    private String gender;
    @JsonProperty(value = "created_date")
    private String dateCreated;
    @JsonProperty(value = "created_by")
    private String createdBy;
    
}
