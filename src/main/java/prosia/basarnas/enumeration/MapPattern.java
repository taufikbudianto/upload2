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
public enum MapPattern {
    SECTOR_SEARCH("Sector Search", "sector"),
    EXPANDING_SQUARE_SEARCH("Expanding Square Search", "square"),
    PARAREL_SEARCH("Pararel Search", "pararel"),
    TRACK_SEARCH_RETURN_SEARCH("Track Search Return Search", "tsr"),
    TRACK_SEARCH_NON_RETURN_SEARCH("Track Search not-Return Search", "tsn"),
    TRAPEZIUM_PARAREL_SEARCH("Trapezium Pararel Search", "trapezium"),
    SEARCH_PATTERN_FREE_DEFINE("Search Pattern Free Define","free_define");

    @Getter
    private final String name;
    @Getter
    private final String type;

    MapPattern(String name, String type) {
        this.name = name;
        this.type = type;
    }
;
}
