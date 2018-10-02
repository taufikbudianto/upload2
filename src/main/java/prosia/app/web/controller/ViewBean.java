/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import javax.faces.context.FacesContext;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.model.Setting;
import prosia.app.repo.SettingRepo;

/**
 *
 * @author Randy
 */
@Controller
@Scope("session")
public class ViewBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SettingRepo settingRepo;

    @Getter
    private String dateFormatPattern;
    @Getter
    private String dateTimeFormatPattern;
    @Getter
    private String decimalFormatPattern;
    @Getter
    private String integerFormatPattern;
    @Getter
    private String rowsPageDefault;
    @Getter
    private String rowsPerPageTemplate;

    @Getter
    private final String emailPattern
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    protected int first;

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void onPageChange(PageEvent event) {
        this.setFirst(((DataTable) event.getSource()).getFirst());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // load default values from settings
        try {
            dateFormatPattern = settingRepo.findOne(Setting.X_DATE_FORMAT).getValue();
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_DATE_FORMAT);
            dateFormatPattern = "dd-MM-yyyy";
        }

        try {
            dateTimeFormatPattern = settingRepo.findOne(Setting.X_DATE_TIME_FORMAT).getValue();
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_DATE_TIME_FORMAT);
            dateTimeFormatPattern = "dd-MM-yyyy HH:mm:ss";
        }

        try {
            decimalFormatPattern = settingRepo.findOne(Setting.X_DECIMAL_FORMAT).getValue();
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_DECIMAL_FORMAT);
            decimalFormatPattern = "###.##";
        }

        try {
            integerFormatPattern = settingRepo.findOne(Setting.X_INTEGER_FORMAT).getValue();
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_INTEGER_FORMAT);
            integerFormatPattern = "###";
        }

        try {
            rowsPageDefault = settingRepo.findOne(Setting.X_ROWS_PER_PAGE_DEFAULT).getValue();
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_ROWS_PER_PAGE_DEFAULT);
            rowsPageDefault = "10";
        }

        try {
            rowsPerPageTemplate = settingRepo.findOne(Setting.X_ROWS_PER_PAGE_TEMPLATE).getValue();
        } catch (Exception e) {
            log.warn("Can't find setting with prefix_name : {}" + Setting.X_ROWS_PER_PAGE_TEMPLATE);
            rowsPerPageTemplate = "5,10,15";
        }
    }

    /**
     * Convert current string to first uppercase and replace '_' with space
     * character. For example, to display java enum values.
     *
     * @param string
     * @return
     */
    public String toFirstUppercase(String string) {
        return string != null ? WordUtils.capitalize(string.replace("_", " ").toLowerCase()) : null;
    }

    /**
     * Add space between letters.
     *
     * @param string
     * @return
     */
    public String addWhiteSpaceBetweenCapitalLetters(String string) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                string.replaceAll("\\s\\d+", "\\s \\d")), " ");
    }

}
