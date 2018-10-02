/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.model.RoleTask;
import prosia.app.model.Task;

/**
 * Use this class to store values in the session cache.
 * @author Randy
 */
@Controller
@Scope("session")
public class SessionData {
    
    @Getter
    private final List<RoleTask> userRoleTasks;
    @Getter
    private final List<Task> userTaskMenus;

    public SessionData() {
        userTaskMenus = new ArrayList<>();
        userRoleTasks = new ArrayList<>();
    }
    
}
