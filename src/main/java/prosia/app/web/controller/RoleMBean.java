/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.model.Role;
import prosia.app.model.RoleTask;
import prosia.app.model.Task;
import prosia.app.model.Tenant;
import prosia.app.repo.RoleRepo;
import prosia.app.repo.TaskRepo;
import prosia.app.repo.TenantRepo;
import prosia.app.service.RoleService;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;

/**
 *
 * @author Randy
 */
@Controller
@Scope("view")
public class RoleMBean extends AbstractManagedBean implements InitializingBean {
    
    @Getter
    private boolean isDuplicate;
    @Getter
    private boolean isNewRole;
    @Getter
    private boolean isShowDetail;
    @Getter
    @Setter
    private boolean isDisabled;
    @Getter
    @Setter
    private String roleFilter;
    @Getter
    @Setter
    private String headerDetail;
    @Getter
    @Setter
    private Integer idTempRole;
    
    @Getter
    @Setter
    private Role roleDetail;
    @Getter
    @Setter
    private Role targetRole;
    @Getter
    @Setter
    private TreeNode menus;
    @Getter
    @Setter
    private TreeNode[] selectedMenus;
    
    @Getter
    private LazyDataModelJPA<Role> rolesList;
    @Getter
    @Setter
    private List<Role> selectedRoles;
    @Getter
    @Setter
    private List<RoleTask> selectedNavFields;
    @Getter
    @Setter
    private List<RoleTask> selectedRoleTasks;
    @Getter
    private List<SelectItem> accessRights;
    @Getter
    private List<Tenant> tenants;
    
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TenantRepo tenantRepo;
    
    @Override
    protected List<SecureItem> getSecureItems() {
        List<SecureItem> secureItems = new ArrayList<>();
        
        secureItems.add(new SecureItem("CreateButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("EnabledButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("DisabledButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("DuplicateButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("SaveButton", Task.Type.ACTION));
        
        secureItems.add(new SecureItem("RoleNameInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("InitialAccessInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("TenantInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("MenuEdit", Task.Type.FIELD));
        secureItems.add(new SecureItem("NavigationFieldEdit", Task.Type.FIELD));
        
        return secureItems;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        if (getCurrentUser().getFirstRole() != null && getCurrentUser().getFirstRole().getSystem()) {
            this.rolesList = new LazyDataModelJPA(roleRepo) {
                @Override
                protected long getDataSize() {
                    if (roleFilter != null && !roleFilter.isEmpty()) {
                        return roleRepo.countByRoleNameContaining(roleFilter);
                    } else {
                        return super.getDataSize();
                    }
                }
                
                @Override
                protected Page getDatas(PageRequest request) {
                    if (roleFilter != null && !roleFilter.isEmpty()) {
                        return roleRepo.findAllByRoleNameContaining(roleFilter, request);
                    } else {
                        return super.getDatas(request);
                    }
                }
            };
        } else {
            this.rolesList = new LazyDataModelJPA(roleRepo) {
                @Override
                protected long getDataSize() {
                    if (roleFilter != null && !roleFilter.isEmpty()) {
                        // return roleRepo.countByRoleNameAndCurrentTenant(roleFilter);
                        return roleRepo.count(whereQuery(roleFilter));
                    } else {
                        // return roleRepo.countByCurrentTenant();
                        return roleRepo.count(whereQuery(roleFilter));
                    }
                }
                
                @Override
                protected Page getDatas(PageRequest request) {
                    if (roleFilter != null && !roleFilter.isEmpty()) {
                        // return roleRepo.findAllByRoleNameAndCurrentTenant(roleFilter, request);
                        return roleRepo.findAll(whereQuery(roleFilter), request);
                    } else {
                        // return roleRepo.findAllByCurrentTenant(request);
                        return roleRepo.findAll(whereQuery(roleFilter), request);
                    }
                }
            };
        }
        
        isShowDetail = false;

        // load available menus
        boolean isSelectable = isSecureItemEditable("MenuEdit");
        Task topMenu = taskRepo.findOne(Task.TOP_LEVEL_MENU_TASK_ID);
        
        menus = new CheckboxTreeNode(topMenu, null);
        menus.setSelectable(isSelectable);
        
        generatedMenuTree(menus, topMenu.getChildrenByTypeAndStatus(
                Task.Type.MENU, Task.Status.ACTIVE), isSelectable);

        // load select_items
        this.accessRights = new ArrayList<>();
        for (Role.AccessRight accessRight : Role.AccessRight.values()) {
            this.accessRights.add(new SelectItem(accessRight, viewBean.toFirstUppercase(accessRight.name())));
        }

        // load tenants
        if (getCurrentUser().getFirstRole().getSystem()) {
            this.tenants = tenantRepo.findAllByEnabled(true);
        } else {
            this.tenants = new ArrayList<>();
        }
    }

    /**
     * Show role's detail. If the role parameter is null, this method will show
     * empty details and insert a new role data when submitted.
     *
     * @param role
     */
    public void loadDetail(Role role) {
        isShowDetail = true;
        isDuplicate = false;
        selectedNavFields = new ArrayList<>();
        menus.setSelected(false);
        
        if (role != null) {
            roleDetail = role;
            isNewRole = false;
            isDisabled = false;
            headerDetail = "Ubah Hak Akses";

            // update tree menu selections
            updateSelectedMenus();
        } else {
            // create new user
            roleDetail = new Role();
            isNewRole = true;
            isDisabled = false;
            headerDetail = "Tambah Hak Akses";

            // reset to empty menu
            selectedMenus = null;
        }
        
        selectedRoleTasks = new ArrayList<>(roleDetail.getRoleTasks());
    }

    /**
     * Update menu tree selections. Check the menu if it's already define in
     * selected role's tasks.
     */
    private void updateSelectedMenus() {
        List<TreeNode> assignedMenus = new ArrayList<>();
        assignedMenus = getSelectedMenuTreeNode(menus, roleDetail.getTasksByType(Task.Type.MENU), assignedMenus);
        
        selectedMenus = assignedMenus.toArray(new TreeNode[assignedMenus.size()]);
    }

    /**
     * Get the selected TreeNode from the parent TreeNode. The TreeNode is
     * selected when it's Task is in selectedTasks.
     *
     * @param parent
     * @param selectedTasks
     * @param selectedNodes
     * @return
     */
    private List<TreeNode> getSelectedMenuTreeNode(
            TreeNode parent, List<Task> selectedTasks, List<TreeNode> selectedNodes) {
        
        for (TreeNode child : parent.getChildren()) {
            Task childTask = (Task) child.getData();
            
            if (selectedTasks.contains(childTask)) {
                // selected checkbox
                if (child.isLeaf()) {
                    child.setSelected(true);
                }
                selectedNodes.add(child);
            }

            // check the child tree
            if (!child.isLeaf()) {
                selectedNodes = getSelectedMenuTreeNode(child, selectedTasks, selectedNodes);
            }
        }
        return selectedNodes;
    }

    /**
     * Get all task children and add the tasks into the parent tree node.
     *
     * @param parent
     * @param children
     */
    private void generatedMenuTree(TreeNode parent, List<Task> children, boolean isSelectable) {
        for (Task child : children) {
            TreeNode childNode = new CheckboxTreeNode(child, parent);
            childNode.setSelectable(isSelectable);
            
            List<Task> newChild = child.getChildrenByTypeAndStatus(Task.Type.MENU, Task.Status.ACTIVE);
            if (newChild.size() > 0) {
                generatedMenuTree(childNode, newChild, isSelectable);
            }
        }
    }

    /**
     * Change view back to role list.
     */
    public void hideDetail() {
        this.isShowDetail = false;
    }

    /**
     * CLose pop-up message that triggered from method saveDetail, and change
     * the view back to role list.
     */
    public void closeSaveInformationDialog() {
        RequestContext.getCurrentInstance().execute("PF('saveNotification').hide()");
        hideDetail();
    }

    /**
     * Create a new role and automatically insert menus, navigations, and fields
     * for the new role with the menus, navigations, and fields from the
     * selected role.
     */
    public void duplicateSelectedRole() {
        if (this.selectedRoles == null || this.selectedRoles.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                    getMessageLocale("error_role_no_role_selected"));
            
        } else if (this.selectedRoles.size() > 1) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                    getMessageLocale("error_role_duplicating_selecting"));
            
        } else {
            // show detail's form
            loadDetail(null);
            
            this.isDuplicate = true;

            // get the target role's detail
            this.targetRole = this.selectedRoles.get(0);
            this.roleDetail.setInitialAccess(this.targetRole.getInitialAccess());
        }
    }

    /**
     * Update status of the selected roles.
     *
     * @param enabled
     */
    public void enabledOrDisabledSelectedRoles(boolean enabled) {
        if (this.selectedRoles == null || this.selectedRoles.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                    getMessageLocale("error_role_no_role_selected"));
            
        } else {
            roleService.updateRolesStatus(this.selectedRoles, enabled);
            addPopUpMessage(FacesMessage.SEVERITY_INFO, getMessageLocale("header_success"),
                    getMessageLocale("data_success_update"));
        }
    }

    /**
     * Show list of navigations and fields for selected menu.
     *
     * @param menu
     */
    public void showNavigationFields(Task menu) {
        try {
            showMenuAndNavigationFields();

            // save nav_fields changes
            saveNavFields();
            
            List<Task> listTasks = menu.getChildrenByTypeAndStatus(Task.Type.ACTION, Task.Status.ACTIVE);
            listTasks.addAll(menu.getChildrenByTypeAndStatus(Task.Type.FIELD, Task.Status.ACTIVE));
            
            selectedNavFields = new ArrayList<>();

            // get navigations and fields
            for (Task navFields : listTasks) {
                RoleTask roleTask = new RoleTask(navFields, roleDetail);
                
                int index = selectedRoleTasks.indexOf(roleTask);
                if (index != -1) {
                    roleTask.setAccessRight(selectedRoleTasks.get(index).getAccessRight());
                } else {
                    roleTask.setAccessRight(roleDetail.getInitialAccess());
                }
                selectedNavFields.add(roleTask);
            }
        } catch (Exception e) {
            log.error("Role Id belum ada, hanya utk edit");
        }
    }

    /**
     * Get the navigations and fields changes and add to selectedRoleTasks to
     * later been committed to database.
     */
    private void saveNavFields() {
        if (selectedNavFields != null && !selectedNavFields.isEmpty()) {
            for (RoleTask navFields : selectedNavFields) {
                int index = selectedRoleTasks.indexOf(navFields);
                if (index != -1) {
                    // update access_right if has the role_task
                    selectedRoleTasks.get(index).setAccessRight(navFields.getAccessRight());
                } else {
                    // insert new role_task if don't have
                    selectedRoleTasks.add(navFields);
                }
            }
        }
    }

    /**
     * Save a new role's data and show menu and navigation & field.
     */
    public void showMenuAndNavigationFields() {
//        idTempRole = roleRepo.findByRoleId().getRoleId() + 1;
        if (roleDetail != null) {
            try {
                // set default tenant if null
                if (roleDetail.getTenant() == null) {
                    roleDetail.setTenant(getCurrentUser().getDefaultTenant());
                }

                // check duplicate role
                if (roleRepo.findOneByRoleIdentifier(roleDetail.getRoleIdentifier()) != null) {
                    addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                            getMessageLocale("error_role_duplicate_identifier"));
                    return;
                }

//                roleDetail.setRoleId(idTempRole);
                roleDetail.setEnabled(true);
//                roleDetail = roleService.createNewRole(roleDetail);

                // copy menus, navigations, and fields if isDuplicate
                if (isDuplicate) {
                    // copy role tasks
                    roleDetail = roleService.copyRoleTasksTo(roleDetail, targetRole.getRoleTasks());
                    // update selectedRoleTasks
                    selectedRoleTasks = new ArrayList<>(roleDetail.getRoleTasks());

                    // update tree menu selections
                    updateSelectedMenus();
                }
                
                isNewRole = false;
                
            } catch (Exception ex) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header"),
                        getMessageLocale("error_role_create"));
                
                log.error("Failed create a new role : ", ex);
            }
        }
    }

    /**
     * Check if the specified menu and its children had been selected or
     * unselected. If it's selected, then add the menu into selectedRoleTasks to
     * later been committed to database. Otherwise, if it's unselected, then
     * remove the menu from selectedRoleTasks.
     */
    private void saveMenus(TreeNode menu) {
        // menu is selected
        if (menu.isSelected() || menu.isPartialSelected()) {
            // insert into role task if not exists
            RoleTask menuRoleTask = new RoleTask((Task) menu.getData(), roleDetail,
                    roleDetail.getInitialAccess());
            
            if (!selectedRoleTasks.contains(menuRoleTask)) {
                selectedRoleTasks.add(menuRoleTask);
                log.debug("a new role_task has been added : {}", menu.getData());
            }
            
        } else { // menu is unselected

            // remove from role task if exists
            RoleTask menuRoleTask = new RoleTask((Task) menu.getData(), roleDetail);
            
            int index = selectedRoleTasks.indexOf(menuRoleTask);
            if (index != -1) {
                selectedRoleTasks.remove(index);
                log.debug("a role_task has been removed : {}", menu.getData());
            }
        }

        // check menu's children
        for (TreeNode child : menu.getChildren()) {
            saveMenus(child);
        }
    }

    /**
     * Save role's detail or create a new role. This method will called the
     * pop-up message to informs the result.
     */
    public void saveDetail() {
        if (menus.isSelected() || menus.isPartialSelected()) {
            roleRepo.save(roleDetail);

            // save menus changes
            for (TreeNode menu : menus.getChildren()) {
                saveMenus(menu);
            }

            // save nav_fields changes
            saveNavFields();
            
            try {
                // update role's data
                roleService.updateRoleTasks(roleDetail, selectedRoleTasks);
                RequestContext.getCurrentInstance().execute("PF('saveNotification').show()");
            } catch (Exception ex) {
                addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header"),
                        getMessageLocale("error_role_save"));
                
                log.error("Failed save role's detail : ", ex);
            }
        } else {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, "Peringatan", "Pilih minimal 1 Menu!");
        }
        
    }
    
    public void hapus(Role role) {
        roleRepo.delete(role);
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Data hak akses berhasil dihapus!");
    }
    
    public void view(Role role) {
        loadDetail(role);
        isDisabled = true;
        headerDetail = "Lihat Hak Akses";
    }
    
    private Specification<Role> whereQuery(
            final String value) {
        List<Predicate> predicates = new ArrayList<>();
        
        return new Specification<Role>() {
            
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(cb.like(cb.lower(root.<String>get("roleName")), getLikePattern(value.trim())));
                }
//                predicates.add(cb.notEqual(root.<Boolean>get("activated"), false));
//                predicates.add(cb.notEqual(root.<Boolean>get("enabled"), false));
//                query.orderBy(cb.desc(root.get("dateCreated")));
                return andTogether(predicates, cb);
            }
        };
    }
    
    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
    
    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }
}
