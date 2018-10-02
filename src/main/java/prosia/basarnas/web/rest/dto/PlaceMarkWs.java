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
 * @author PROSIA
 */
@Data
public class PlaceMarkWs implements Serializable{
  @JsonProperty(value = "placemarkName")
  private String name;
  @JsonProperty(value = "description")
  private String description;
  @JsonProperty(value = "latitude")
  private String latitude;
  @JsonProperty(value = "longitude")
  private String longitude;
  @JsonProperty(value = "incidentId")
  private String incidentid;
  @JsonProperty(value = "image_icon")
  private String image;
}
