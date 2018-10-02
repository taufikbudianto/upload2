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
    this.addToClusterManager();
    
    if(this.isNewBeacon){
        /*
        if(gpsLatlng){
            this.gpsMarker.marker.setImage(BEACON_COMPOSITE_GPS_SYMBOL_GIS);
        }
        */
        this.dopplerMarker.setOptions({
            optimized : false
        });
        this.dopplerMarker.marker.setIcon(new google.maps.MarkerImage(
            BEACON_COMPOSITE_BLINK_1_DEFAULT_SYMBOL_GIS,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            ));   
    }
}
	
GBeaconComposite.prototype = new GObject();
        
//JIKA GPS MAKA MASUKANNYA ADALAH GPS
//JIKA DOPPLER MAKA MASUKANNYA ADALAH DOPPLER
//2. GBEACON_COMPOSITE_TO_DO
GBeaconComposite.prototype.initGPSMarker = function(gpsLatlng, compositeID, beaconID, spaceCraftID, transmitDtg){
    this.gpsMarker = new GObject();
    /*
    if(this.isNewBeacon){
        this.gpsMarker.marker = new PdMarker(gpsLatlng);
        this.gpsMarker.marker.blink(true, 500);
    }else{
     */
    this.gpsMarker.marker = CustomIconMarker(BEACON_COMPOSITE_GPS_SYMBOL_GIS, gpsLatlng, compositeID + " (Beacon Composite GPS)");
    /*
    }
    */
    google.maps.event.addListener(this.gpsMarker.marker, "click", function() {
        eBubble.openOnMarker(this,
            "<b class='title'><u>Beacon Composite GPS</u></b>"+
            "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(gpsLatlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(gpsLatlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Beacon Composite ID</b>: <font class='value'>" + compositeID + "</font>" +
            "<br><b class='field'>Satellite ID</b>   : <font class='value'>" + spaceCraftID + "</font>" +
            "<br><b class='field'>Transmit DTG</b>   : <font class='value'>" + transmitDtg + "</font>");
    });
}
 
 
 
    
//2. GBEACON_COMPOSITE_TO_DO    
GBeaconComposite.prototype.initDopplerMarker = function(dopplerLatlng, compositeID, beaconID, spaceCraftID, transmitDtg){
    this.dopplerMarker = new GObject();
    /*
    if(this.isNewBeacon){
        this.dopplerMarker.marker = new PdMarker(dopplerLatlng);
        this.dopplerMarker.marker.blink(true, 500);
    }else{
    */
    this.dopplerMarker.marker = CustomIconMarker(BEACON_COMPOSITE_DEFAULT_SYMBOL_GIS, dopplerLatlng, compositeID + " (Beacon Composite Doppler)");
    /*
    }
    */
    google.maps.event.addListener(this.dopplerMarker.marker, "click", function() {
        eBubble.openOnMarker(this,
            "<b class='title'><u>Beacon Composite Doppler</u></b>"+
            "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(dopplerLatlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(dopplerLatlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Beacon Composite ID</b>: <font class='value'>" + compositeID + "</font>" +
            "<br><b class='field'>Beacon ID</b>: <font class='value'>" + beaconID + "</font>" +
            "<br><b class='field'>Satellite ID</b>   : <font class='value'>" + spaceCraftID + "</font>" +
            "<br><b class='field'>Transmit DTG</b>   : <font class='value'>" + transmitDtg + "</font>");
    });
}
        
        

        
        

GBeaconComposite.prototype.addToClusterManager = function(){
    if(this.gpsMarker){
        clusterMarker.addMarkers([this.dopplerMarker.marker, this.gpsMarker.marker]);
    }else{
        clusterMarker.addMarker(this.dopplerMarker.marker);
    }
}
/**
     *Menampilkan pada Map semua marker beacon composite yang ditampung pada array COMPOSITE
     **/
function showAllGGBeaconComposite(){
    showAllObjectMarker(COMPOSITE);
}
	
/**
     *Remove semua Object Beacon Composite yang ada pada Map
     */
function removeAllGBeaconComposite(){
    this.clearSpecifiedHash(COMPOSITE);
}
	
	
/**
 *Remove pada map GBeaconComposite berdasarkan IDnya 
 */
function removeGBeaconComposite(compositeID){
    clusterManager.remove(compositeID, COMPOSITE);
}
	
