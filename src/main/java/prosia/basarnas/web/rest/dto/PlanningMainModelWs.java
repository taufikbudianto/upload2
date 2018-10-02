/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import java.io.Serializable;
import java.util.List;
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
public class PlanningMainModelWs implements Serializable {

    private List<PlanningWs1> worksheet1;
    private List<PlanningWs2> worksheet2;
    private List<PlanningWs3> worksheet3;
    private List<PlanningWs8> worksheet8;
}
