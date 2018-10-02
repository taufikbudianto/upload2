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
        this.setIdentifier(noSighting);
        this.setLatLng(latlng);
        var infoPopup = "<b class='title'><u>Sighting</u></b>" +    
                                "<br><b class='field'>No Sighting</b>     : <font class='value'>" + noSighting + "</font>" +
                                "<br><b class='field'>Nama</b>     : <font class='value'>" + name + "</font>" +
				"<br><b class='field'>Phone</b>     : <font class='value'>" + phone + "</font>" + 
				"<br><b class='field'>Lokasi </b>     : <font class='value'>" + location + "</font>" +
				"<br><b class='field'>Kantor SAR</b>     : <font class='value'>" + kansar + "</font>";
        if(incidentNumber){
           this.setSymbol(getRelateIncidentSymbolURL(objectType));
           infoPopup += "<br><b class='field'>No Incident</b> : <font class='value'>" + incidentNumber+ "</font>";
        }else{
           this.setSymbol(SIGHTING_DEFAULT_SYMBOL_GIS);
        }
        infoPopup += "<br><b class='field'>Remarks </b>   : " + reportType + " " + objectType + "</font>" +
                     "<br><b class='field'>Latitude</b>      : " + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
                     "<br><b class='field'>Longitude</b>     : " + deg_to_dms(latlng.lng(), LONGITUDE_MODE + "</font>");
        this.setPopupInfo(infoPopup);
        ge.getFeatures().appendChild(this.marker);
        this.addClickListener();
    }

GSighting.prototype = new GObject();



function getRelateIncidentSymbolURL(objectType){
    if(objectType == "Kapal"){
        return SIGHTING_VESSEL_DEFAULT_SYMBOL_GIS;
    }else if(objectType == "Orang"){
        return SIGHTING_HUMAN_DEFAULT_SYMBOL_GIS;
    }else if(objectType == "Polusi"){
        return SIGHTING_POLLUTION_SYMBOL_GIS;
    }else if(objectType == "Pesawat"){
        return SIGHTING_AIRCRAFT_SYMBOL_GIS;
    }else if(objectType == "Sinyal"){
        return SIGHTING_SIGNAL_SYMBOL_GIS;
    }else if(objectType == "Kendaraan"){
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

