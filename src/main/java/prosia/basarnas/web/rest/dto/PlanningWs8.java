/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Taufik AB
 */
@Getter
@Setter
@ToString
public class PlanningWs8 implements Serializable {

    private Double weather_correction_factor;
    private Double speed_correction_factor;
    private Double winds_speed;
    private String search_platform;
    private String search_object;
    private String worksheet_name;
    private Double meteorlogical_visibility;
    private Double search_height;
    private String search_repeat;
    private Boolean fatigue_factor;
    private Double uncorrected_sweep_width;
    private Double fatigue_correction_factor;
    private Double sweep_width_factor;
    private Double practical_track_spacing;
    private Double coverage_factor;
    private Double probability_detection;
    private Double search_area;
    private Double search_hours;

}
