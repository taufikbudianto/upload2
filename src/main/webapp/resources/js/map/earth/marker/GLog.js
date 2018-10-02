/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var LOG = new Hash();
function setNewGLog(){
    LOG = new Hash();
}

    
function GLog(latlng, logID, logType){
    this.setIdentifier(logID);
    this.setLatLng(latlng);
    this.setSymbol(LOG_DEFAULT_SYMBOL_GIS);
    var infoPopUp = "<b class='title'><u>Log</u></b>" +
            "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Log id </b> : <font class='value'>" +  logID + "</font>" +
            "<br><b class='field'>Log Type </b> : <font class='value'>" +  logType + "</font>";
   
    this.setPopupInfo(infoPopUp);
    ge.getFeatures().appendChild(this.marker);
    this.addClickListener();
}



GLog.prototype = new GObject();

/**
     *Menampilkan pada Map semua marker pos sar yang ditampung pada array LOG
     **/
function showAllGLog(){
    showAllObjectMarker(LOG);
}
    
	
/**
    *Remove semua Object Possar yang ada pada Map
    */
	
function removeAllGLog(){
    removeAllObjectMarker(LOG);
}
	
/**
     *Remove pada map GLog berdasarkan IDnya 
     **/
function removeGLog(logID){
    removeObjectMarker(logID, LOG);
}

