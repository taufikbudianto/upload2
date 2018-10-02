/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.ResPersonnelTraining;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.ResPotencyContact;
import prosia.basarnas.repo.PersonelHistoryRepo;
import prosia.basarnas.repo.PersonelTrainingRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.ResPersonnelRepo;
import prosia.basarnas.repo.ResPotencyContactRepo;
import prosia.basarnas.repo.ResPotencyRepo;

/**
 *
 * @author Owner
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class PotensiService {

    @Autowired
    private ResAssetRepo assetRepo;

    @Autowired
    private ResPotencyContactRepo resPotencyContactRepo;

    @Autowired
    private ResPersonnelRepo personnelRepo;

    @Autowired
    private ResPotencyRepo potensyRepo;

    @Autowired
    private PersonelHistoryRepo personelHistoryRepo;

    @Autowired
    private PersonelTrainingRepo personelTrainingRepo;

    public void savePotency(ResPotency potency, List<ResAsset> listAset, List<ResPersonnel> listpersonnel,
            List<ResPersonnelHistory> listPersonelHistory, List<ResPersonnelTraining> listPersonelTraining,
            List<ResPotencyContact> listkontak) {

        System.out.println("Save Potency And ResPotency Contacs");
        if (potency.getPotencyid() == null) {
            potency.setPotencyid(createidPotency());
        }
        potency.setIsdeleted(BigInteger.ZERO);
        potency.setDatecreated(new Date());
        potensyRepo.save(potency);

        for (int i = 0; i < listAset.size(); i++) {
            ResAsset asset = assetRepo.findOne(listAset.get(i).getAssetid());
            if (asset == null) {
                listAset.get(i).setAssetid(createidtemp(i));
            }
            listAset.get(i).setPotencyid(potency);
            listAset.get(i).setDatecreated(new Date());
            assetRepo.save(listAset.get(i));

        }
        //Delete Aset
        List<ResAsset> lsAset = assetRepo.findBypotencyidAndisdeleted(potency);
        if (!lsAset.isEmpty()) {
            for (ResAsset asset : listAset) {
                lsAset.removeIf(x -> x.getAssetid().equals(asset.getAssetid()));
            }
            assetRepo.delete(lsAset);
        }

        int personelId = 0;
        for (ResPersonnel personnel : listpersonnel) {
            List<ResPersonnelHistory> listFilterPersonelHistory
                    = listPersonelHistory.stream().filter(x
                            -> x.getPersonnel().getPersonnelID().equals(personnel.getPersonnelID()))
                            .collect(Collectors.toList());
            List<ResPersonnelTraining> listFilterPersonelTraining
                    = listPersonelTraining.stream().filter(x
                            -> x.getPersonnel().getPersonnelID().equals(personnel.getPersonnelID()))
                            .collect(Collectors.toList());
            
             //DELETE  
//            List<ResPersonnel> lsPersonel = personnelRepo.findAllBypotencyAndIsdeleted(potency, BigInteger.ZERO);
//            if (!lsPersonel.isEmpty()) {
//                for (ResPersonnel resPersonnel : listpersonnel) {
//                    resPersonnel.setIsdeleted(BigInteger.ONE);
//                    personnelRepo.save(resPersonnel);
//                }
//            }
            
            ResPersonnel personels = personnelRepo.findOne(personnel.getPersonnelID());
            if (personels == null) {
                personnel.setPersonnelID(createidtempPersonel(personelId));
            }
            personnel.setPotency(potency);
            personnelRepo.save(personnel);
           

            for (int i = 0; i < listFilterPersonelHistory.size(); i++) {
                ResPersonnelHistory history
                        = personelHistoryRepo.findOne(listFilterPersonelHistory.get(i).getPersonnelHistoryID());
                if (history == null) {
                    listFilterPersonelHistory.get(i).setPersonnelHistoryID(createidtempPersonelHistory(i));
                }
                listFilterPersonelHistory.get(i).setPersonnel(personnel);
                personelHistoryRepo.save(listFilterPersonelHistory.get(i));

            }
            //DELETE
            List<ResPersonnelHistory> listHistory = personelHistoryRepo.findAllResPersonnelHistoryBypersonnel(personnel);
            if (!listHistory.isEmpty()) {
                for (ResPersonnelHistory filterHistory : listFilterPersonelHistory) {
                    listHistory.removeIf(x -> x.getPersonnelHistoryID().equals(filterHistory.getPersonnelHistoryID()));
                }
                personelHistoryRepo.delete(listHistory);
            }

            for (int i = 0; i < listFilterPersonelTraining.size(); i++) {
                ResPersonnelTraining training = personelTrainingRepo.findOne(listFilterPersonelTraining.get(i).getPersonnelTrainingID());
                if (training == null) {
                    listFilterPersonelTraining.get(i).setPersonnelTrainingID(createidtempPersonelTraining(i));
                }
                listFilterPersonelTraining.get(i).setPersonnel(personnel);
                personelTrainingRepo.save(listFilterPersonelTraining.get(i));
            }
            //DELETE
            List<ResPersonnelTraining> listTraining = personelTrainingRepo.findAllResPersonnelTrainingBypersonnel(personnel);
            if (!listTraining.isEmpty()) {
                for (ResPersonnelTraining filterTraining : listFilterPersonelTraining) {
                    listTraining.removeIf(x -> x.getPersonnelTrainingID().equals(filterTraining.getPersonnelTrainingID()));
                }
                personelTrainingRepo.delete(listTraining);
            }
            personelId += 1;
        }

        for (int i = 0; i < listkontak.size(); i++) {
            ResPotencyContact contact = resPotencyContactRepo.findOne(listkontak.get(i).getPotencycontactid());
            if (contact == null) {
                listkontak.get(i).setPotencycontactid(createidtempPersonelContact(i));
            }
            listkontak.get(i).setPotencyid(potency);
            resPotencyContactRepo.save(listkontak.get(i));
        }

        //Delete
        List<ResPotencyContact> lspotencyContacts = resPotencyContactRepo.findAllBypotencyid(potency);

        if (!lspotencyContacts.isEmpty()) {
            for (ResPotencyContact contacts : listkontak) {
                lspotencyContacts.removeIf(x -> x.getPotencycontactid().equals(contacts.getPotencycontactid()));
            }
            resPotencyContactRepo.delete(lspotencyContacts);
        }
    }

    public String createidPotency() {
        System.out.println("createidPotency");
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPotency> ias = potensyRepo.findAllpotencyid("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = potensyRepo.findPotencyByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }

    public String createidtemp(int i) {
        System.out.println("createidtemp");
        i += 1;
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResAsset> ias = assetRepo.findAllByassetid("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = assetRepo.findAssetByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + i;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }

    public String createidtempPersonel(int i) {
        System.out.println("createidtempPersonel");
        i += 1;
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPersonnel> ias = personnelRepo.findAllBypersonnelIDLike("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = personnelRepo.findPersonnelByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }

    private String createidtempPersonelTraining(int i) {
        System.out.println("createidtempPersonelTraining");
        i += 1;
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPersonnelTraining> ias = personelTrainingRepo.findAllBypersonnelTrainingID("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = personelTrainingRepo.findPersonnelTrainingByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + i;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }

    private String createidtempPersonelHistory(int i) {
        System.out.println("createidtempPersonelHistory");
        i += 1;
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPersonnelHistory> ias = personelHistoryRepo.findAllByPersonnelHistoryIDLike("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = personelHistoryRepo.findPersonnelByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + i;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incassetid;
    }

    private String createidtempPersonelContact(int i) {
        System.out.println("createidtempPersonelContact");
        i += 1;
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPotencyContact> ias = resPotencyContactRepo.findAllByPContactIdLike("CGK");
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = resPotencyContactRepo.findPersonnelByMaxId("CGK");
            String[] splitId = maxassetId.split("-");
            if (maxassetId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + i;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        System.out.println("CASE 1");
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        System.out.println("NEXTVAL : " + nextval);
                        break;
                    case 2:
                        System.out.println("CASE 2");
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        System.out.println("CASE 3");
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        System.out.println("CASE 4");
                        nextval = "" + next;
                        break;
                    default:
                        System.out.println("DEFAULT");
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
