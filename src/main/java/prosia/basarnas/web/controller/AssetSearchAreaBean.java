/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import lombok.Data;
import prosia.basarnas.model.DriftCalcWorksheet2;
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.basarnas.model.IncidentAsset;

/**
 *
 * @author Aris
 */
public @Data class AssetSearchAreaBean {
    private String worksheetID;
    private DriftCalcWorksheet2 worksheet2;
    private DriftCalcWorksheet8 worksheet8;
    private IncidentAsset incidentAsset;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private Double endurance;
    private Double practicalTrackSpacing;
    private Double searchHours;
    private Double searchArea;
}
