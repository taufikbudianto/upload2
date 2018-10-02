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
 * @author PROSIA
 */
@Entity
@Table(name = "gtw_groupdetail")
public @Data class GtwGroupDetail extends AbstractAuditingEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "groupdetailid")
    private String groupDetailID;
//    @ManyToOne
//    @JoinColumn(name = "groupid", nullable = false)
//    @Fetch(FetchMode.JOIN)
//    @BatchSize(size = 25)
//    private GtwGroup gtwGroup;
    @Column(name = "groupid", length = 15)
    private String groupID;
    @Column(name = "personnelid", length = 15)
    private String personnelID;
    //helper
    @Transient
    private String recipientName;
    @Transient
    private String emailAddress;
    @Transient
    private String smsPhoneNumber;
    
}
