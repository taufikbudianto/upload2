/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var COMPOSITE = new Hash();
function setNewGComposite(){
    COMPOSITE = new Hash();
}

	
function GBeaconComposite(dopplerLatlng, gpsLatlng, compositeID, beaconID, transmitDtg, spaceCraftID, isNewBeacon){
    this.isNewBeacon = isNewBeacon;
    this.initDopplerMarker(dopplerLatlng, compositeID, beaconID, spaceCraftID, transmitDtg);
    if(gpsLatlng){
        this.initGPSMarker(gpsLatlng, compositeID, beaconID, spaceCraftID, transmitDtg);
    }
}
	
        
        
GBeaconComposite.prototype.initGPSMarker = function(gpsLatlng, compositeID, beaconID, spaceCraftID, transmitDtg){
    this.gpsMarker = new GObject();
    this.gpsMarker.setIdentifier(compositeID + " (Beacon Composite GPS)");
    this.gpsMarker.setLatLng(gpsLatlng);
    if(this.isNewBeacon){
        this.gpsMarker.setSymbol(BEACON_COMPOSITE_GPS_SYMBOL_GIS);
    }else{
        this.gpsMarker.setSymbol(BEACON_COMPOSITE_GPS_SYMBOL_GIS);
    }
    var popupInfoGPS = "<b class='title'><u>Beacon Composite GPS</u></b>"+
    "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(gpsLatlng.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(gpsLatlng.lng(), LONGITUDE_MODE) + "</font>" +
    "<br><b class='field'>Beacon Composite ID</b>: <font class='value'>" + compositeID + "</font>" +
    "<br><b class='field'>Satellite ID</b>   : <font class='value'>" + spaceCraftID + "</font>" +
    "<br><b class='field'>Transmit DTG</b>   : <font class='value'>" + transmitDtg + "</font>";
    this.gpsMarker.setPopupInfo(popupInfoGPS);
    ge.getFeatures().appendChild(this.gpsMarker.marker);
    this.gpsMarker.addClickListener();
}




GBeaconComposite.prototype.initDopplerMarker = function(dopplerLatlng, compositeID, beaconID, spaceCraftID, transmitDtg){
    this.dopplerMarker = new GObject();
    this.dopplerMarker.setIdentifier(compositeID + " (Beacon Composite Doppler)");
    this.dopplerMarker.setLatLng(dopplerLatlng);
    if(this.isNewBeacon){
        this.dopplerMarker.setSymbol(BEACON_COMPOSITE_BLINK_1_DEFAULT_SYMBOL_GIS);
        
    }else{
        this.dopplerMarker.setSymbol(BEACON_COMPOSITE_DEFAULT_SYMBOL_GIS);
    }
    var popupInfoDoppler = "<b class='title'><u>Beacon Composite Doppler</u></b>"+
    "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(dopplerLatlng.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(dopplerLatlng.lng(), LONGITUDE_MODE) + "</font>" +
    "<br><b class='field'>Beacon Composite ID</b>: <font class='value'>" + compositeID + "</font>" +
    "<br><b class='field'>Beacon ID</b>: <font class='value'>" + beaconID + "</font>" +
    "<br><b class='field'>Satellite ID</b>   : <font class='value'>" + spaceCraftID + "</font>" +
    "<br><b class='field'>Transmit DTG</b>   : <font class='value'>" + transmitDtg + "</font>";
    this.dopplerMarker.setPopupInfo(popupInfoDoppler);
    ge.getFeatures().appendChild(this.dopplerMarker.marker);
    this.dopplerMarker.addClickListener();
}





        
/**
     *Menampilkan pada Map semua marker beacon composite yang ditampung pada array COMPOSITE
     **/
function showAllGGBeaconComposite(){
    for(i in COMPOSITE.items){
        if(COMPOSITE.items[i] != null){
            if(COMPOSITE.items[i].gpsMarker){
                map.addOverlay(COMPOSITE.items[i].gpsMarker.marker);
            }
            map.addOverlay(COMPOSITE.items[i].doppler.marker);
        }
    }
}
	
/**
     *Remove semua Object Beacon Composite yang ada pada Map
     */
function removeAllGBeaconComposite(){
    for(i in COMPOSITE.items){
        if(COMPOSITE.items[i] != null){
            ge.getFeatures().removeChild(COMPOSITE.items[i].dopplerMarker.marker);
            COMPOSITE.items[i].dopplerMarker.marker.release();
            if(COMPOSITE.items[i].gpsMarker){
                ge.getFeatures().removeChild(COMPOSITE.items[i].gpsMarker.marker);
                COMPOSITE.items[i].gpsMarker.marker.release();
            }
        }
    }
    COMPOSITE.clear();
}
	
	
/**
     *Remove pada map GBeaconComposite berdasarkan IDnya 
     */
function removeGBeaconComposite(compositeID){
    ge.getFeatures().removeChild(COMPOSITE.getItem(compositeID).dopplerMarker.marker);
    COMPOSITE.getItem(compositeID).dopplerMarker.marker.release();
    if(COMPOSITE.getItem(compositeID).gpsMarker){
        ge.getFeatures().removeChild(COMPOSITE.getItem(compositeID).gpsMarker.marker);
        COMPOSITE.getItem(compositeID).gpsMarker.marker.release();   
    }
    COMPOSITE.removeItem(compositeID);
}
	
