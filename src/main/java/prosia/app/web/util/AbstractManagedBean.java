/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import prosia.app.model.Role;
import prosia.app.model.RoleTask;
import prosia.app.web.model.SecureItem;
import prosia.app.model.Task;
import prosia.app.model.User;
import prosia.app.repo.TaskRepo;
import prosia.app.service.TaskService;
import prosia.app.web.controller.ApplicationData;
import prosia.app.web.controller.ResourceBundleBean;
import prosia.app.web.controller.SessionData;
import prosia.app.web.controller.ViewBean;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.MapCalculation;

/**
 * Extends this class for all the ManagedBean classes that handles menu view. That class will be registered as 'task' 
 * with 'TopLevelMenu' parent and the secure objects that been declared will also be registered as 'task' 
 * with '{managed_bean_package_name}' parent.
 * @author Randy
 */
public abstract class AbstractManagedBean implements Serializable {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    protected Task currentTask;
    
    private List<SecureItem> secureItems;
    
    @Autowired
    protected ApplicationData applicationData;
    @Autowired
    private ResourceBundleBean messages;
    @Autowired
    protected SessionData sessionData;
    @Autowired
    protected ViewBean viewBean;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private TaskRepo taskRepo;
    
    public AbstractManagedBean() {
    }
    
    @PostConstruct
    public void init() {        
        // get menu_task
        this.currentTask = taskRepo.findOne(getTaskId());
        
        // insert child_task
        insertChildTask();
    }
    
    /**
     * @return the current user that has been login to system.
     */
    protected User getCurrentUser() {
        try {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Insert child_task.
     */
    private void insertChildTask() {
        // load secure_item
        this.secureItems = getSecureItems();
        
        if (this.currentTask == null || this.secureItems == null) {
            return;
        }
        
        short sequence = 0;
        
        for (SecureItem secureItem : this.secureItems) {
            // get task_type
            String taskId = this.currentTask.getTaskId() + "." + secureItem.getItemName();
            
            Task.Type taskType = null;
            
            if (secureItem.getItemType().equals(Task.Type.ACTION) &&
                    !isTaskExistInList(taskId, this.applicationData.getListTaskAction())) {
                taskType = Task.Type.ACTION;
                
            } else if (secureItem.getItemType().equals(Task.Type.FIELD) && 
                    !isTaskExistInList(taskId, this.applicationData.getListTaskField())) {
                taskType = Task.Type.FIELD;
            }
            
            // add sequence
            sequence++;

            // insert navigation_field if not exists
            if (taskType != null) {
                Task childTask = new Task(taskId, 
                        viewBean.addWhiteSpaceBetweenCapitalLetters(secureItem.getItemName()), taskType);
                childTask.setStatus(Task.Status.ACTIVE);
                
                try {
                    taskService.insertTask(childTask, getCurrentUser().getRoles(), this.currentTask, sequence);
                    
                    log.info("A new task had been inserted : {}", taskId);
                } catch (Exception e) {
                    log.error("Failed to insert a new task {} : {}", taskId, e);
                }
            }
        }
    }
    
    /**
     * Get the access_right for the specific secure_item_name.
     * @param secureItem
     * @return 
     */
    protected final Role.AccessRight getSecureItemAccessRight(String secureItem) {
        if (this.currentTask == null) {
            return Role.AccessRight.NO_ACCESS;
        }
        
        String secureItemId = this.currentTask.getTaskId() + "." + secureItem;
        for (RoleTask roleTask : this.sessionData.getUserRoleTasks()) {
            if (roleTask.getTask().getTaskId().equals(secureItemId) 
                    && roleTask.getTask().getStatus().equals(Task.Status.ACTIVE) 
                    && roleTask.getRole().getTenant().equals(getCurrentUser().getDefaultTenant())) {
                return roleTask.getAccessRight();
            }
        }
        return Role.AccessRight.NO_ACCESS;
    }
    
    /**
     * Validate if the secure_item is visible or not.
     * @param secureItem
     * @return 'true' if visible and 'false' if hide/collapse.
     */
    public final boolean isSecureItemVisible(String secureItem) {
        return !getSecureItemAccessRight(secureItem).equals(Role.AccessRight.NO_ACCESS);
    }
    
    /**
     * Validate if the secure_item could be edited or not. Use this result 
     * to define editable secure_item for form component.
     * @param secureItem
     * @return 'true' if editable and 'false' if not editable.
     */
    public final boolean isSecureItemEditable(String secureItem) {
        return getSecureItemAccessRight(secureItem).equals(Role.AccessRight.READ_WRITE);
    }
    
    /**
     * Validate if the secure_item is read_only or not.
     * @param secureItem
     * @return 'true' if read_only and 'false' if could be access.
     */
    public final boolean isSecureItemReadOnly(String secureItem) {
        return getSecureItemAccessRight(secureItem).equals(Role.AccessRight.READ_ONLY) 
                || getSecureItemAccessRight(secureItem).equals(Role.AccessRight.NO_ACCESS);
    }

    protected void addMessage(String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    protected void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary, detail));
    }

    protected void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    protected void addPopUpMessage(String summary) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary));
    }

    protected void addPopUpMessage(String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary, detail));
    }

    protected void addPopUpMessage(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }
    
    /**
     * @param key
     * @return message locale.
     */
    protected String getMessageLocale(String key) {
        return messages.get(key);
    }
    
    /**
     * @param taskId
     * @param source
     * @return true if taskId already exists in the source list.
     */
    protected boolean isTaskExistInList(String taskId, List<Task> source) {
        boolean result = false;
        for (Task task : source) {
            if (task.getTaskId().equals(taskId)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    /**
     * @return the full package with class name.
     */
    protected String getTaskId() {
        return getClass().getName();
    }
    
    /**
     * @return the class name without its package.
     */
    protected String getTaskName() {
        return getClass().getSimpleName();
    }
    
    /**
     * @return the secure_items from the current task.
     */
    protected abstract List<SecureItem> getSecureItems();
    
    public static Object getRequestParam(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        Map<String, Object> map = component.getAttributes();

        return map.get(name);
    }
    
     /**
     * @param long1
     * @param lat1
     * @param long2
     * @param lat2
     * @return data distance and angle.
     */
    protected Map<String,Double> setDistanceAndAngle(String long1,String lat1,String long2, String lat2){
        Map<String,Double> map = new HashMap<>();
        Double longFrom = Double.valueOf(long2);
        Double latFrom = Double.valueOf(lat2);
        Double longTo = Double.valueOf(long1);
        Double latTo = Double.valueOf(lat1);
        Double distance = MapCalculation.calculateDistanceInNm(latFrom, longFrom, latTo, longTo);
        distance = DecimalUtil.rounding(distance);
        Double angle = MapCalculation.calculateAngle(longFrom, latFrom, longTo, latTo);
        angle = MapCalculation.convertDegreeToPositive(angle);
        angle = DecimalUtil.rounding(angle);
        map.put("distance", distance);
        map.put("angle", angle);
        return map;
    }
    
    
}
