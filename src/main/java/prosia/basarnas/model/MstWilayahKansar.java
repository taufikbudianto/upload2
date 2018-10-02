/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "mst_wil_kansar")

public @Data
class MstWilayahKansar extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "kantorsarname")
    private String kantorSarName;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;
    
    @Column(name = "kantorid")
    private Integer kantorId;
    
    @Column(name = "seq")
    private Integer seq;
}
