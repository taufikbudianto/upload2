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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Irfan Rofie
 */
@Entity
@Table(name = "CP_NOTIF_PERSONIL")
public @Data
class CPNotifPersonil extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NOTIF_PERSONIL_ID")
    @SequenceGenerator(sequenceName = "SEQ_NOTIF_PERSONIL", allocationSize = 1, name = "SEQ_NOTIF_PERSONIL_ID")
    @Column(name = "NOTIF_PERSONIL_ID")
    private Integer notifPersonilId;

    @JoinColumn(name = "NOTIF_ID", referencedColumnName = "NOTIF_ID")
    @ManyToOne(optional = true)
    private CPNotifikasi notifikasi;
    
    @JoinColumn(name = "PERSONNEL_ID", referencedColumnName = "PERSONNELID")
    @ManyToOne(optional = true)
    private ResPersonnel personnel;
}