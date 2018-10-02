/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var KANSAR = new Hash();
function setNewGKansar(){
    KANSAR = new Hash();
}
    
function GKansar(latlng, kansarID, kansarName, alamat, distanceToIncident, arrowTodegree){
    this.marker = CustomIconMarker(KANSAR_DEFAULT_SYMBOL_GIS, latlng, kansarName);
    var me = this;
    GEvent.addListener(this.marker, "click", function() {
        var infoPopUp = "<b class='title'><u>Kantor SAR</u></b>" +
            "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Kantor SAR Name</b> : <font class='value'>" + kansarName;
        if(distanceToIncident || arrowTodegree){
            infoPopUp += "<br><b class='field'>Arah ke Incident </b>   : <font class='value'>" + arrowTodegree + " " + "°" + "</font>" +
                         "<br><b class='field'>Jarak ke Incident </b>  : <font class='value'>" + distanceToIncident.toFixed(2) + " NM" + "</font>" +
                         "<br><b class='field'>Jarak ke Incident </b>  : <font class='value'>" + (distanceToIncident * KM_TO_NM_EXT).toFixed(2) + " KM" + "</font>";
        }
        infoPopUp += "<br><b class='field'>Alamat</b>   : <font class='value'>" + alamat  + "</font>";
        eBubble.openOnMarker(this, infoPopUp);
    });
    this.addToClusterManager();
}

GKansar.prototype = new GObject();

/**
     *Menampilkan pada Map semua marker kansar yang ditampung pada array KANSAR
     **/
function showAllGKansar(){
    showAllObjectMarker(KANSAR);
}
    
	
/**
    *Remove semua Object Kansar yang ada pada Map
    */
	
function removeAllGKansar(){
    removeAllObjectMarker(KANSAR);
}
	
/**
     *Remove pada map GKansar berdasarkan IDnya 
     **/
function removeGKansar(kansarID){
    removeObjectMarker(kansarID, KANSAR);
}

