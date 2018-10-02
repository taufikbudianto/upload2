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
 * @author Aris
 */
@Entity
@Table(name = "index_country")
public @Data
class IndexCountry extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "indexcountryid", length = 3)
    private String indexCountryId;
    @Column(name = "countryname", length = 100)
    private String countryName;
}
