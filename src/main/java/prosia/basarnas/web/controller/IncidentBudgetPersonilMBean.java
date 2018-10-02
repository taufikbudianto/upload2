/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentBudgetPersonil;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.repo.IncidentBudgetPersonnelRepo;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.MstBiayaPersonelRepo;

/**
 *
 * @author Taufik AB
 */
@Controller
@Scope("view")
@Data
public class IncidentBudgetPersonilMBean extends AbstractManagedBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private IncidentPersonnelRepo incPersonelRepo;
    @Autowired
    private IncidentBudgetPersonnelRepo incBudgetPersonelRepo;
    @Autowired
    private MstBiayaPersonelRepo mstBiayaPersonelRepo;

    @Autowired
    private IncidentMBean incidentMBean;
    private Incident currIncident;

    private List<IncidentBudgetPersonil> listBudgetPersonil;
    private List<IncidentPersonnel> listIncidentPersonil;
    private List<IncidentBudgetPersonil> listBiayaPersonilDelete;

    private Double totalAll;
    private Boolean disabled;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    public IncidentBudgetPersonilMBean() {
        listBudgetPersonil = new ArrayList<>();
        listIncidentPersonil = new ArrayList<>();
        listBiayaPersonilDelete = new ArrayList<>();
        totalAll = 0.00;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        currIncident = incidentMBean.getIncident();
        if (!currIncident.getStatus().equals("Open")) {
            disabled = true;
        } else {
            disabled = false;
        }
        checkDataIntable(currIncident);
        setData(null, false);

    }

    public void changeDate(IncidentBudgetPersonil budgetPersonil) {
        setData(budgetPersonil, true);
    }

    public void hapusBudget(IncidentBudgetPersonil budgetPersonil) {
        totalAll = 0.00;
        listBiayaPersonilDelete.add(budgetPersonil);
        listBudgetPersonil.removeIf((t) -> {
            if (budgetPersonil == t) {
                return true;
            }
            return false;
        });
        System.out.println("list size : " + listBudgetPersonil.size());
        for (IncidentBudgetPersonil budget : listBudgetPersonil) {
            totalAll += budget.getTotal();
        }
    }

    private void setData(IncidentBudgetPersonil budgetPers, Boolean change) {
        totalAll = 0.00;
        if (change) {
            long betweenDays = daysBetween(budgetPers.getTglMulai(), budgetPers.getTglAkhir());
            Double total = (budgetPers.getMstBiayaPersonnel().getBiayaMakan() + budgetPers.getMstBiayaPersonnel().getBiayaSuplemen()) * betweenDays;
            budgetPers.setTotal(total);
            listBudgetPersonil.replaceAll((IncidentBudgetPersonil biaya)
                    -> biaya.equals(budgetPers) ? budgetPers : biaya);
        } else {
            for (IncidentBudgetPersonil budgetPersonel : incBudgetPersonelRepo.findAllByNotDeleted(currIncident.getIncidentid())) {
                long betweenDays = daysBetween(budgetPersonel.getTglMulai(), budgetPersonel.getTglAkhir());
                Double total = (budgetPersonel.getMstBiayaPersonnel().getBiayaMakan() + budgetPersonel.getMstBiayaPersonnel().getBiayaSuplemen()) * betweenDays;
                budgetPersonel.setTotal(total);
                listBudgetPersonil.add(budgetPersonel);
            }
        }
        for (IncidentBudgetPersonil budget : listBudgetPersonil) {
            totalAll += budget.getTotal();
        }
    }

    public void save() {
        for (IncidentBudgetPersonil budget : listBiayaPersonilDelete) {
            budget.setIsDeleted(true);
            incBudgetPersonelRepo.save(budget);
        }
        for (IncidentBudgetPersonil budget : listBudgetPersonil) {
            incBudgetPersonelRepo.save(budget);
        }
        addPopUpMessage("Sukses",
                "Biaya Lain berhasil disimpan");
    }

    public void tambah() {

    }

    public void checkDataIntable(Incident inc) {
        log.info("Cek Table Incident Budget Personel ......"+inc.getIncidentid());
        listIncidentPersonil = incPersonelRepo.findByIncidentAndDeleted(inc, false);
        for (IncidentPersonnel pers : listIncidentPersonil) {
            if (incBudgetPersonelRepo.findByIncPersonelId(pers) == null) {
                IncidentBudgetPersonil budgetPersonel = new IncidentBudgetPersonil();
                budgetPersonel.setBudgetPersonalId(formatIncidentBudgetId(0));
                budgetPersonel.setDeskripsi(pers.getPersonnelName());
                budgetPersonel.setIncPersonelId(pers);
                budgetPersonel.setIncidentId(inc.getIncidentid());
                budgetPersonel.setIsDeleted(null);
                budgetPersonel.setKansar(pers.getPersonnel() != null ? (pers.getPersonnel().getOfficeSar() != null
                        ? pers.getPersonnel().getOfficeSar().getKantorsarname() : null) : null);
                budgetPersonel.setMstBiayaPersonnel(mstBiayaPersonelRepo.findByBiayaPersonnelId(1));
                budgetPersonel.setTglAkhir(new Date());
                budgetPersonel.setTglMulai(new Date());
                budgetPersonel.setTotal(0.00);
                incBudgetPersonelRepo.save(budgetPersonel);
            } else {
                IncidentBudgetPersonil budgetPersonel = incBudgetPersonelRepo.findByIncPersonelId(pers);
                budgetPersonel.setIsDeleted(false);
                incBudgetPersonelRepo.save(budgetPersonel);
            }
        }
        listIncidentPersonil = incPersonelRepo.findByIncidentAndDeleted(inc, true);
        for (IncidentPersonnel pers : listIncidentPersonil) {
            if (incBudgetPersonelRepo.findByIncPersonelId(pers) != null) {
                IncidentBudgetPersonil budgetPersonel = incBudgetPersonelRepo.findByIncPersonelId(pers);
                budgetPersonel.setIsDeleted(true);
                incBudgetPersonelRepo.save(budgetPersonel);
            }
        }
    }

    public Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date diffDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    private static long daysBetween(Date mulai, Date akhir) {
        if (mulai.compareTo(akhir) > 0 || mulai.compareTo(akhir) == 0) {
            return 0;
        }
        long difference = (mulai.getTime() - akhir.getTime()) / 86400000;
        return Math.abs(difference);
    }

    public Date getMinDate() {
        return Calendar.getInstance().getTime();
    }

    public String formatIncidentBudgetId(int i) {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        // System.out.println("FROMDATEDETAIL : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";

        List<IncidentBudgetPersonil> ias = incBudgetPersonelRepo.findTopOneByBudgetPersonalIdContaining("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = incBudgetPersonelRepo.findBudgetPersonelByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1 + i;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        break;
                    case 2:
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        nextval = "" + next;
                        break;
                    default:
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;

    }
}
