/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public class IncidentListWs implements Serializable {
    
    @JsonProperty(value = "incident_id")
    private String incidentid;
    @JsonProperty(value = "incident_name")
    private String incidentname;
    @JsonProperty(value = "incident_number")
    private String incidentnumber;
    @JsonProperty(value = "location")
    private String location;
    @JsonProperty(value = "latitude")
    private String latitude;
    @JsonProperty(value = "longitude")
    private String longitude;
    @JsonProperty(value = "incident_time")
    private String incidenttime;
    @JsonProperty(value = "gmt")
    private String gmt;
    @JsonProperty(value = "incident_type")
    private String incidenttype;
    @JsonProperty(value = "saroffice_list")
    private List<KantorSarList> sarofficesar;
    @JsonProperty(value = "picture_list")
    private List<IncidentListPictureWs> picturelist;
    
}
