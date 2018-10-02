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
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Aris
 */
@Entity
@Table(name = "index_leeway")
public @Data
class IndexLeeway extends AbstractAuditingEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "indexleewayid", length = 15)
    private String indexLeewayID;
    @Column(name = "category", length = 50)
    private String category;
    @Column(name = "subcategory", length = 50)
    private String subCategory;
    @Column(name = "leewaydescription", length = 150)
    private String leewayDescription;
    @Column(name = "multiplier")
    private Double multiplier;
    @Column(name = "modifier")
    private Double modifier;
    @Column(name = "angle")
    private Double angle;
}
