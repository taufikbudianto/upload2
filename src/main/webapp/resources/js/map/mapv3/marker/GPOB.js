/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var POB = new Hash();
function setNewGPOB(){
    POB = new Hash();
}

    
function GPOB(latlng, pobID, incidentID, distance, arrow){
    this.marker = CustomIconMarker(POB_DEFAULT_SYMBOL_GIS, latlng, pobID + " (POB)");
    var me = this;
    google.maps.event.addListener(this.marker, "click", function() {
        eBubble.openOnMarker(this , "<b class='title'><u>POB : </u></b>" +
            "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Incident ID</b> : <font class='value'>" + incidentID + "</font>" +
            "<br><b class='field'>Arah ke Incident </b>   : <font class='value'>" + arrow + " °" + "</font>" +
            "<br><b class='field'>Jarak ke Incident </b>  : <font class='value'>" + distance.toFixed(2) +" NM" + "</font>" +
            "<br><b class='field'>Jarak ke Incident </b>  : <font class='value'>" + (distance * KM_TO_NM_EXT).toFixed(2) + " KM" + "</font>");
    });
    this.addToClusterManager();
}

GPOB.prototype = new GObject();

/**
     *Menampilkan pada Map semua marker pob yang ditampung pada array POB
     **/
function showAllGPOB(){
    showAllObjectMarker(POB);
}
    
	
/**
    *Remove semua Object POB yang ada pada Map
    */
	
function removeAllGPOB(){
    removeAllObjectMarker(POB);
}
	
/**
     *Remove pada map GPOB berdasarkan IDnya 
     **/
function removeGPOB(pobID){
    removeObjectMarker(pobID, POB);
}

