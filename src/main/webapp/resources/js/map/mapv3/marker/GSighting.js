/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var SIGHTING = new Hash();
function setNewGSighting(){
    SIGHTING = new Hash();
}

    
    function GSighting(latlng, sightingID, noSighting, phone, name, location, reportType, objectType,kansar, incidentNumber){
        this.objectType = objectType;
        var infoPopup = "<b class='title'><u>Sighting</u></b>" +    
                                "<br><b class='field'>No Sighting</b>     : <font class='value'>" + noSighting+ "</font>" +
                                "<br><b class='field'>Nama</b>     : <font class='value'>" + name+  "</font>" +
				"<br><b class='field'>Phone</b>     : <font class='value'>" + phone+  "</font>" + 
				"<br><b class='field'>Lokasi </b>     : <font class='value'>" + location+  "</font>" +
				"<br><b class='field'>Kantor SAR</b>     : <font class='value'>" + kansar + "</font>";
        if(incidentNumber){           
           this.marker = CustomIconMarker(this.getRelateIncidentSymbolURL(), latlng, noSighting + " (Sighting)");
           infoPopup += "<br><b class='field'>No Incident</b> : <font class='value'>" + incidentNumber + "</font>";
        }else{
           this.marker = CustomIconMarker(SIGHTING_DEFAULT_SYMBOL_GIS, latlng, noSighting + " (Sighting)");
        }
        infoPopup += "<br><b class='field'>Remarks </b>   : <font class='value'>" + reportType + ": "+ objectType + "</font>" +
                     "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
                     "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>";
        google.maps.event.addListener(this.marker, "click", function() {
            eBubble.openOnMarker(this, infoPopup);
        });
        this.addToClusterManager();
    }

GSighting.prototype = new GObject();


GSighting.prototype.getRelateIncidentSymbolURL = function(){
    if(this.objectType == "Kapal"){
        return SIGHTING_VESSEL_DEFAULT_SYMBOL_GIS;
    }else if(this.objectType == "Orang"){
        return SIGHTING_HUMAN_DEFAULT_SYMBOL_GIS;
    }else if(this.objectType == "Polusi"){
        return SIGHTING_POLLUTION_SYMBOL_GIS;
    }else if(this.objectType == "Pesawat"){
        return SIGHTING_AIRCRAFT_SYMBOL_GIS;
    }else if(this.objectType == "Sinyal"){
        return SIGHTING_SIGNAL_SYMBOL_GIS;
    }else if(this.objectType == "Kendaraan"){
        return SIGHTING_CAR_SYMBOL_GIS;
    }else {
        return INCIDENT_DEFAULT_SYMBOL_GIS;
    }
}


     /**
     *Menampilkan pada Map semua marker sighting yang ditampung pada array SIGHTING
     **/
    function showAllGSighting(){
        showAllObjectMarker(SIGHTING);
    }
    
	
    /**
    *Remove semua Object Sighting yang ada pada Map
    */
	
    function removeAllGSighting(){
    	removeAllObjectMarker(SIGHTING);
    }
	
    /**
     *Remove pada map GSighting berdasarkan IDnya 
     **/
    function removeGSighting(sightingID){
        removeObjectMarker(sightingID, SIGHTING);
    }

