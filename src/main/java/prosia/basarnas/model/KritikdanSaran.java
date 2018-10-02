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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;
/**
 *
 * @author PROSIA
 */
@Entity
@Table (name = "KRITIKDANSARAN")
public @Data

class KritikdanSaran extends AbstractAuditingEntity implements Serializable{
   
    @Id
    @Basic (optional = false)
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "KRITIKDANSARAN_SEQ_ID")
    @SequenceGenerator(sequenceName = "KRITIKDANSARAN_SEQ",allocationSize = 1, name="KRITIKDANSARAN_SEQ_ID")
    @Column(name = "KRITIKDANSARANID")
    private Integer kritikdansaranID;
    @Column (name = "PERSONNELID")
    private  String personnelID;
    @Column (name = "KRITIKDANSARAN")
    private String kritikdansaran;
}
