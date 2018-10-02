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
@Table(name = "MST_BBM")
@Data
public class MstBbm extends AbstractAuditingEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_BBM_ID")
    @SequenceGenerator(sequenceName = "SEQ_MST_BBM", allocationSize = 1, name = "SEQ_MST_BBM_ID")
    @Column(name = "BBMID", length = 15)
    private Integer bbmID;
    
    @Column(name = "JENIS")
    private String jenis;
    
    @Column(name = "NILAI")
    private double nilai;
    
}
