package prosia.basarnas.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.repo.IncidentAssetRepo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PROSIA
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class IncidentPeralatanService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IncidentAssetRepo incidentAssetRepo;

    public IncidentAsset saveIncidentAsset(IncidentAsset asset) {
        try {
            Date currentDate = DateUtils.getSynchronizedCurrentDate();
            asset.setDateCreated(currentDate);
            asset.setUserSiteID(ApplicationPropertiesHandler.getProperty(
                    ApplicationPropertiesHandler.CURRENT_SITE_KEY));
            asset.setIncidentAssetID(getIncidentAssetId(asset));
            logger.debug("IncidentAssetId : {}", asset.getIncidentAssetID());
            return incidentAssetRepo.save(asset);
        } catch (Exception e) {
            logger.error("Error Save Incident Peralatan : {}", e.getMessage());
        }
        return null;
    }

    public String getIncidentAssetId(IncidentAsset ia) {
        SimpleDateFormat sdfToStr = new SimpleDateFormat(ProsiaConstant.FORMAT_YYYY_MM_DD);
        String fromDate = sdfToStr.format(ia.getDateCreated());
        String[] splitDate = fromDate.split("-");
        String nextval = "";
        String incidentAssetId = "";
        List<IncidentAsset> lis = incidentAssetRepo.findByIncidentAssetIDContaining(ia.getUserSiteID());
        if (lis.isEmpty()) {
            incidentAssetId = ia.getUserSiteID() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = incidentAssetRepo.findIncidentAssetByMaxId(ia.getUserSiteID());
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
                incidentAssetId = ia.getUserSiteID() + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                incidentAssetId = ia.getUserSiteID() + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return incidentAssetId;
    }
}
