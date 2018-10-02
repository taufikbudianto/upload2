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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.model.ResPersonnelHistory;
import prosia.basarnas.model.ResPersonnelTraining;
import prosia.basarnas.repo.PersonelHistoryRepo;
import prosia.basarnas.repo.PersonelTrainingRepo;
import prosia.basarnas.repo.ResPersonnelRepo;

/**
 *
 * @author Owner
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class PersonnelService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResPersonnelRepo personnelRepo;

    @Autowired
    private PersonelHistoryRepo personelHistoryRepo;

    @Autowired
    private PersonelTrainingRepo personelTrainingRepo;

    private String namePlusGelar;

    public void updatePersonnel(ResPersonnel personnel, List<ResPersonnelHistory> listPersonelHistory, List<ResPersonnelTraining> listPersonelTraining) {

        try {

            int personelId = 0;
            if (personnel != null) {
                
                personnel.setDatecreated(new Date());
                personnelRepo.save(personnel);
               
                int i = 0;
                for (ResPersonnelHistory history : listPersonelHistory) {
                    if (history.getPersonnelHistoryID() != null) {
                        System.out.println("Null" + null);
                    } else {
                        i += 1;
                        history.setPersonnelHistoryID(createidtempPersonelHistory(i));
                    }
                    history.setPersonnel(personnel);
                    personelHistoryRepo.save(history);
                }
                
                i = 0;
                for (ResPersonnelTraining training : listPersonelTraining) {
                    if (training.getPersonnelTrainingID() != null) {
                        System.out.println("Null" + null);
                    } else {
                        i += 1;
                        training.setPersonnelTrainingID(createidtempPersonelTraining(i));
                    }
                    training.setPersonnel(personnel);
                    personelTrainingRepo.save(training);
                }
            }
        } catch (Exception e) {
            logger.error("error : ", e);
        }
    }

    public void savePersonnel(ResPersonnel personnel, List<ResPersonnelHistory> listPersonelHistory, List<ResPersonnelTraining> listPersonelTraining) {

        if (personnel != null) {
            System.out.println("Masuk Ke Serivce");
            personnel.setPersonnelID(createidtempPersonel(0));
            personnel.setIsdeleted(BigInteger.ZERO);
            personnelRepo.save(personnel);
            System.out.println("Simpan Personel");
            int i = 0;
            for (ResPersonnelHistory history : listPersonelHistory) {
                i += 1;
                history.setPersonnelHistoryID(createidtempPersonelHistory(i));
                history.setPersonnel(personnel);
                personelHistoryRepo.save(history);
            }
            i = 0;
            for (ResPersonnelTraining training : listPersonelTraining) {
                i += 1;
                training.setPersonnelTrainingID(createidtempPersonelTraining(i));
                training.setPersonnel(personnel);
                personelTrainingRepo.save(training);
            }

        }

    }

    public String createidtempPersonel(int i) {
        System.out.println("createidtempPersonel");
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
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incassetid = "";
        List<ResPersonnelTraining> ias = personelTrainingRepo.findAllBypersonnelTrainingID("CGK");
        System.out.println("createidtempPersonelTraining " + ias.size());
        if (ias.isEmpty()) {
            incassetid = "CGK" + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxassetId = personelTrainingRepo.findPersonnelTrainingByMaxId("CGK");
            System.out.println(maxassetId);
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

}
