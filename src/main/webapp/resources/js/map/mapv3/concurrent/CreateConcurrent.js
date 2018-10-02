/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function createConcurrent(json){
    for(i in json){
        try{
            json[i].gen(json[i].object);
        }catch(error){}
    }
}


function genGSighting(object){
    createGSighting(object.latlng, object.id, object.sightingNumb, object.observerPhone, object.observerName, object.observerLocation, object.reportType, object.objectType, object.userSiteID, object.incidentNumb);
}

function generateGResult(object){
    createGResult(object.latlng, object.id, object.address , object.viewport);
}

function generateGPotency(object){
    createGPotency(object.latlng, object.id, object.name, object.phone1, object.phone2, object.phone3, object.unsurSars, object.image, object.tilt, object.distance);
}

function generateGPossar(object){
    createGPossar(object.latlng, object.id, object.name, object.distance, object.tilt);
}

function generateGPOB(object){
    createGPOB(object.latlng, object.id, object.incidentID, object.distance, object.tilt);
}

function generateGLog(object){
    createGLog(object.latlng, object.id, object.logType);
}

function generateGKansar(object){
    createGKansar(object.latlng, object.id, object.name, object.address, object.distance, object.tilt);
}

function generateGIncidentPotency(object){
    createGIncidentPotency(object.latlng, object.id, object.name, object.incidentID, object.phone1, object.phone2, object.phone3, object.unsurSars, object.image);
}

function generateGIncident(object){
    createGIncident(object.latlng, object.id, object.numb, object.name, object.location, object.type);
}

function generateGIncidentAsset(object){
    createGIncidentAsset(object.latlng , object.id , object.name, object.goodQty, object.rejectedQty, object.servicedQty, object.otherOty, object.assetCondition, object.image);
}

function generateGAsset(object){
    createGAsset(object.latlng , object.id, object.name, object.goodQty, object.rejectedQty, object.servicedQty, object.otherQty, object.assetCondition, object.cargo, object.image, object.tilt, object.distance);
}

function generateGBeaconComposite(object){
    createGBeaconComposite(object.dopplerLatlng, object.gpsLatLng, object.id, object.beaconID, object.transmitDtg, object.sid, object.isNewBeacon);
}

function generateGBeaconElemental(object){
    createGBeaconElemental(object.latlng, object.id, object.beaconID, object.transmitDtg, object.sit, object.isNewBeacon);
}

// POLYLINE CONCURRENT INTERFACE

function generatePararelPattern(object){
    createPararel(object.id, object.start, object.searchLeg, object.width, object.trackSpacing, object.heading, object.pivot, object.vertexs, object.color);
}

function generateSectorPattern(object){
    createSector(object.id, object.start, object.radius, object.heading, object.pivot, object.vertexs, object.color);
}

function generateSquarePattern(object){
    createSquare(object.id, object.trackSpacing, object.radius , object.start, object.heading, object.vertexs, object.color);
}

function generateTSRPattern(object){
    createTSR(object.id, object.start, object.trackSpacing, object.searchLeg, object.heading, object.pivot, object.vertexs, object.color);
}

function generateTSNPattern(object){
    createTSN(object.id, object.start, object.trackSpacing, object.searchRadius, object.heading, object.pivot, object.vertexs, object.color);
}

function generateTrapeziumPararelPattern(object){
    createTrapeziumPararelPattern(object.id, object.name, object.start, object.minSearchLeg, object.width, object.trackSpacing, object.heading, object.maxSearchLeg, object.vertexs, object.color);
}

function generateFreeDefinePattern(object){
    createFreeDefine(object.start, object.id, object.vertexs, object.pivot);
}





//POLYGON

function generateSearchArea(object){
    createSearchArea(object.start, object.id, object.vertexs, object.parrentID);
}

function generateCustomSearchArea(object){
    createCustomArea(object.start, object.id, object.vertexs, object.pivot);
}




