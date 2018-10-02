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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
@Table(name = "psa_app_setting")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"prefixName"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Setting implements Serializable {

    public static final String X_DATE_FORMAT = "date_format";
    public static final String X_DATE_TIME_FORMAT = "date_time_format";
    public static final String X_DECIMAL_FORMAT = "decimal_format";
    public static final String X_INTEGER_FORMAT = "integer_format";
    public static final String X_MULTI_TENANCY = "multi_tenancy";
    public static final String X_MULTI_ROLES = "multi_roles";
    public static final String X_ROWS_PER_PAGE_DEFAULT = "rows_per_page_default";
    public static final String X_ROWS_PER_PAGE_TEMPLATE = "rows_per_page_template";
    
    @Id
    @Basic(optional = false)
    @Column(name = "prefix_name", length = 50)
    private String prefixName;
    
    @Size(max = 100)
    @Column(name = "value_", length = 100)
    private String value;
    
    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;
    
}
