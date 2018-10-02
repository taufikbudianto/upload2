/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var POSSAR = new Hash();
function setNewGPossar(){
    POSSAR = new Hash();
}

    
function GPossar(latlng, possarID, possarName, distanceToIncident, arrowTodegree){
    this.marker = CustomIconMarker(POSSAR_DEFAULT_SYMBOL_GIS, latlng, possarName);
    var me = this;
    google.maps.event.addListener(this.marker, "click", function() {
        var infoPopUp = "<b class='title'><u>Pos SAR</u></b>" +
            "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Pos SAR </b> : <font class='value'>" + possarName + "</font>";
        if(distanceToIncident || arrowTodegree){
            infoPopUp += "<br><b class='field'>Arah ke Incident </b>   : <font class='value'>" + arrowTodegree + " °" + "</font>" +
                         "<br><b class='field'>Jarak ke Incident </b>  : <font class='value'>" + distanceToIncident.toFixed(2) + " NM"  + "</font>" +
                         "<br><b class='field'>Jarak ke Incident </b>  : <font class='value'>" + (distanceToIncident*KM_TO_NM_EXT).toFixed(2) + " KM" + "</font>";
        }
        eBubble.openOnMarker(this, infoPopUp);
    });
    this.addToClusterManager();
}



GPossar.prototype = new GObject();

/**
     *Menampilkan pada Map semua marker pos sar yang ditampung pada array POSSAR
     **/
function showAllGPossar(){
    showAllObjectMarker(POSSAR);
}
    
	
/**
    *Remove semua Object Possar yang ada pada Map
    */
	
function removeAllGPossar(){
    removeAllObjectMarker(POSSAR);
}
	
/**
     *Remove pada map GPossar berdasarkan IDnya 
     **/
function removeGPossar(possarID){
    removeObjectMarker(possarID, POSSAR);
}

