/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 * clusterMarkerID is ID of marker this value given when it will be adding to ClusterMarker class
 * @see ClusterManager.add
 * @see ClusterManager.remove
 */

var clusterMarker;
var clusterManager;
var eBubble;
var SEARCH_PATTERN = new Array(); //variable of Array contain every type Search Pattern
var LAT; //variable containing latitude value when map clicked
var LNG; //variable containing longitude value when map clicked
var MODE; //variable containing current mode for determine when map clicked whether making Saerch Pattern or Search Area or Search Pattern Free Define or Measure 
var polylineMeasure; //variable containing current PolyLineMasure on map
var polygon; //
var currentFreeDefine; //variable containing current Saerch Pattern Free Define has focused on Map
var placemark; //variable containing placemark(an Object for help user in define latlng position from map to Application SAR MDI(only one at a Session))
var currentCustomArea; //variable containing current Custome Search Area has focused on Map
var count = 0;

var startSA;
var latlngsSA;



function reinit(){
    map.setCenter(INDONESIA_POSITION, 4);
}

/**
 *Menghapus semua Overlay pada map kecuali IncidentMarker
 **/
function resetMapOverlay(){
    
    map.clearOverlays();
    
    //set null reference Object
    polylineMeasure = null;
    current_SearchArea = null;
    placemark = null;
    AUTO_GENERATE_STATE = 0;
    //set new variable Collection
    setNewPatterns();
    setNewGResource();
    setNewGComposite();
    setNewGKansar();
    setNewGAsset();
    setNewGPotency();
    setNewGIncidentAsset();
    setNewGIncidentPotency();
    setNewGPossar();
    setNewGPOB();
    setNewGSighting();
    setNewGBeaconElemental();
    setNewGBeaconComposite();
    setNewGLog();
    LayerCollection = new Hash();
    TASK_SEARCH_AREA = new Hash();
}





function mapClick(mouseevent){                            
    latlng = mouseevent.latLng;
    if(!latlng){
        LAT = undefined;					
        LNG = undefined;
        return;
    }
    LAT = latlng.lat();
    LNG = latlng.lng();
    if(MODE=='PLACEMARK'){
        makePlacemark(latlng);
    }else if(MODE =='MEASURE'){
        if(polylineMeasure){
            polylineMeasure.add(latlng);
        }else{
            polylineMeasure = new MeasurePolyline(latlng);
        }
    }else if(MODE == 'SEARCH_AREA'){
        if(current_SearchArea){
            current_SearchArea.add(latlng);
        }else{
            current_SearchArea = new SearchArea(latlng, map);
        }
    /*
         *Code integrasi Free Define Pattern dengan POI dipasang pada method integrasi di file MeasurePolygon.js
         */
    }else if(MODE == 'FREE_DEFINE_PATTERN'){
        if(currentFreeDefine){
            currentFreeDefine.add(latlng);
        }else{
            document.title = "FREE_DEFINE_PATTERN_CREATE_NEW#"+latlng.lat()+"#"+latlng.lng();
        }
    }else if(MODE == 'CUSTOM_SEARCH_AREA'){
        if(currentCustomArea){
            currentCustomArea.add(latlng);
        }else{
            document.title = "CUSTOM_SEARCH_AREA_CREATE_NEW#"+latlng.lat()+"#"+latlng.lng();
        }
    }else if(MODE == 'SEARCH_PATTERN'){
        document.title = "SEARCH_PATTERN_CREATE_NEW#"+latlng.lat()+"#"+latlng.lng();
    }
}










//method for fusion maps 
function showFusionLayer(layerID){
    var intLayerID = parseInt(layerID, 10);
    map.showFusionLayer(intLayerID);
}

function hideFusionLayer(layerID){
    var intLayerID = parseInt(layerID, 10);
    map.hideFusionLayer(intLayerID);
}


function createGIncident(latlng, id, numb, name, location, type){
    if(!INCIDENT.hasItem(id)){
        INCIDENT.setItem(id, new GIncident(latlng, numb, name, location, type));
    }
}


function createGResource(latlng, resourceID, description, imageName){
    if(!RESOURCE.hasItem(resourceID)){
        RESOURCE.setItem(resourceID, new GResource(latlng, resourceID, description, imageName));
    }
}

function createGAsset(latlng , assetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, kapasitas, imageName, degreeToIncident, distanceToIncident){
    if(!ASSET.hasItem(assetID)){
        ASSET.setItem(assetID, new GAsset(latlng , assetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, kapasitas, imageName, degreeToIncident, distanceToIncident));
    }
}


function createGPotency(latlng, potencyID, potencyName, telphone1, telphone2, telphone3, unsurSars, imageName, degreeToIncident, distanceToIncident){
    if(!POTENCY.hasItem(potencyID)){
        POTENCY.setItem(potencyID, new GPotency(latlng, potencyID, potencyName, telphone1, telphone2, telphone3, unsurSars, imageName, degreeToIncident, distanceToIncident));
    }
}



function createGBeaconComposite(dopplerLatlng, gpsLatLng, compositeID, beaconID, transmitDtg, spaceCraftID, isNewBeacon){
    if(!COMPOSITE.hasItem(compositeID)){
        COMPOSITE.setItem(compositeID, new GBeaconComposite(dopplerLatlng, gpsLatLng, compositeID, beaconID, transmitDtg, spaceCraftID, isNewBeacon));
    }
}

function createGBeaconElemental(latlng, elementalID, beaconID, transmitDtg, spaceCraftID, isNewBeacon){
    if(!ELEMENTAL.hasItem(elementalID)){
        ELEMENTAL.setItem(elementalID, new GBeaconElemental(latlng, elementalID, beaconID, transmitDtg, spaceCraftID, isNewBeacon));
    }
}

function createGResult(latlng, resultID, formattedAddress , viewport, pIcon){
    if(!RESULT.hasItem(resultID)){
        RESULT.setItem(resultID, new GResult(latlng, resultID , formattedAddress, viewport , pIcon));
    }
}
function createGKansar(latlng, kansarID, kansarName, alamat, distanceToIncident, arrowTodegree){
    if(!KANSAR.hasItem(kansarID)){
        KANSAR.setItem(kansarID, new GKansar(latlng, kansarID , kansarName, alamat , distanceToIncident, arrowTodegree));
    }
}

function createGPossar(latlng, possarID, description, distanceToIncident, arrowTodegree){
    if(!POSSAR.hasItem(possarID)){
        POSSAR.setItem(possarID, new GPossar(latlng, possarID , description ,distanceToIncident, arrowTodegree));
    }
}


function createGSighting(latlng, sightingID, noSighting, phone, name, location, reportType, objectType,kansar, incidentNumber){
    if(!SIGHTING.hasItem(sightingID)){
        SIGHTING.setItem(sightingID, new GSighting(latlng, sightingID, noSighting, phone, name, location, reportType, objectType,kansar, incidentNumber));
    }
}

function createGPOB(latlng, pobID, incidentID, distance, arrow){
    if(!POB.hasItem(pobID)){
        POB.setItem(pobID, new GPOB(latlng, pobID, incidentID, distance, arrow));
    }
}


function createGIncidentPotency(latlng, incidentPotencyID, potencyName, incidentID, telphone1, telphone2, telphone3, unsurSars, imageName){
    if(!INC_POTENCY.hasItem(incidentPotencyID)){
        INC_POTENCY.setItem(incidentPotencyID, new GIncidentPotency(latlng, incidentPotencyID, potencyName, incidentID, telphone1, telphone2, telphone3, unsurSars, imageName));
    }
}



function createGIncidentAsset( latlng , incidentAssetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, imageName){
    if(!INC_ASSET.hasItem(incidentAssetID)){
        INC_ASSET.setItem(incidentAssetID, new GIncidentAsset( latlng , incidentAssetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, imageName));
    }
}


function createGLog(latlng, logID, logType){
    if(!LOG.hasItem(logID)){
        LOG.setItem(logID, new GLog(latlng, logID, logType));
    }
}



function createMeasurePolyline(vertexs){
    polylineMeasure = new MeasurePolyline(null, vertexs);
}



/**
 *function untuk memberikan nilai pada variable MODE yang menentukan alur program
 *pada saat MAP DICLICK
 **/
function setMODE(value){
    MODE = value;
}

