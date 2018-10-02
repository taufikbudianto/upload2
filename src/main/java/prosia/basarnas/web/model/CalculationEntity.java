/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author PROSIA
 */
public @Data
class CalculationEntity implements Serializable {
    private String sru;
    private String speed;
    private String trackSpacing;
    private String duration;
    private String searchArea;
}
