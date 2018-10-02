/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import org.primefaces.model.map.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosia.basarnas.service.CommonConstant;
import prosia.basarnas.service.MapCalculation;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.model.map.GenerateValueRequestBean.GenerateValueState;
import prosia.basarnas.web.model.map.SearchPatternParameter;
import prosia.basarnas.web.model.map.TrapeziumSearchArea;

/**
 *
 * @author Ismail
 */
public class GisCalcES implements java.io.Serializable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Double trackSpacing;

    public GisCalcES() {
        try {
//            trackSpacing = Double.valueOf(ApplicationPropertiesES.getDefaultSearchPatternTrackSpacing());
            trackSpacing = Double.valueOf(0.25);
        } catch (Exception e) {
            trackSpacing = Double.valueOf(0.25);
        }
    }

    /**
     * Method to get search pattern generate value
     */
    public SearchPatternParameter generatePararelValue(GenerateValueRequestBean requestBean) {
        SearchPatternParameter result = new SearchPatternParameter();
        result.setIsSearchTrackSpacing(true);
        result.setIsSearchWidth(true);
        result.setIsSearchLeg(true);
        result.setIsSearchHeading(true);
        if (requestBean.getState() == GenerateValueState.DRIFT_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltDrift() < 0) {
                heading = 360 + requestBean.getTiltDrift();
            } else {
                heading = requestBean.getTiltDrift();
            }
            result.setHeading(heading);;
            result.setSearchLeg(requestBean.getRadiusDrift() * 2);
            result.setWidth(requestBean.getRadiusDrift() * 2);
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(trackSpacing);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            result.setSearchLeg(result.getSearchLeg() - result.getTrackSpacing());
            result.setWidth(result.getWidth() - result.getTrackSpacing());
            LatLng datum = requestBean.getDatumPoint();
            double latSpaceCorner, lngSpaceCorner, latitudeStart, longitudeStart;
            latSpaceCorner = result.getWidth() / 2;
            lngSpaceCorner = result.getSearchLeg() / 2;
            latitudeStart = datum.getLat() + (latSpaceCorner * CommonConstant.ONE_NM_TO_DEG);
            longitudeStart = datum.getLng() - (lngSpaceCorner * CommonConstant.ONE_NM_TO_DEG);
            LatLng start = new LatLng(latitudeStart, longitudeStart);
            result.setStart(start);
            result.setPivot(datum);
        } else if (requestBean.getState() == GenerateValueState.TASK_SEARCH_AREA) {
            //penamaan property pada task search area dan parameter script ada yang berbeda
            //jika garis horizontal dari titik start sebelum dirotasi
            //- task search area : width
            //- paparel seaarch pattern : height
            //jika garis vertical dari titik start sebelum dirotasi
            //- task search area : height
            //- paparel seaarch pattern : search leg
            double heading;
            if (requestBean.getTiltTaskSearchArea() < 0) {
                heading = 360 + requestBean.getTiltTaskSearchArea();
            } else {
                heading = requestBean.getTiltTaskSearchArea();
            }
            result.setHeading(heading);
            result.setSearchLeg(requestBean.getWidthTaskSearchArea());
            result.setWidth(requestBean.getHeightTaskSearchArea());
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(trackSpacing);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            result.setSearchLeg(result.getSearchLeg() - result.getTrackSpacing());
            result.setWidth(result.getWidth() - result.getTrackSpacing());
            LatLng pivot = requestBean.getPivotTaskSearchArea();
            double latSpaceCorner, lngSpaceCorner, latitudeStart, longitudeStart;
            latSpaceCorner = result.getWidth() / 2;
            lngSpaceCorner = result.getSearchLeg() / 2;
            latitudeStart = pivot.getLat() + (latSpaceCorner * CommonConstant.ONE_NM_TO_DEG);
            longitudeStart = pivot.getLng() - (lngSpaceCorner * CommonConstant.ONE_NM_TO_DEG);
            LatLng start = new LatLng(latitudeStart, longitudeStart);
            result.setStart(start);
            result.setPivot(pivot);
        }
        logger.debug("Pararel pattern heading = " + result.getHeading());
        logger.debug("Pararel pattern searchradius = " + result.getSearchLeg());
        logger.debug("Pararel pattern start = " + result.getStart());
        logger.debug("Pararel pattern track spacing = " + result.getTrackSpacing());
        logger.debug("Pararel pattern width = " + result.getWidth());
        return result;
    }

    public SearchPatternParameter generateSquareValue(GenerateValueRequestBean requestBean) {
        SearchPatternParameter result = new SearchPatternParameter();
        result.setIsSearchTrackSpacing(true);
        result.setIsSearchRadius(true);
        result.setIsSearchHeading(true);
        if (requestBean.getState() == GenerateValueState.DRIFT_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltDrift() < 0) {
                heading = 360 + requestBean.getTiltDrift();
            } else {
                heading = requestBean.getTiltDrift();
            }
            result.setHeading(heading);
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(trackSpacing);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            Double searchRadius = requestBean.getRadiusDrift();
            result.setSearchRadius(searchRadius);
            result.setStart(requestBean.getDatumPoint());
            result.setPivot(requestBean.getDatumPoint());
        } else if (requestBean.getState() == GenerateValueState.TASK_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltTaskSearchArea() < 0) {
                heading = 360 + requestBean.getTiltTaskSearchArea();
            } else {
                heading = requestBean.getTiltTaskSearchArea();
            }
            result.setHeading(heading);
            Double searchRadius;
            // tentukan nilai searchRadius dari square search pattern
            // nilainya adalah nilai yang terkecil antara width dan height tasking
            if (requestBean.getHeightTaskSearchArea() > requestBean.getWidthTaskSearchArea()) {
                searchRadius = requestBean.getWidthTaskSearchArea() / 2;
            } else {
                searchRadius = requestBean.getHeightTaskSearchArea() / 2;
            }
            result.setSearchRadius(searchRadius);
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(trackSpacing);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            result.setStart(requestBean.getPivotTaskSearchArea());
            result.setPivot(requestBean.getPivotTaskSearchArea());
        }
        logger.debug("Square pattern heading = " + result.getHeading());
        logger.debug("Square pattern searchradius = " + result.getSearchRadius());
        logger.debug("Square pattern start = " + result.getStart());
        logger.debug("Square pattern trackspacing = " + result.getTrackSpacing());
        return result;
    }

    public SearchPatternParameter generateSectorValue(GenerateValueRequestBean requestBean) {
        SearchPatternParameter result = new SearchPatternParameter();
        result.setIsSearchRadius(true);
        result.setIsSearchHeading(true);
        if (requestBean.getState() == GenerateValueState.DRIFT_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltDrift() < 0) {
                heading = 360 + requestBean.getTiltDrift();
            } else {
                heading = requestBean.getTiltDrift();
            }
            result.setHeading(heading);
            result.setSearchRadius(requestBean.getRadiusDrift());
            LatLng datum = requestBean.getDatumPoint();
            double latitude = datum.getLat() - (result.getSearchRadius() * CommonConstant.ONE_NM_TO_DEG);
            LatLng start = new LatLng(latitude, datum.getLng());
            result.setStart(start);
            result.setPivot(datum);
        } else if (requestBean.getState() == GenerateValueState.TASK_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltTaskSearchArea() < 0) {
                heading = 360 + requestBean.getTiltTaskSearchArea();
            } else {
                heading = requestBean.getTiltTaskSearchArea();
            }
            result.setHeading(heading);
            Double lengthOfSector;
            // tentukan nilai yang akan menjadi panjang sisi dari sector search pattern
            // nilainya adalah nilai yang terkecil antara width dan height tasking
            if (requestBean.getHeightTaskSearchArea() < requestBean.getHeightTaskSearchArea()) {
                lengthOfSector = requestBean.getHeightTaskSearchArea();
            } else {
                lengthOfSector = requestBean.getWidthTaskSearchArea();
            }
            result.setSearchRadius(lengthOfSector / 2);
            LatLng pivot = requestBean.getPivotTaskSearchArea();
            LatLng start = new LatLng(pivot.getLat()
                    - (result.getSearchRadius() * CommonConstant.ONE_NM_TO_DEG), pivot.getLng());
            result.setStart(start);
            result.setPivot(pivot);
        }
        logger.debug("Sector pattern heading = " + result.getHeading());
        logger.debug("Sector pattern searchradius = " + result.getSearchRadius());
        logger.debug("Sector pattern start = " + result.getStart());
        return result;
    }

    public SearchPatternParameter generatePararelValue(TrapeziumSearchArea area) {
        SearchPatternParameter result = new SearchPatternParameter();
        result.setIsSearchWidth(true);
        result.setIsSearchTrackSpacing(true);
        result.setIsSearchHeading(true);
        result.setIsSearchMaxLeg(true);
        result.setIsSearchMinLeg(true); 
        result.setIsSearchLeg(false); 
        result.setIsSearchRadius(false);        
        result.setWidth(area.getWidth());
        result.setHeading(area.getHeading());
        result.setTrackSpacing(area.getTrackSpacing());
        result.setMinSearchLeg(area.getMinSearchLeg());
        result.setMaxSearchLeg(area.getMaxSearchLeg());
        result.setStart(area.getStartTrapeziumPararelPattern());
        return result;
    }

    public SearchPatternParameter generateTSRValue(GenerateValueRequestBean requestBean) {
        SearchPatternParameter result = new SearchPatternParameter();
        result.setIsSearchTrackSpacing(true);
        result.setIsSearchLeg(true);
        result.setIsSearchHeading(true);
        if (requestBean.getState() == GenerateValueState.DRIFT_SEARCH_AREA) {
            double heading;
            if (requestBean.getLkpToDatumAngle() < 0) {
                heading = 360 + requestBean.getLkpToDatumAngle();
            } else {
                heading = requestBean.getLkpToDatumAngle();
            }
            result.setHeading(heading);
            result.setSearchLeg(requestBean.getLkpToDatumDistance());
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(result.getSearchLeg() / 4);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            LatLng lkp = requestBean.getLkpPoint();
            LatLng orginalStart = new LatLng(lkp.getLat(), (lkp.getLng()
                    - (result.getTrackSpacing() / 2 * CommonConstant.ONE_NM_TO_DEG)));
            result.setStart(orginalStart);
            result.setPivot(requestBean.getDatumPoint());
        } else if (requestBean.getState() == GenerateValueState.TASK_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltTaskSearchArea() < 0) {
                heading = 360 + requestBean.getTiltTaskSearchArea();
            } else {
                heading = requestBean.getTiltTaskSearchArea();
            }
            result.setHeading(heading);
            result.setSearchLeg(requestBean.getHeightTaskSearchArea());
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(result.getSearchLeg() / 4);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            if (requestBean.getLkpPoint() == null) {
                LatLng pivot = requestBean.getPivotTaskSearchArea();
                LatLng orginalStart = new LatLng(pivot.getLat()
                        - (result.getSearchLeg() / 2 * CommonConstant.ONE_NM_TO_DEG),
                        pivot.getLng() - (result.getTrackSpacing() * CommonConstant.ONE_NM_TO_DEG));
                LatLng start = MapCalculation.rotation(pivot, orginalStart, result.getHeading());
                result.setStart(start);
            } else {
                LatLng lkp = requestBean.getLkpPoint();
                LatLng orginalStart = new LatLng(lkp.getLat(), (lkp.getLng()
                        - (result.getTrackSpacing() / 2 * CommonConstant.ONE_NM_TO_DEG)));
                LatLng start = MapCalculation.rotation(lkp, orginalStart, result.getHeading());
                result.setStart(start);

            }
            result.setPivot(requestBean.getPivotTaskSearchArea());
        }
        logger.debug("TSR pattern heading = " + result.getHeading());
        logger.debug("TSR pattern searchleg = " + result.getSearchLeg());
        logger.debug("TSR pattern start = " + result.getStart());
        logger.debug("TSR pattern trackspacing = " + result.getTrackSpacing());
        return result;
    }

    public SearchPatternParameter generateTSNValue(GenerateValueRequestBean requestBean) {
        SearchPatternParameter result = new SearchPatternParameter();
        result.setIsSearchTrackSpacing(true);
        result.setIsSearchLeg(true);
        result.setIsSearchHeading(true);
        if (requestBean.getState() == GenerateValueState.DRIFT_SEARCH_AREA) {
            double heading;
            if (requestBean.getLkpToDatumAngle() < 0) {
                heading = 360 + requestBean.getLkpToDatumAngle();
            } else {
                heading = requestBean.getLkpToDatumAngle();
            }
            result.setHeading(heading);
            result.setSearchLeg(requestBean.getLkpToDatumDistance());
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(result.getSearchLeg() / 4);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            result.setStart(requestBean.getLkpPoint());
            result.setPivot(requestBean.getDatumPoint());
        } else if (requestBean.getState() == GenerateValueState.TASK_SEARCH_AREA) {
            double heading;
            if (requestBean.getTiltTaskSearchArea() < 0) {
                heading = 360 + requestBean.getTiltTaskSearchArea();
            } else {
                heading = requestBean.getTiltTaskSearchArea();
            }
            result.setHeading(heading);
            result.setSearchLeg(requestBean.getHeightTaskSearchArea());
            if (requestBean.getTrackSpacing() == null || requestBean.getTrackSpacing() == 0) {
                result.setTrackSpacing(result.getSearchLeg() / 4);
            } else {
                result.setTrackSpacing(requestBean.getTrackSpacing());
            }
            if (requestBean.getLkpPoint() == null) {
                LatLng pivot = requestBean.getPivotTaskSearchArea();
                LatLng orginalStart = new LatLng(pivot.getLat()
                        - (result.getSearchLeg() / 2 * CommonConstant.ONE_NM_TO_DEG), pivot.getLng());
                LatLng start = MapCalculation.rotation(pivot, orginalStart, result.getHeading());
                result.setStart(start);
            } else {
                result.setStart(requestBean.getLkpPoint());
            }
            result.setPivot(requestBean.getPivotTaskSearchArea());
        }
        logger.debug("TSN pattern heading = " + result.getHeading());
        logger.debug("TSN pattern searchleg = " + result.getSearchLeg());
        logger.debug("TSN pattern start = " + result.getStart());
        logger.debug("TSN pattern trackspacing = " + result.getTrackSpacing());
        return result;
    }

}
