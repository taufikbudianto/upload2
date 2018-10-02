/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.enumeration;

import lombok.Getter;

/**
 *
 * @author PROSIA
 */
public enum Checklistdetail {
    Awareness("Awareness"),
    Initial_Action("Initial Action"),
    Planning("Planning"),
    Operation("Operation"),
    Conclusion("Conclusion");
    
    @Getter
    private String capitalize;

    private Checklistdetail(String capitalize) {
        this.capitalize = capitalize;
    }
}
