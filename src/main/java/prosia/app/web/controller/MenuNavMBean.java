/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import prosia.app.model.Role;
import prosia.app.model.Task;
import prosia.app.model.User;
import prosia.app.repo.TaskRepo;

/**
 *
 * @author Randy
 */
@Controller
@Scope("session")
public class MenuNavMBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SessionData sessionData;
    
    @Autowired
    private TaskRepo taskRepo;
    
    @Getter
    private User userSession;
    
    @Getter
    private LinkedHashMap<Task, MenuModel> menuMap;    

    @Override
    public void afterPropertiesSet() throws Exception {
        // get user_login
        try {
            this.userSession = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            this.userSession = null;
        }
        
        this.menuMap = new LinkedHashMap<>();
        if (this.userSession != null) {
            // save data to session
            for (Role role : userSession.getRoles()) {
                // user_role_task
                this.sessionData.getUserRoleTasks().addAll(role.getRoleTasks());
                
                // user_task_menu
                for (Task task : role.getTasks()) {
                    if (task.getTaskType().equals(Task.Type.MENU) && !sessionData.getUserTaskMenus().contains(task)) {
                        sessionData.getUserTaskMenus().add(task);
                    }
                }
            }
            
            // generate menu model
            this.menuMap = generateMenuModel();
        }
    }
        
    /**
     * generate menu from database
     * @return hash map contains menu model based on database model
     */
    private LinkedHashMap<Task, MenuModel> generateMenuModel() {
        LinkedHashMap<Task, MenuModel> map = new LinkedHashMap<>();
        
        // get menu_task (child of TopLevelMenu
        Task topLevelMenu = taskRepo.findOne(Task.TOP_LEVEL_MENU_TASK_ID);
        
        for (Task tabMenu : topLevelMenu.getChildrenByStatus(Task.Status.ACTIVE)) {
            // rendered tab_menu if user has permission
            if (sessionData.getUserTaskMenus().contains(tabMenu)) {
                // create menu content
                DefaultMenuModel menu = new DefaultMenuModel();
                
                for (Task content : getListChildTask(tabMenu)) {
                    menu.addElement(generateMenuChild(content));
                }
                
                // add to map menu if menu is not empty
                if (menu.getElements() != null && menu.getElements().size() > 0) {
                    map.put(tabMenu, menu);
                }
            }
        }
        
        return map;
    }
    
    /**
     * Get list of child task from its parent_menu and remove the child if it's not define in the 
     * role_task for the current user login.
     * @param parentMenu
     * @return 
     */
    private List<Task> getListChildTask(Task parentMenu) {
        List<Task> listChildTask = new ArrayList<>();        
        for (Task child : parentMenu.getChildrenByStatus(Task.Status.ACTIVE)) {
            if (sessionData.getUserTaskMenus().contains(child)) {
                listChildTask.add(child);
            }
        }
        
        return listChildTask;
    }
    
    /**
     * Generate child_menu for the specific parent_menu.
     * @param parentMenu
     * @return parentMenu as a MenuItem if it isn't a nested menu, and parentMenu as a SubMenu if it have 
     * another nested child_menu.
     */
    private MenuElement generateMenuChild(Task parentMenu) {
        List<Task> listChildMenu = getListChildTask(parentMenu);
        
        if (listChildMenu != null && listChildMenu.size() > 0) {
            DefaultSubMenu subMenu = new DefaultSubMenu();
            subMenu.setLabel(parentMenu.getTaskName());
            subMenu.setIcon(parentMenu.getIconName());
            for (Task child : listChildMenu) {
                subMenu.addElement(generateMenuChild(child));
            }
            return subMenu;
        } else {
            DefaultMenuItem menuItem = new DefaultMenuItem();
            menuItem.setValue(parentMenu.getTaskName());
            menuItem.setIcon(parentMenu.getIconName());
            menuItem.setOutcome(parentMenu.getOutcome());
            return menuItem;
        }
    }
    
    /**
     * @return list tab menu.
     */
    public Set<Task> getListMenu() {
        return this.menuMap != null ? this.menuMap.keySet() : new HashSet<>();
    }
    
    /**
     * @param tabMenu
     * @return menu content for specific tab menu.
     */
    public MenuModel getMenuContent(Task tabMenu) {
        return this.menuMap != null ? this.menuMap.get(tabMenu) : null;
    }
    
}
