/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.enumeration;

import lombok.Getter;

/**
 *
 * @author Ismail
 */
public enum MapPatternType {
    SEARCH_AREA("Relative ke datum"),
    TASK_SEARCH_AREA("Berdasarkan Task Search Area");

    @Getter
    private final String name;

    MapPatternType(String name) {
        this.name = name;
    }
}
