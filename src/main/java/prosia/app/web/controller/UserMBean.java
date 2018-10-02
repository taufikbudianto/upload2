/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.math.BigInteger;
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
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import prosia.app.model.Party;
import prosia.app.model.Role;
import prosia.app.model.Setting;
import prosia.app.model.Task;
import prosia.app.model.User;
import prosia.app.repo.PartyRepo;
import prosia.app.repo.RoleRepo;
import prosia.app.repo.SettingRepo;
import prosia.app.repo.UserRepo;
import prosia.app.service.UserService;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.app.web.util.LazyDataModelJPA;

/**
 *
 * @author Randy
 */
@Controller
@Scope("view")
public class UserMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private SettingRepo settingRepo;
    @Autowired
    private PartyRepo partyRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Getter
    @Setter
    private DualListModel<Role> roles;
    @Getter
    private LazyDataModelJPA<User> usersList;
    @Getter
    private List<Role> availableRoles;
    @Getter
    private List<SelectItem> rolesList;
    @Getter
    @Setter
    private List<User> selectedUsers;
    @Getter
    @Setter
    private List<Party> listParty;
    @Getter
    @Setter
    private Role selectedRole;
    @Getter
    private User userDetail;

    @Getter
    @Setter
    private boolean isGeneratedPassword;
    @Getter
    private boolean isMultipleRoles;
    @Getter
    private boolean isShowDetail;
    @Getter
    @Setter
    private boolean isDisabled;
    @Getter
    @Setter
    private String fullNameInput;
    @Getter
    @Setter
    private String userNameFilter;
    @Getter
    @Setter
    private String passwordInput;
    @Getter
    @Setter
    private String headerDetail;

    @Override
    protected List<SecureItem> getSecureItems() {
        List<SecureItem> secureItems = new ArrayList<>();

        secureItems.add(new SecureItem("CreateButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("LockButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("UnlockButton", Task.Type.ACTION));
        secureItems.add(new SecureItem("SaveButton", Task.Type.ACTION));

        secureItems.add(new SecureItem("UsernameInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("PasswordInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("FullNameInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("RoleInput", Task.Type.FIELD));
        secureItems.add(new SecureItem("GeneratePasswordCheckBox", Task.Type.FIELD));
        secureItems.add(new SecureItem("MustChangePasswordCheckBox", Task.Type.FIELD));

        return secureItems;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // initiate users_list
        if (getCurrentUser().getFirstRole() != null && getCurrentUser().getFirstRole().getSystem()) {
            this.usersList = new LazyDataModelJPA(userRepo) {
                @Override
                protected long getDataSize() {
                    if (userNameFilter != null && !userNameFilter.isEmpty()) {
                        return userRepo.countByLoginContaining(userNameFilter);
//                        return userRepo.count(whereQuery(userNameFilter));
                    } else {
                        return super.getDataSize();
                    }
                }

                @Override
                protected Page getDatas(PageRequest request) {
                    if (userNameFilter != null && !userNameFilter.isEmpty()) {
                        return userRepo.findAllByLoginContaining(userNameFilter, request);
//                        return userRepo.findAll(whereQuery(userNameFilter),request);
                    } else {
                        return super.getDatas(request);
                    }
                }

            };
            
            // initiate available_roles
            this.availableRoles = roleRepo.findAllByEnabled(true);
        } else {
            System.out.println("load pertama di afterPropertiesSet() - ELSE");
            this.usersList = new LazyDataModelJPA(userRepo) {
                @Override
                protected long getDataSize() {
                    if (userNameFilter != null && !userNameFilter.isEmpty()) {
                        System.out.println("ini load if pertama di ELSE");
                       // return userRepo.countByLoginAndCurrentTenant(userNameFilter);
                       return userRepo.count(whereQuery(userNameFilter));
                    } else {                        
                        System.out.println("ini load else pertama di ELSE");
                       // return userRepo.countByCurrentTenant();
                       return userRepo.count(whereQuery(userNameFilter));
                    }
                }

                @Override
                protected Page getDatas(PageRequest request) {
                    if (userNameFilter != null && !userNameFilter.isEmpty()) {
                        System.out.println("ini load if kedua di ELSE");
                        //return userRepo.findAllByLoginAndCurrentTenant(userNameFilter, request);
                        return userRepo.findAll(whereQuery(userNameFilter),request);
                    } else {
                        System.out.println("ini load else kedua di ELSE");
                        //return userRepo.findAllByCurrentTenant(request);
                        return userRepo.findAll(whereQuery(userNameFilter),request);
                    }
                }

            };

            // initiate available_roles
            this.availableRoles = roleRepo.findAllByEnabledAndCurrentTenant(true);
        }

        // initiate multi_roles from settings
        try {
            this.isMultipleRoles = Boolean.valueOf(settingRepo.findOne(Setting.X_MULTI_ROLES).getValue());
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_MULTI_ROLES);
            this.isMultipleRoles = false;
        }

        if (!this.isMultipleRoles) {
            this.rolesList = new ArrayList<>();
            for (Role role : this.availableRoles) {
                this.rolesList.add(new SelectItem(role, role.getRoleName()));
            }
        }
        
        this.isShowDetail = false;
    }

    /**
     * Update status of the selected users.
     * @param isUnlock
     */
    public void lockOrUnlockSelectedUsers(boolean isUnlock) {
        if (this.selectedUsers == null || this.selectedUsers.isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                    getMessageLocale("error_user_no_user_selected"));

        } else {
            userService.updateUsersStatus(this.selectedUsers, isUnlock);
            addPopUpMessage(FacesMessage.SEVERITY_INFO, getMessageLocale("header_success"),
                    getMessageLocale("data_success_update"));
        }
    }

    /**
     * CLose pop-up message that triggered from method saveDetail, and change
     * the view back to user list.
     */
    public void closeSaveInformationDialog() {
        RequestContext.getCurrentInstance().execute("PF('saveNotification').hide()");
        hideDetail();
    }

    /**
     * Save user's detail or create a new user. This method will called the
     * pop-up message to informs the result.
     */
    public void saveDetail() {
        if (this.roles != null && this.roles.getTarget().isEmpty()) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header_wrong_input"),
                    getMessageLocale("error_user_empty_role_selecting"));
            return;
        }

        try {
            // new user
            if (this.userDetail.getUserId() == null) {
                this.userDetail.setEnabled(true);
                this.userDetail.setActivated(true);

                this.userDetail.setDefaultTenant(this.isMultipleRoles
                        ? this.roles.getTarget().get(0).getTenant()
                        : this.selectedRole.getTenant());
            }

            // update user password
            if (this.passwordInput != null) {
                this.userDetail.setHashedPassword(this.passwordEncoder.encode(this.passwordInput));
            }

            // update user_roles
            if (this.isMultipleRoles) {
                userService.saveUser(this.userDetail, this.fullNameInput,
                        this.isGeneratedPassword, this.roles.getTarget());
            } else {
                List<Role> newRoles = new ArrayList<>();
                newRoles.add(this.selectedRole);

                userService.saveUser(this.userDetail, this.fullNameInput,
                        this.isGeneratedPassword, newRoles);
            }

            RequestContext.getCurrentInstance().execute("PF('saveNotification').show()");

        } catch (DuplicateKeyException dx) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header"),
                    getMessageLocale("error_user_duplicate_username"));
            log.error("Failed save user's detail : ", dx);

        } catch (Exception ex) {
            addPopUpMessage(FacesMessage.SEVERITY_WARN, getMessageLocale("error_header"),
                    getMessageLocale("error_user_save"));
            log.error("Failed save user's detail : ", ex);
        }
    }

    /**
     * Load and show user's detail. If the user parameter is null, this method
     * will show empty details and insert a new user data when submitted.
     *
     * @param user
     */
    public void loadDetail(User user) {
        this.isShowDetail = true;
        
        // reset password for view
        this.passwordInput = null;

        if (user != null) {
            this.userDetail = user;
            this.isGeneratedPassword = false;
            this.fullNameInput = this.userDetail.getParty().getFullName();
            headerDetail = "Ubah Data Pengguna";
            isDisabled = false;

            // roles
            if (this.isMultipleRoles) {
                List<Role> sourceRoles = new ArrayList<>(this.availableRoles);
                sourceRoles.removeAll(user.getRoles());
                this.roles = new DualListModel<>(sourceRoles, user.getRoles());
                this.selectedRole = null;

            } else {
                this.roles = null;
                this.selectedRole = !user.getRoles().isEmpty() ? user.getRoles().get(0) : null;
            }

        } else {
            // create new user
            this.userDetail = new User();
            this.isGeneratedPassword = true;
            this.fullNameInput = null;
            isDisabled = false;
            headerDetail = "Tambah Data Pengguna";

            // roles
            if (this.isMultipleRoles) {
                this.roles = new DualListModel<>(this.availableRoles, new ArrayList<>());
                this.selectedRole = null;

            } else {
                this.roles = null;
                this.selectedRole = null;
            }
        }

        // disabled auto_generate_password feature
        this.isGeneratedPassword = false;
        RequestContext.getCurrentInstance().reset("user-content:user-detail");
        RequestContext.getCurrentInstance().update("user-content:user-detail");
    }

    /**
     * Change view back to user list.
     */
    public void hideDetail() {
        this.isShowDetail = false;
    }

    public void hapus(User user) {
//        user.setActivated(false);
//        user.setEnabled(false);
//        userRepo.save(user);
        userRepo.delete(user);
        partyRepo.delete(user.getParty());
        addPopUpMessage(FacesMessage.SEVERITY_INFO, "Informasi", "Data user berhasil dihapus!");
    }

    public void showUser(User user) {
        isShowDetail = true;
        userDetail = user;
        isDisabled = true;
        fullNameInput = user.getParty().getFullName();
        headerDetail = "Lihat Data Pengguna";
        selectedRole = user.getRoles().get(0);
    }

    private Specification<User> whereQuery(
            final String value) {
        List<Predicate> predicates = new ArrayList<>();

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(value)) {
                    predicates.add(cb.like(cb.lower(root.<String>get("login")), getLikePattern(value.trim())));
                }
                predicates.add(cb.notEqual(root.<Boolean>get("activated"), false));
                predicates.add(cb.notEqual(root.<Boolean>get("enabled"), false));
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
