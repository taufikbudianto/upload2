/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Taufik AB
 */
@Getter
@Setter
@ToString
public class PlanningWs1 implements Serializable {

    private String worksheet_name;
    private String description;
    private String latitude;
    private String longitude;
    private String incident_time;
    private String operation_time;
    private Double safety_factor;
    private String unit;
    private Double sea_current_angle;
    private Double sea_current_speed;
    private Double sea_current_distance;
    private Double surface_wind_angle;
    private Double wind_current_angle;
    private Double wind_current_speed;
    private Double wind_current_distance;
    private Double leeway_divergence_angle;
    private Double leeway_distance;
    private Double leeway_speed;
    private Double leeway_left_angle;
    private Double leeway_right_angle;
    private Double drift_left_distance;
    private Double drift_right_distance;
    private Double drif_left_to_right_distance;
    private Double drift_error;
    private Double drift_error_percentage;
    private Double distress_craft_error;
    private Double search_craft_error;
    private Double probable_error_total;
    private String drift_left_latitude;
    private String drift_left_longitude;
    private String drift_right_latitude;
    private String drift_right_longitude;
    private String datum_latitude;
    private String datum_longitude;
    private Double search_radius;
    private Double search_area;
    private Double drift_left_to_right_angle;
    private Double wind_current_speed_cal_distance;
    private Double incident_to_datum_angle;
    private SearchArea search_area_on_map;
    private SearchPattern search_pattern;
    private TaskSearch search_task_area;

    @Data
    public static class SearchArea implements Serializable {

        private List<Object> overlays;
    }

    @Data
    public static class SearchPattern implements Serializable {//"":"#000000","":0.1,"strokeWeight":3,"type":"Polygon","strokeColor":"#00FF00","strokeOpacity":3

        private List<Object> overlays;
    }

    @Data
    public static class TaskSearch implements Serializable {

        private List<Object> overlays;
    }

    @Data
    public static class Paths implements Serializable {

        private List<LatitudeLongitude> paths;
        private Double strokeWeight;
        private String type;
        private String strokeColor;
    }

    @Data
    public static class PathsTask implements Serializable {

        private List<LatitudeLongitude> paths;
        private Double strokeWeight;
        private Double fillOpacity;
        private Double strokeOpacity;
        private String type;
        private String strokeColor;
        private String fillColor;
    }

    @Data
    public static class Position implements Serializable {

        private LatitudeLongitude position;
        private String type;
        private String title;
    }

    @Data
    public static class LatitudeLongitude implements Serializable {

        private Double lng;
        private Double lat;
    }

}
