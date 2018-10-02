/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var INCIDENT = new Hash();
function setNewGIncident(){
    INCIDENT = new Hash();
}


function GIncident(latlng, incidentNumber, incidentName, location, type){
    var image;
    if(type == 1){
        image = INCIDENT_KECELAKAAN_LAUT;
    }else if(type == 2){
        image = INCIDENT_KECELAKAAN_UDARA;
    }else if(type == 3){
        image = INCIDENT_MUSIBAH;
    }else if(type == 4){
        image = INCIDENT_BENCANA;
    }
    this.marker = CustomIconMarker(image, latlng, incidentNumber + " (Incident)");
    var me = this;
    GEvent.addListener(this.marker, "click", function() {
         eBubble.openOnMarker(this, "<b class='title'><u>Incident</u></b>" +
            "<br><b class='field'>Latitude</b>     : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>    : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>Name</b> : <font class='value'>" + incidentName + "</font>" +
            "<br><b class='field'>Lokasi</b> : <font class='value'>" + location + "</font>");
    });
    this.addToClusterManager();
}
    
GIncident.prototype = new GObject();
	
/**
     *Menampilkan pada Map semua marker incident yang ditampung pada array INCIDENT
     **/
function showAllGIncident(){
    showAllObjectMarker(INCIDENT);
}

    
/**
     *Remove pada map GIncidentMarker berdasarkan IDnya 
     **/
function removeGIncident(number){
    removeObjectMarker(number, INCIDENT);
}
