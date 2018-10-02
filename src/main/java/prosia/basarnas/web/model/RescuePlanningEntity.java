/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Ismail
 */
public @Data class RescuePlanningEntity implements Serializable {

    private Date datetime;
    private String metode;
    private String createBy;
}
