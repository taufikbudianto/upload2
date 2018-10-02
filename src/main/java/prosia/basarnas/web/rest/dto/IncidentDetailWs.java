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
 * @author Taufik AB
 */
@Data
public class IncidentDetailWs implements Serializable {

    @JsonProperty(value = "incident_name")
    private String incedentName;
    @JsonProperty(value = "incident_number")
    private String incidentNumber;
    @JsonProperty(value = "sar_office")
    private String sarOffice;
    @JsonProperty(value = "incident_time")
    private String incidentTime;
    @JsonProperty(value = "general")
    private General generalIncident;
    @JsonProperty(value = "SH")
    private List<SightingHearing> sightingHearing;
    @JsonProperty(value = "SRU")
    private List<Sru> sru;
    @JsonProperty(value = "resource")
    private Resource resource;
    @JsonProperty(value = "planning")
    private List<Planning> planning;
    @JsonProperty(value = "log")
    private List<LogIncident> log;
    @JsonProperty(value = "pob")
    private List<Pob> pob;
    @JsonProperty(value = "drift_calculation")
    private List<DriftCalculation> drift_calculation;

    @Data
    public static class General implements Serializable {

        @JsonProperty(value = "incident_scala")
        private String incidentScala;
        @JsonProperty(value = "sar_office")
        private String sarOffice;
        @JsonProperty(value = "icident_type")
        private String incidenttype;
        @JsonProperty(value = "incident_number")
        private String incidentNumber;
        @JsonProperty(value = "location")
        private String location;
        @JsonProperty(value = "latitude")
        private String latitude;
        @JsonProperty(value = "longitude")
        private String longitude;
        @JsonProperty(value = "smc")
        private String smc;
        @JsonProperty(value = "start_date")
        private String startDate;
        @JsonProperty(value = "close_date")
        private String closeDate;
        @JsonProperty(value = "close_ops_date")
        private String closeOpsDate;
        @JsonProperty(value = "start_ops_date")
        private String startOpsDate;
        @JsonProperty(value = "timezone_start_date")
        private String timezoneStartDate;
        @JsonProperty(value = "timezone_end_date")
        private String timezoneEndDate;
        @JsonProperty(value = "timezone_start_ops_date")
        private String timeZoneStartOpsDate;
        @JsonProperty(value = "timezone_end_ops_date")
        private String timeZoneEndOpsDate;
        @JsonProperty(value = "survivor_number")
        private Integer survivornumber;
        @JsonProperty(value = "lost_number")
        private Integer lostNumber;
        @JsonProperty(value = "light_injured_number")
        private Integer lightInjuredNumber;
        @JsonProperty(value = "heavy_injured_number")
        private Integer heavyInjuredNumber;
        @JsonProperty(value = "death_number")
        private Integer deathNumber;
        @JsonProperty(value = "owner_name")
        private String ownerName;
        @JsonProperty(value = "owner_address")
        private String ownerAddres;
        @JsonProperty(value = "owner_email")
        private String email;
        @JsonProperty(value = "owner_phone_number")
        private String phoneNumber;
        @JsonProperty(value = "owner_cellphone_number")
        private String cellPhone;
        @JsonProperty(value = "owner_fax")
        private String ownerFax;
        @JsonProperty(value = "object_call_sign")
        private String objectCallSign;
        @JsonProperty(value = "object_length")
        private String objectLength;
        @JsonProperty(value = "object_colour")
        private String objectColor;
        @JsonProperty(value = "object_home_base")
        private String objectHomeBase;

    }

    @Data
    public static class SightingHearing implements Serializable {

        private String date;
        private String location;
        private String object;
        private String description;
        private String latitude;
        private String longitude;
        private String status;

    }

    @Data
    public static class Sru implements Serializable {

        private String asset_type;
        private String name;
        private String potency_name;
        private String distance;
        private String angle;
        private String tanggal;
        private String kecepatan;
        private String endurance;

    }

    @Data
    public static class Personel implements Serializable {

        private String code;
        private String name;
        private String office_name;
        private String employer_class;
        private String tanggal;
        private String image;

    }

    @Data
    public static class Potency implements Serializable {

        private String potency_name;
        private String office_name;
        private String potency_type;
        private String phone_number;

    }

    @Data
    public static class Asset implements Serializable {

        private String asset_name;
        private String office_name;
        private Integer quantity;

    }

    @Data
    public static class Resource implements Serializable {

        private List<Personel> personnel;
        private List<Potency> potency;
        private List<Asset> asset;

    }

    @Data
    public static class Planning implements Serializable {

        private String date;
        private String name;

    }

    @Data
    public static class LogIncident implements Serializable {

        private String subject_log;
        private String content;
        private String date;
        private String time;
        private String initiator;
        private String latitude;
        private String longitude;
        private List picture;

    }

    @Data
    public static class Pob implements Serializable {

        private String name;
        private Integer age;
        private String gender;
        private String country;
        private String condition;
    }

    @Data
    public static class DriftCalculation implements Serializable {

        private Integer id;
        private String name;
        private String url;
    }
}
