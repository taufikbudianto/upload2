/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "TASK")
public @Data class TaskEmail implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "TASK_ID")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long taskId;
    @Basic(optional = false)
    @Column(name = "TASK_NAME")
    private String taskName;
    @Basic(optional = false)
    @Column(name = "TASK_TYPE")
    private String taskType;
    @Basic(optional = false)
    @Column(name = "TASK_TYPE_ID")
    private int taskTypeId;
    @Lob
    @Column(name = "PARAMETERS")
    private String parameters;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Lob
    @Column(name = "EXECUTION_MESSAGE")
    private String executionMessage;
    @Basic(optional = false)
    @Column(name = "TIME_SUBMITTED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSubmitted;
    @Column(name = "TIME_EXECUTED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeExecuted;
    @Column(name = "TIME_FINISHED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFinished;
}
