/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
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
 * @author Taufik AB
 */
@Entity
@Table(name = "MST_JNS_BIAYA")
@Data
public class MstJenisBiaya extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MST_JNS_BIAYA_SEQ_ID")
    @SequenceGenerator(sequenceName = "MST_JNS_BIAYA_SEQ", allocationSize = 1, name = "MST_JNS_BIAYA_SEQ_ID")
    @Column(name = "JNSBIAYAID")
    private Integer jnsBiayaId;
    @Column(name = "NAMA")
    private String nama;
    @Column(name = "NILAI")
    private String nilai;

}
