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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Ismail
 */
@Entity
@Table(name = "psa_app_task_mobile")
@Data
public class TaskMobile implements Serializable {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TASK_MOBILE_ID")
    @SequenceGenerator(sequenceName = "PSA_APP_TASK_MOBILE_SEQ", allocationSize = 1, name = "SEQ_TASK_MOBILE_ID")
    @Column(name = "task_id", length = 150)
    private String taskId;
    @Column(name = "task_name", length = 50)
    private String taskName;
    @Column(name = "isdeleted")
    private boolean deleted;
}
