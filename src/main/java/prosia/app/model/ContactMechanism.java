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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_contact_mechanism")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString
@NoArgsConstructor
public class ContactMechanism extends AbstractAuditingEntity implements Serializable {
    
    public enum MechanismType {
        POSTAL_ADDRESS,
        TELECOMMUNICATION_NUMBER,
        ELECTRONIC_ADDRESS
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "cm_id")
    private Integer id;
    
    @Column(name = "mechanism_type", length = 30)
    @Enumerated(EnumType.STRING)
    private MechanismType mechanismType;
    
    @Column(name = "address_1", length = 100)
    private String address1;
    
    @Column(name = "address_2", length = 100)
    private String address2;
    
    @Column(name = "directions", length = 100)
    private String directions;
    
    @ManyToOne
    @JoinColumn(name = "geo_id", referencedColumnName = "geo_id")
    private GeoBoundary geoBoundary;
    
    @Column(name = "country_code", length = 5)
    private String countryCode;
    
    @Column(name = "area_code", length = 5)
    private String areaCode;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "ext_number", length = 10)
    private String extNumber;
    
    @Column(name = "electronic_address", length = 100)
    private String electronicAddress;

    public ContactMechanism(MechanismType mechanismType) {
        this.mechanismType = mechanismType;
    }
    
}