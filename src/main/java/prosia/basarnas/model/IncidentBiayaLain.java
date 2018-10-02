/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Taufik AB
 */
@Entity
@Table(name = "INC_BIAYA_LAIN")
@Data
public class IncidentBiayaLain extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "BIAYALAINID")
    private String biayaLainId;
    @Column(name = "DESKRIPSI")
    private String deskripsi;

    @ManyToOne
    @JoinColumn(name = "INCIDENTID")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Incident incident;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;

    @ManyToOne
    @JoinColumn(name = "JENISBIAYAID")
    //@fetch
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private MstJenisBiaya mstJenisBiaya;

    @Transient
    private Double total;
    @Transient
    private Boolean pilih;
    

}
