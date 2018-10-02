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
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import prosia.app.web.controller.MenuNavMBean;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.repo.IncidentAssetRepo;

/**
 *
 * @author PROSIA
 */
@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Component
@Scope("view")

public @Data
class IncidentSRUAllMBean implements InitializingBean {

    private IncidentAsset incidentAsset;

    @Autowired
    private IncidentAssetRepo incidentAssetRepo;

    @Autowired
    private MenuNavMBean menuNavMBean;

    @Autowired
    private IncidentSRULautMBean incSRULautMBean;

    private List<TempCheck> checkLaut;

    public String formatIncidentAssetId() {
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        System.out.println("FROMDATEDETAIL : " + fromDate);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";

        List<IncidentAsset> ias = incidentAssetRepo.findTopOneByIncidentAssetIDContaining("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = incidentAssetRepo.findAssetByMaxId("CGK");
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

    public void simpan() {
//        checkLaut = new ArrayList<TempCheck>();
//        for (incSRULautMBean.getListTempCheck().size()) {
//            if (incidentAsset.getIncidentAssetID() == null) {
//                incidentAsset.setIncidentAssetID(formatIncidentAssetId());
//            }
//
//            incidentAsset.setAssetType(assetType);
//        }
        formatIncidentAssetId();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("SRU LAUT : " + incSRULautMBean.getListTempCheck().size());

//        ResAsset.setCreatedby(menuNavMBean.getUserSession().getUsername());
//        ResAsset.setUsersiteid(ProsiaConstant.SITES);
//        ResAsset.setDatecreated(new Date());
//        ResAsset.setIsdeleted(BigInteger.ZERO);
//        MstAssetType assettypename = assetTypeRepo.findByAssetname(assetname);
//        System.out.println("id :" + assettypename);
//        if (ResAsset.getAssettypeid() == null) {
//            ResAsset.setAssettypeid(assettypename);
//        }
//        MstKantorSar sar = kantorSarRepo.findByKantorsarname(kantorsarname);
//        if (ResAsset.getKantorsarid() == null) {
//            ResAsset.setKantorsarid(sar);
//        };
//        //System.out.println("respotensi =" +resPotency.getPotencyid());
//        if(resPotency.getPotencyid()!=null){
//            ResAsset.setPotencyid(resPotency);
//        }
//        ResAsset.setOp_type(operationaltype);
//        saranaRepo.save(ResAsset);
//
//        //addMessage("Sukses", negara.getCountryID() != null ? "Negara berhasil disimpan" : "Negara berhasil diubah");
//        addMessage("Sukses", "Data Checklist berhasil disimpan");
//        refresh();
//        RequestContext.getCurrentInstance().execute("PF('widgetResSarana').hide()");
//        RequestContext.getCurrentInstance().execute("PF('widgetResSaranaInput').hide()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println("SRU LAUT : " + incSRULautMBean.getListTempCheck().size());
    }

    @Data
    public class TempCheck {

        private Boolean kirim;
        private Boolean pilih;
        private ResAsset resAsset;
    }
}
