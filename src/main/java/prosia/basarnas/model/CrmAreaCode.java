/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "crm_areacode")
public @Data class CrmAreaCode extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="areacodeid")
    private int areaCodeID;
    @Column(name="code")
    private String code;
    @Column(name="area")
    private String area;
    @Column(name="latitude")
    private String latitude;
    @Column(name="longitude")
    private String longitude;
    @ManyToOne
    @JoinColumn(name = "regionid", nullable = true)
    @BatchSize(size = 20)
    private MstRegion region;
    @Column(name="kansar")
//    @Transient
    private String kansar;
}
