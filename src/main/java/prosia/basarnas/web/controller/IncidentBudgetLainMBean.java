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
import java.util.stream.Collectors;
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
import prosia.basarnas.model.IncidentBiayaLain;
import prosia.basarnas.model.MstJenisBiaya;
import prosia.basarnas.repo.IncidentBudgetLainRepo;
import prosia.basarnas.repo.MstJenisBiayaRepo;

/**
 *
 * @author Taufik AB
 */
@Controller
@Scope("view")
@Data
public class IncidentBudgetLainMBean extends AbstractManagedBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private IncidentBudgetLainRepo budgetLainRepo;
    @Autowired
    private MstJenisBiayaRepo jenisBiayaRepo;
    @Autowired
    private IncidentMBean incidentMBean;
    private Incident currIncident;

    private List<SelectItem> listJenis;
    private List<IncidentBiayaLain> listBiayaLain;
    private List<IncidentBiayaLain> listBiayaLainDelete;
    private List<IncidentBiayaLain> tambahlistbiaya;

    private Double totalAll;
    private Boolean disabled;

    public IncidentBudgetLainMBean() {
        listBiayaLain = new ArrayList<>();
        listBiayaLainDelete = new ArrayList<>();
        tambahlistbiaya = new ArrayList<>();
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

        listJenis = new ArrayList<>();
        jenisBiayaRepo.findAll().stream().forEach((biayalain) -> {
            listJenis.add(new SelectItem(biayalain.getNama(), biayalain.getNama()));
        });
        getAllData(null, currIncident, false);
    }

    //change biaya
    public void changeBiaya(IncidentBiayaLain biayaLain) {
        
        MstJenisBiaya jenis = jenisBiayaRepo.findByNama(biayaLain.getMstJenisBiaya().getNama());
        biayaLain.setMstJenisBiaya(jenis);
        getAllData(biayaLain, currIncident, true);
    }

    public void getAllData(IncidentBiayaLain biayaLain, Incident inc, Boolean change) {
        if (budgetLainRepo.findByIncident(inc).isEmpty()) {
            if (change) {
                totalAll = 0.00;
                MstJenisBiaya jenis = jenisBiayaRepo.findByNama(biayaLain.getMstJenisBiaya().getNama());
                biayaLain.setTotal(Double.valueOf(jenis.getNilai()));
                biayaLain.setBiayaLainId(null);
                listBiayaLain.replaceAll((IncidentBiayaLain biaya)
                        -> biaya.equals(biayaLain) ? biayaLain : biaya);

            } else {
                totalAll = 0.00;
                List<MstJenisBiaya> listBiaya = jenisBiayaRepo.findAll();
                int i = 0;
                for (MstJenisBiaya jenisBiaya : listBiaya) {
                    IncidentBiayaLain biayaLainInc = new IncidentBiayaLain();
                    biayaLainInc.setIsdeleted(false);
                    biayaLainInc.setIncident(inc);
                    biayaLainInc.setDeskripsi(jenisBiaya.getNama());
                    MstJenisBiaya jenis = jenisBiayaRepo.findByNama("Transportasi");
                    biayaLainInc.setMstJenisBiaya(jenis);
                    biayaLainInc.setTotal(Double.valueOf(jenis.getNilai()));

                    listBiayaLain.add(biayaLainInc);
                    i++;
                }
            }
        } else {
            if (change) {
                totalAll = 0.00;
                MstJenisBiaya jenis = jenisBiayaRepo.findByNama(biayaLain.getMstJenisBiaya().getNama());
                biayaLain.setMstJenisBiaya(jenis);
                biayaLain.setTotal(Double.valueOf(jenis.getNilai()));
                listBiayaLain.replaceAll((IncidentBiayaLain biaya)
                        -> biaya.equals(biayaLain) ? biayaLain : biaya);
            } else {
                totalAll = 0.00;
                for (IncidentBiayaLain budgetLain : budgetLainRepo.findByIncidentAndIsdeleted(inc, false)) {
                    budgetLain.setTotal(Double.valueOf(budgetLain.getMstJenisBiaya().getNilai()));
                    listBiayaLain.add(budgetLain);

                }

            }
        }
        for (IncidentBiayaLain budget : listBiayaLain) {
            totalAll += budget.getTotal();
        }

    }

    //save
    public void save() {
        if (!listBiayaLainDelete.isEmpty()) {
            for (IncidentBiayaLain budget : listBiayaLainDelete) {
                budget.setIsdeleted(true);
                budgetLainRepo.save(budget);
            }
        }
        int i = 0;
        for (IncidentBiayaLain budget : listBiayaLain) {
            if (budget.getBiayaLainId() == null) {
                budget.setBiayaLainId(formatIncidentAssetId(i));
                i++;
            }
            budgetLainRepo.save(budget);

        }

        addPopUpMessage("Sukses",
                "Biaya Lain berhasil disimpan");
    }

    //delete budget
    public void hapusBudget(IncidentBiayaLain biayaLain) {
        totalAll = 0.00;
        listBiayaLainDelete.add(biayaLain);
        listBiayaLain.removeIf((t) -> {
            if (biayaLain == t) {
                return true;
            }
            return false;
        });
        for (IncidentBiayaLain budget : listBiayaLain) {
            totalAll += budget.getTotal();
        }

    }

    //tambah delete budget
    public void tambah() {
        totalAll = 0.00;
        tambahlistbiaya = new ArrayList<>();
        currIncident = incidentMBean.getIncident();
        // RequestContext.getCurrentInstance().execute("PF('budgetlain-dlg').show()");
        IncidentBiayaLain biayaLainIncident = new IncidentBiayaLain();
        biayaLainIncident.setIsdeleted(false);
        biayaLainIncident.setIncident(currIncident);
        biayaLainIncident.setDeskripsi("Transportasi");
        MstJenisBiaya jenisLain = jenisBiayaRepo.findByNama("Transportasi");
        biayaLainIncident.setMstJenisBiaya(jenisLain);
        biayaLainIncident.setTotal(Double.valueOf(jenisLain.getNilai()));
        listBiayaLain.add(biayaLainIncident);;
        for (IncidentBiayaLain budget : listBiayaLain) {
            totalAll += budget.getTotal();
        }
        if (budgetLainRepo.findByIncident(currIncident).isEmpty()) {
            for (IncidentBiayaLain biayaLainInc : listBiayaLainDelete) {
                biayaLainInc.setIsdeleted(false);
                biayaLainInc.setIncident(currIncident);
                MstJenisBiaya jenis = jenisBiayaRepo.findByNama(biayaLainInc.getMstJenisBiaya().getNama());
                biayaLainInc.setMstJenisBiaya(jenis);
                biayaLainInc.setTotal(Double.valueOf(jenis.getNilai()));
                tambahlistbiaya.add(biayaLainInc);

            }
        } else {
            for (IncidentBiayaLain biayaLainInc : budgetLainRepo.findByIncidentAndIsdeleted(currIncident, false)) {
                biayaLainInc.setIsdeleted(false);
                biayaLainInc.setIncident(currIncident);
                MstJenisBiaya jenis = jenisBiayaRepo.findByNama(biayaLainInc.getMstJenisBiaya().getNama());
                biayaLainInc.setMstJenisBiaya(jenis);
                biayaLainInc.setTotal(Double.valueOf(jenis.getNilai()));
                tambahlistbiaya.add(biayaLainInc);

            }
        }

    }

    public void updatepilih(IncidentBiayaLain biayaLain) {
        listBiayaLain.add(biayaLain);

        listBiayaLainDelete.removeIf((t) -> {
            if (biayaLain == t) {
                return true;
            }
            return false;
        });
        totalAll = 0.0;
        for (IncidentBiayaLain budget : listBiayaLain) {
            totalAll += budget.getTotal();
        }
    }

    public String formatIncidentAssetId(int i) {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        // System.out.println("FROMDATEDETAIL : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";

        List<IncidentBiayaLain> ias = budgetLainRepo.findTopOneBybiayaLainIdContaining("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = budgetLainRepo.findBiayaLainByMaxId("CGK");
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
