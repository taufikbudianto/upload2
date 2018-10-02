/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Taufik
 */
@Data
public class CalenderWs implements Serializable {

    private String title;
    private Boolean all_day;
    private String start_date;
    private String end_date;
    private String repeat;
    private Integer reminder;
    
}
