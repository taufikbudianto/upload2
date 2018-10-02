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
 * @author PROSIA
 */
@Getter
@Setter
@ToString
public class IncidentListPictureWs implements Serializable{
    @JsonProperty(value = "picture")
    private String picture;
}
