/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
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
public class TrapeziumWs implements Serializable {

    private List<TrapArea> TrapeziumArea;
    private List<TrapTaskArea> trapezium_task_area;

    @Data
    public static class TrapArea {
        private String id;
        private String worksheetname;
        private String description;
        private String operation_time;
        private Double latitude_kp;
        private Double longitude_kp;
        private Double latitude_dest;
        private Double longitude_dest;
        private Double safety_factor;
        private Double distress_error;
        private Double search_error;
        private Double area_space;
        private String task_area_last_point;
        private Double total_task_area_length;
        private Double unit;
        private Double waypoint;
        
    }

    @Data
    public static class TrapTaskArea {

        private String id;
        private String name;
        private String description;
        private Double task_area_length;
        private String split;
        private Double task_area_space;
        private String small_coord_1;
        private String small_coord_2;
        private String big_coord_1;
        private String big_coord_2;
    }
}
