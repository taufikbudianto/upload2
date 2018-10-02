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
 * @author Taufik AB
 */
@Entity
@Table(name = "MST_BIAYA_PERSONNEL")
@Data
public class MstBiayaPesonnel extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_BIAYA_PERSONNEL_ID")
    @SequenceGenerator(sequenceName = "SEQ_MST_BIAYA_PERSONNEL", allocationSize = 1, name = "SEQ_MST_BIAYA_PERSONNEL_ID")
    @Column(name = "BIAYAPERSONNELID", length = 15)
    private Integer biayaPersonnelId;
    @Column(name = "KATEGORI")
    private String kansar;
    @Column(name = "BIAYA_MAKAN")
    private Double biayaMakan;
    @Column(name = "BIAYA_SUPLEMEN")
    private Double biayaSuplemen;

}
