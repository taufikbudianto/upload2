/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Ismail
 */
@Entity
@Table(name = "psa_app_role_task_mobile")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"roleTaskMobilePK"})
@ToString
@NoArgsConstructor
public class RoleTaskMobile extends AbstractAuditingEntity implements Serializable{
    @EmbeddedId
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private RoleTaskMobilePK roleTaskMobilePK;
    
    @Column(name = "access_right", length = 20)
    @Enumerated(EnumType.STRING)
    private Role.AccessRight accessRight;
    
    
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Role role;
    
    @JoinColumn(name = "task_id", referencedColumnName = "task_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TaskMobile task;

    public RoleTaskMobile(String taskId, int roleId) {
        this.roleTaskMobilePK = new RoleTaskMobilePK(taskId, roleId);
    }

    public RoleTaskMobile(TaskMobile task, Role role) {
        this.roleTaskMobilePK = new RoleTaskMobilePK(task.getTaskId(), role.getRoleId());
        this.task = task;
        this.role = role;
    }

    public RoleTaskMobile(TaskMobile task, Role role, Role.AccessRight accessRight) {
        this.roleTaskMobilePK = new RoleTaskMobilePK(task.getTaskId(), role.getRoleId());
        this.task = task;
        this.role = role;
        this.accessRight = accessRight;
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"taskId", "roleId"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RoleTaskMobilePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "task_id", length = 100)
    private String taskId;
    
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;
    
}