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
 * @author Irfan Rofie
 */
public @Data
class Theme implements Serializable {

    private int id;
    private String displayName;
    private String name;

    public Theme() {
    }

    public Theme(int id, String displayName, String name) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
    }
}
