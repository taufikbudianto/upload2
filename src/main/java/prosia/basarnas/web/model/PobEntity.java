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
public @Data class PobEntity implements Serializable{
    private String nama;
    private String jk;
    private Integer usia;
    private Date dtDitemukan;
    private String koordinat;
}
