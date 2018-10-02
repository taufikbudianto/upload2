/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var ELEMENTAL = new Hash();
function setNewGBeaconElemental(){
    ELEMENTAL = new Hash();
}

    
function GBeaconElemental(latlng, beaconElementalID, beaconID, transmitDtg, spaceCraftID, isNewBeacon){
    if(isNewBeacon){
        this.marker = new PdMarker();
        this.marker.blink();
    }else{
        this.marker = CustomIconMarker(BEACON_ELEMENTAL_DEFAULT_SYMBOL_GIS, latlng, beaconElementalID + " (Beacon Elemental)");
    }
    GEvent.addListener(this.marker, "click", function() {
         eBubble.openOnMarker(this, "<b class='title'><u>Beacon Elemental</u></b>" +
            "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>BeaconElemental ID</b> : <font class='value'>" + beaconElementalID + "</font>" +
            "<br><b class='field'>Satellite ID</b>   : <font class='value'>" + spaceCraftID + "</font>" +
            "<br><b class='field'>Transmit DTG</b>   : <font class='value'>" + transmitDtg + "</font>");
    });
    this.addToClusterManager();
    if(isNewBeacon){
        this.marker.setImage(BEACON_ELEMENTAL_BLINK_1_DEFAULT_SYMBOL_GIS);
    }
}

GBeaconElemental.prototype = new GObject();

/**
 *Menampilkan pada Map semua marker beaconElemental yang ditampung pada array ELEMENTAL
 **/
function showAllGBeaconElemental(){
    showAllObjectMarker(ELEMENTAL);
}
    
	
/**
 *Remove semua Object BeaconElemental yang ada pada Map
 */
	
function removeAllGBeaconElemental(){
    removeAllObjectMarker(ELEMENTAL);
}
	
/**
 *Remove pada map GBeaconElemental berdasarkan IDnya 
 **/
function removeGBeaconElemental(beaconElementalID){
    removeObjectMarker(beaconElementalID, ELEMENTAL);
}

