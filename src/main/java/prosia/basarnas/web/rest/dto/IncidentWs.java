/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class IncidentWs implements Serializable {

    @JsonProperty(value = "inc_id")
    private String incidentId;
    @JsonProperty(value = "inc_number")
    private String incidentNumber;
    @JsonProperty(value = "inc_type")
    private String incidentType;
    @JsonProperty(value = "situation")
    private String situation;
    @JsonProperty(value = "method")
    private String method;
    @JsonProperty(value = "phonenumber")
    private String phoneNumber;
    @JsonProperty(value = "location")
    private String location;
    @JsonProperty(value = "longitude")
    private String longitude;
    @JsonProperty(value = "latitude")
    private String latitude;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "keterangan")
    private String keterangan;
    @JsonProperty(value = "death_people")
    private Integer deathPeople;
    @JsonProperty(value = "heavy_injured_people")
    private Integer heavyInjuredPeople;
    @JsonProperty(value = "light_injured_people")
    private Integer lightInjuredPeople;
    @JsonProperty(value = "lost_people")
    private Integer lostPeople;
    @JsonProperty(value = "survived_people")
    private Integer survivedPeople;
    @JsonProperty(value = "kantorsar")
    private List<String> kantorSar;
    @JsonProperty(value = "isdeleted")
    private Integer isDeleted;
    @JsonProperty(value = "inc_name")
    private String incidentName;
    @JsonProperty(value = "owner_name")
    private String ownerName;
    @JsonProperty(value = "owner_addres")
    private String ownerAddres;
    @JsonProperty(value = "owner_phone")
    private String ownerPhone;
    @JsonProperty(value = "owner_cell")
    private String ownerCell;
    @JsonProperty(value = "owner_fax")
    private String ownerFax;
    @JsonProperty(value = "object_callsign")
    private String objectCallSign;
    @JsonProperty(value = "object_color")
    private String objectColor;
    @JsonProperty(value = "object_length")
    private String objectLength;
    @JsonProperty(value = "object_capacity")
    private String objectCapacity;
    @JsonProperty(value = "object_homebase")
    private String objectHomebase;
    @JsonProperty(value = "smc")
    private String smc;
    @JsonProperty(value = "date_incident")
    private Date startDate;
    @JsonProperty(value = "alert_date")
    private Date alertDate;
    @JsonProperty(value = "alert_by")
    private String alertBy;
    @JsonProperty(value = "start_operation")
    private Date startStopDate;
    @JsonProperty(value = "close_operation")
    private Date closeDate;
}
