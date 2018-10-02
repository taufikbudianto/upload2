/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_enumeration")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"enumId"})
@ToString
public class Enumeration extends AbstractAuditingEntity implements Serializable {

    public enum EnumerationType {
        ATTACHMENT_TYPE,
        CONTACT_MECHANISM_PURPOSE,
        DOCUMENT_TYPE,
        FACILITY,
        GEO_BOUNDARY,
        PARTY_ROLE_TYPE,
        PARTY_RELATIONSHIP_TYPE,
        TENANT_SUBSCRIPTION
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "enum_id")
    private Integer enumId;
    
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
    @ManyToOne
    private Tenant tenant;
    
    @Column(name = "type_", length = 50)
    @Enumerated(EnumType.STRING)
    private EnumerationType type;
    
    @Column(name = "enum_name", length = 100)
    private String enumName;
    
    @Column(name = "default_option")
    private Boolean defaultOption;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "sequence_")
    private Short sequence;
    
    @Column(name = "system_")
    private Boolean system;

    public Enumeration() {
    }

    public Enumeration(Tenant tenant, EnumerationType type, String enumName) {
        this.tenant = tenant;
        this.type = type;
        this.enumName = enumName;
    }
    
}
