/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import lombok.Data;
import org.primefaces.context.RequestContext;
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
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentBudgetAsset;
import prosia.basarnas.model.MstBbm;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentBudgetAssetRepo;
import prosia.basarnas.repo.MstBbmRepo;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
@Data
public class IncidentBudgetAssetMBean extends AbstractManagedBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IncidentBudgetAssetRepo incBudgetAssetRepo;
    @Autowired
    private MstBbmRepo bbmRepo;
    @Autowired
    private IncidentAssetRepo incAssetRepo;
    @Autowired
    private IncidentMBean incidentMBean;

    private List<SelectItem> listBbm;
    private List<IncidentBudgetAsset> listAssetBudget;
    private List<IncidentBudgetAsset> listAssetBudgetDelete;
    private List<IncidentAsset> listIncidentAsset;

    private Incident currIncident;
    private Double totalAll;
    private Boolean disabled;

    public IncidentBudgetAssetMBean() {
        listAssetBudget = new ArrayList<>();
        listIncidentAsset = new ArrayList<>();
        listAssetBudgetDelete = new ArrayList<>();
        totalAll = 0.00;

    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        currIncident = incidentMBean.getIncident();
        if (!currIncident.getStatus().equals("Open")) {
            disabled = true;
        } else {
            disabled = false;
        }
        listBbm = new ArrayList<>();
        bbmRepo.findAll().stream().forEach((bbm) -> {
            listBbm.add(new SelectItem(bbm.getJenis(), bbm.getJenis()));
        });
        checkDataIntable(currIncident);
        setData(null, false);

    }

    public void changeBBM(IncidentBudgetAsset assetBudget) {
        setData(assetBudget, true);
    }

    private void setData(IncidentBudgetAsset budgetAss, Boolean change) {
        totalAll = 0.00;
        if (change) {
            MstBbm bbm = bbmRepo.findByJenis(budgetAss.getBbm().getJenis());
            budgetAss.setBbm(bbm);
            budgetAss.setTotal(bbm.getNilai() * budgetAss.getBbbAssetPemakaian() * budgetAss.getBudgetAssetDur());
            listAssetBudget.replaceAll((IncidentBudgetAsset budget)
                    -> budget.getIncidentAsset().equals(budgetAss) ? budgetAss : budget);
        } else {
            for (IncidentBudgetAsset budget : incBudgetAssetRepo.findAllByNotDeleted(currIncident.getIncidentid())) {
                budget.setTotal(budget.getBbm().getNilai() * budget.getBbbAssetPemakaian() * budget.getBudgetAssetDur());
                listAssetBudget.add(budget);
            }
        }
        for (IncidentBudgetAsset budget : listAssetBudget) {
            totalAll += budget.getTotal();
        }
    }

    public void hapusBudget(IncidentBudgetAsset budgetAsset) {
        totalAll = 0.00;
        listAssetBudgetDelete.add(budgetAsset);
        listAssetBudget.removeIf((t) -> {
            if (budgetAsset.getIncidentAsset() == t.getIncidentAsset()) {
                return true;
            }
            return false;
        });
        for (IncidentBudgetAsset budget : listAssetBudget) {
            totalAll += budget.getTotal();
        }
    }

    public void tambah() {
        RequestContext.getCurrentInstance().execute("PF('budgetlain-dlg').show()");
    }

    public void save() {
        for (IncidentBudgetAsset budgetAsset : listAssetBudgetDelete) {
            budgetAsset.setIsDeleted(true);
            incBudgetAssetRepo.save(budgetAsset);
        }
        for (IncidentBudgetAsset budgetAsset : listAssetBudget) {
            incBudgetAssetRepo.save(budgetAsset);
        }
        addPopUpMessage("Sukses",
                "Biaya BBM berhasil disimpan");
    }

    public void checkDataIntable(Incident inc) {
        log.info("Cek Table Incident Budget Incident Asset ......");
        listIncidentAsset = incAssetRepo.findByIncidentAndAssetTypeSru(inc, false);
        for (IncidentAsset incAsset : listIncidentAsset) {
            if (incBudgetAssetRepo.findByIncidentAsset(incAsset) == null) {
                IncidentBudgetAsset budgetAsset = new IncidentBudgetAsset();
                budgetAsset.setBbbAssetPemakaian(0.00);
                budgetAsset.setBbm(bbmRepo.findByJenis("Premium"));
                budgetAsset.setBudgetAssetDescr(incAsset.getName());
                budgetAsset.setBudgetAssetDur(0.00);
                budgetAsset.setIncBudgetAssetId(formatIncidentAssetId());
                budgetAsset.setIncidentAsset(incAsset);
                budgetAsset.setIncidentId(inc.getIncidentid());
                budgetAsset.setTotal(0.00);
                budgetAsset.setIsDeleted(null);
                incBudgetAssetRepo.save(budgetAsset);
            } else {
                IncidentBudgetAsset budgetAsset = incBudgetAssetRepo.findByIncidentAsset(incAsset);
                budgetAsset.setIsDeleted(false);
                incBudgetAssetRepo.save(budgetAsset);
            }
        }
        listIncidentAsset = incAssetRepo.findByIncidentAndAssetTypeSru(inc, true);
        for (IncidentAsset incAsset : listIncidentAsset) {
            if (incBudgetAssetRepo.findByIncidentAsset(incAsset) != null) {
                IncidentBudgetAsset budgetAsset = incBudgetAssetRepo.findByIncidentAsset(incAsset);
                budgetAsset.setIsDeleted(true);
                incBudgetAssetRepo.save(budgetAsset);
            }
        }
    }

    public String formatIncidentAssetId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        // System.out.println("FROMDATEDETAIL : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";

        List<IncidentBudgetAsset> ias = incBudgetAssetRepo.findTopOneByIncBudgetAssetIdContaining("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = incBudgetAssetRepo.findAssetByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
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
