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
public enum MapSearch {
    SEARCH_PATTERN("Search Pattern"),
    LAYER("Layer"),
    MAPPING("Mapping"),
//    SEARCH_RESULT("Search Result"),
    ROUTING("Routing"),
    TASK_AREA("Task Area"),
    PLACEMARK("Placemark");

    @Getter
    private final String name;

    MapSearch(String name) {
        this.name = name;
    }
;
}
