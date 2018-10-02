/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.text.SimpleDateFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.SightingAndHearingRepo;

/**
 *
 * @author PROSIA
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class SightingService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SightingAndHearingRepo sightingRepo;

    public void saveSighting(UtiSighting obj) {
        if (org.apache.commons.lang3.StringUtils.isBlank(obj.getUsersiteId())) {
            obj.setUsersiteId(ApplicationPropertiesHandler.getProperty(ApplicationPropertiesHandler.CURRENT_SITE_KEY));
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(obj.getSightingId())) {
            obj.setDateCreated(DateUtils.getSynchronizedCurrentDate());
            obj.setSightingId(getSightingId(obj));
            obj.setSightingNumber(generateSightingNumber(obj));
        }
        obj.setIsDeleted(false);
        obj.setIscrm(false);
        sightingRepo.save(obj);

    }

    public String getSightingId(UtiSighting us) {
        SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        String fromDate = sdfToStr.format(us.getDateCreated());
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String sightingId = "";
        List<UtiSighting> lis = sightingRepo.findBySightingIdContaining(us.getUsersiteId());
        if (lis.isEmpty()) {
            sightingId = us.getUsersiteId() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = sightingRepo.findUtiSightingByMaxId(us.getUsersiteId());
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
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
                sightingId = us.getUsersiteId() + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                sightingId = us.getUsersiteId() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return sightingId;
    }

    private String generateSightingNumber(UtiSighting sighting) {
        String date = "";
        String areacode = "";
        if (sighting.getDateCreated() != null) {
            date = Tanggal.sarDateTimeStringConvert(sighting.getDateCreated(),
                    ApplicationPropertiesHandler.getProperty(ApplicationPropertiesHandler.DATE_HOUR_DIFFERENCE_KEY));
        }
        if (sighting.getObserverPhone() != null && !sighting.getObserverPhone().equalsIgnoreCase("")) {
            String phone = sighting.getObserverPhone();
            if (phone.startsWith("6221") || phone.startsWith("6222") || phone.startsWith("6261") || phone.startsWith("6224") || phone.startsWith("6231")) {
                areacode = phone.substring(2, 4);
            } else {
                areacode = phone.substring(2, 5);
            }
        }
        return areacode + "-" + date;
    }
}
