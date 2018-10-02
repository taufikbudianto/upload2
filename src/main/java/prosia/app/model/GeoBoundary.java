/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_geo_boundary")
@EqualsAndHashCode(callSuper = false, of = {"geoId"})
@ToString(exclude = "childGeoBoundaries")
@NoArgsConstructor
public class GeoBoundary extends AbstractAuditingEntity implements Serializable {
    
    public static final String GEO_BOUNDARY_COUNTRY = "COUNTRY";
    public static final String GEO_BOUNDARY_CITY = "CITY";
    public static final String GEO_BOUNDARY_PROVINCE = "PROVINCE";
    public static final String GEO_BOUNDARY_DISTRICT = "KECAMATAN";
    public static final String GEO_BOUNDARY_SUB_DISTRICT = "KELURAHAN";
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "geo_id")
    @Getter
    @Setter
    private Integer geoId;
    
    @JoinColumn(name = "boundary_type_id", referencedColumnName = "enum_id")
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Enumeration boundaryType;
    
    @JoinColumn(name = "tenant_id", referencedColumnName = "tenant_id")
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Tenant tenant;
    
    @Column(name = "geo_code", length = 5)
    @Getter
    @Setter
    private String geoCode;
    
    @Column(name = "geo_name", length = 50)
    @Getter
    @Setter
    private String geoName;
    
    @Column(name = "abbreviation", length = 10)
    @Getter
    @Setter
    private String abbreviation;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "psa_geo_bound_association",
            joinColumns = {
                @JoinColumn(name = "parent_id", referencedColumnName = "geo_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "child_id", referencedColumnName = "geo_id")},
            uniqueConstraints = {
                @UniqueConstraint(columnNames = {"parent_id", "child_id"})})
    private Set<GeoBoundary> childGeoBoundaries;

    public GeoBoundary(Enumeration boundaryType, Tenant tenant, String geoCode, String geoName) {
        this.boundaryType = boundaryType;
        this.tenant = tenant;
        this.geoCode = geoCode;
        this.geoName = geoName;
    }
    
    /**
     * Add a new child to this GeoBoundary object. As a note, after call this method, save method must be called 
     * from repository class to commit the new child to database.
     * @param child 
     * @throws java.lang.Exception when child is already exists.
     */
    public void addChild(GeoBoundary child) throws Exception {
        if(childGeoBoundaries == null) {
            childGeoBoundaries = new HashSet<>();
        }
        
        boolean result = childGeoBoundaries.add(child);
        
        if (!result) {
            throw new Exception("Child is already exists.");
        }
    }
    
    /**
     * Remove a specific child from this GeoBoundary object. As a note, after call this method, save method must 
     * be called from repository class to remove the child from database.
     * @param child 
     * @throws java.lang.Exception when child is not exists.
     */
    public void removeChild(GeoBoundary child) throws Exception {
        if (childGeoBoundaries == null) {
            throw new Exception("Child is empty.");
        }
        
        boolean result = childGeoBoundaries.remove(child);
        
        if (!result) {
            throw new Exception("Child is not exists.");
        }
    }

    public Set<GeoBoundary> getChilds() {
        return Collections.unmodifiableSet(childGeoBoundaries != null ? childGeoBoundaries : childGeoBoundaries);
    }
    
}
