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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.enumeration.Timezone;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.model.IncidentChecklist;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentNational;
import prosia.basarnas.model.IncidentPOB;
import prosia.basarnas.model.IncidentPersonnel;
import prosia.basarnas.model.IncidentPotency;
import prosia.basarnas.model.IncidentWeather;
import prosia.basarnas.model.UtiBeaconComposite;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.BeaconCompositeRepo;
import prosia.basarnas.repo.IncidentAssetRepo;
import prosia.basarnas.repo.IncidentChecklistRepo;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentNationalRepo;
import prosia.basarnas.repo.IncidentPersonnelRepo;
import prosia.basarnas.repo.IncidentTabPOBRepo;
import prosia.basarnas.repo.IncidentPotencyRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IncidentWeatherRepo;
import prosia.basarnas.repo.SightingAndHearingRepo;

/**
 *
 * @author PROSIA
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class IncidentService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private SightingAndHearingRepo sightingAndHearingRepo;
    @Autowired
    private BeaconCompositeRepo beaconCompositeRepo;
    @Autowired
    private IncidentAssetRepo incidentAssetRepo;
    @Autowired
    private IncidentPersonnelRepo incidentPersonnelRepo;
    @Autowired
    private IncidentPotencyRepo incidentPotencyRepo;
    @Autowired
    private IncidentLogRepo incidentLogRepo;
    @Autowired
    private IncidentTabPOBRepo incidentPobRepo;
    @Autowired
    private IncidentWeatherRepo incidentWeatherRepo;
    @Autowired
    private IncidentChecklistRepo incidentChecklistRepo;
    @Autowired
    private IncidentNationalRepo incidentNationalRepo;

    public Incident saveInsiden(Incident obj, String[] selectedKansar) {
        try {
            if (org.apache.commons.lang3.StringUtils.isBlank(obj.getUsersiteid())) {
                obj.setUsersiteid("BSN");
            }
            if (org.apache.commons.lang3.StringUtils.isBlank(obj.getIncidentid())) {
                obj.setIncidentid(formatIncidentId(obj));
                obj.setIncidentnumber(formatIncidentNumber(obj));
                obj.setIsdeleted(BigInteger.ZERO);
                obj.setDatecreated(new Date());
            }
            logger.debug("INSIDEN ID : {}", obj.getIncidentid());
            logger.debug("INSIDEN NUMBER : {}", obj.getIncidentnumber());
            obj.setLastmodified(new Date());
            Incident inc = incidentRepo.save(obj);
            if (obj.getIncidentScala() == 1 && selectedKansar.length > 0) {
                incidentNationalRepo.deleteByIncident(inc);
                for (String str : selectedKansar) {
                    IncidentNational in = new IncidentNational();
                    in.setKantorSarId(str);
                    in.setIncident(inc);
                    incidentNationalRepo.save(in);
                }
            }
            return inc;
        } catch (Exception e) {
            logger.error("Terjadi Kesalahan : {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Incident reOpenIncident(Incident i) {
        Incident oldIncident = i;
        Incident newIncident;
        i.setIncidentid(formatIncidentId(i));
        if (i.getRemarks() != null && !i.getRemarks().contains("Ref:")) {
            i.setRemarks(i.getRemarks() + "\n Ref: " + i.getIncidentnumber() + " / " + i.getIncidentname());
        } else {
            i.setRemarks(i.getIncidentnumber() + " / " + i.getIncidentname());
        }
        i.setDatecreated(new Date());
        i.setLastmodified(new Date());
        i.setClosedate(null);
        i.setClosedatetimezone(null);
        i.setStartopsdate(null);
        i.setStartopsdatetimezone(null);
        i.setAlertedby(null);
        i.setStatus(ProsiaConstant.OPEN);
        newIncident = incidentRepo.save(i);
        logger.debug("new incident has saved...");

        //UPDATE SIGHTING
        List<UtiSighting> sightingList = sightingAndHearingRepo.findByIncident(oldIncident);
        for (UtiSighting sighting : sightingList) {
            sighting.setIncident(newIncident);
            sightingAndHearingRepo.save(sighting);
        }
        logger.debug("incident sighting has updated...");

        //UPDATE BEACON COMPOSITE
        List<UtiBeaconComposite> compositeList = beaconCompositeRepo.findAllByIncidentid(oldIncident);
        for (UtiBeaconComposite composite : compositeList) {
            composite.setIncidentid(newIncident);
            beaconCompositeRepo.save(composite);
        }
        logger.debug("incident beacon composite has updated...");

        //UPDATE ASSET
        List<IncidentAsset> assetList = incidentAssetRepo.findAllByIncident(oldIncident);
        for (IncidentAsset asset : assetList) {
            asset.setIncident(newIncident);
            incidentAssetRepo.save(asset);
        }
        logger.debug("incident asset has updated...");

        //UPDATE PERSONEL
        List<IncidentPersonnel> personnelList = incidentPersonnelRepo.findAllByIncident(oldIncident);
        for (IncidentPersonnel personnel : personnelList) {
            personnel.setIncident(newIncident);
            incidentPersonnelRepo.save(personnel);
        }
        logger.debug("incident personel has updated...");

        //UPDATE POTENCY
        List<IncidentPotency> potencyList = incidentPotencyRepo.findByIncident(oldIncident);
        for (IncidentPotency potency : potencyList) {
            potency.setIncident(newIncident);
            incidentPotencyRepo.save(potency);
        }
        logger.debug("incident potency has updated...");

        //UPDATE LOG
        List<IncidentLog> logList = incidentLogRepo.findByIncidentId(oldIncident.getIncidentid());
        for (IncidentLog log : logList) {
            log.setIncidentId(newIncident.getIncidentid());
            incidentLogRepo.save(log);
        }
        logger.debug("incident log has updated...");

        //UPDATE POB
        List<IncidentPOB> pobList = incidentPobRepo.findByIncident(oldIncident);
        for (IncidentPOB pob : pobList) {
            pob.setIncident(newIncident);
            incidentPobRepo.save(pob);
        }
        logger.debug("incident pob has updated...");

        //UPDATE WEATHER
        List<IncidentWeather> weatherList = incidentWeatherRepo.findByIncident(oldIncident);
        for (IncidentWeather weather : weatherList) {
            weather.setIncident(newIncident);
            incidentWeatherRepo.save(weather);
        }
        logger.debug("incident weather has updated...");

        //UPDATE CHECKLIST
        List<IncidentChecklist> checklistList = incidentChecklistRepo.findByIncident(oldIncident);
        for (IncidentChecklist checklist : checklistList) {
            checklist.setIncident(newIncident);
            incidentChecklistRepo.save(checklist);
        }
        logger.debug("incident checklist has updated...");

        return newIncident;
    }

    public void deleteIncident(Incident i) {
        if (i != null) {
            unattachAllIncidentRelated(i);
            i.setIsdeleted(BigInteger.ONE);
            incidentRepo.save(i);
            logger.debug("incident has deleted...");
        }
    }

    private void unattachAllIncidentRelated(Incident inc) {
        List<UtiSighting> sightingList = sightingAndHearingRepo.findAll();
        for (UtiSighting sighting : sightingList) {
            sighting.setIncident(null);
            sightingAndHearingRepo.save(sighting);
        }
        logger.debug("sighting has updated...");
        List<IncidentLog> logList = incidentLogRepo.findByIncidentId(inc.getIncidentid());
        for (IncidentLog log : logList) {
            log.setIncidentId(null);
            incidentLogRepo.save(log);
        }
        logger.debug("incident log has updated...");
        List<UtiBeaconComposite> compositeList = beaconCompositeRepo.findAllByIncidentid(inc);
        for (UtiBeaconComposite composite : compositeList) {
            composite.setIncidentid(null);
            beaconCompositeRepo.save(composite);
        }
        logger.debug("beacon composite has updated...");
    }

    public String formatIncidentId(Incident i) {
        SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        String fromDate = sdfToStr.format(i.getAlertedat());
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incidentId = "";
        List<Incident> lis = incidentRepo.findAllByIncidentidContaining(i.getUsersiteid());
        if (lis.isEmpty()) {
            incidentId = i.getUsersiteid() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = incidentRepo.findIncidentByMaxId(i.getUsersiteid());
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1]) || Integer.parseInt(splitId[1]) > Integer.parseInt(splitDate[0] + splitDate[1])) {
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
                if (Integer.parseInt(splitId[1]) > Integer.parseInt(splitDate[0] + splitDate[1])) {
                    incidentId = i.getUsersiteid() + "-" + splitId[1] + "-" + nextval;
                } else {
                    incidentId = i.getUsersiteid() + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
                }
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incidentId = i.getUsersiteid() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incidentId;
    }

    public String formatIncidentNumber(Incident i) {
        String incidentNumber = "";
        String jns = "";
        SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYYMMDDHHMM);
        String fromDate = sdfToStr.format(i.getAlertedat());
        switch (i.getIncidenttype()) {
            case 1:
                jns = ProsiaConstant.PELAYARAN;
                break;
            case 2:
                jns = ProsiaConstant.PENERBANGAN;
                break;
            case 3:
                jns = ProsiaConstant.LAINLAIN;
                break;
            case 4:
                jns = ProsiaConstant.BENCANA;
                break;
            default:
                break;
        }

        String tz = "";
        if (i.getAlertedattimezone().equals(Timezone.GMT_PLUS_7.getTimezone())) {
            tz = Timezone.GMT_PLUS_7.getShorttimezone();
        } else if (i.getAlertedattimezone().equals(Timezone.GMT_PLUS_8.getTimezone())) {
            tz = Timezone.GMT_PLUS_8.getShorttimezone();
        } else if (i.getAlertedattimezone().equals(Timezone.GMT_PLUS_9.getTimezone())) {
            tz = Timezone.GMT_PLUS_9.getShorttimezone();
        } else if (i.getAlertedattimezone().equals(Timezone.GMT_PLUS_0.getTimezone())) {
            tz = Timezone.GMT_PLUS_0.getShorttimezone();
        }
        incidentNumber = i.getUsersiteid() + "-" + jns + "-" + fromDate + tz;
        return incidentNumber;
    }
}
