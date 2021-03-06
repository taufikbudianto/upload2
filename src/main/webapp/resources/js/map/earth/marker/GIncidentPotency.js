/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var INC_POTENCY = new Hash();
function setNewGIncidentPotency(){
    INC_POTENCY = new Hash();
}

    
function GIncidentPotency(latlng, incidentPotencyID, potencyName, incidentID, telphone1, telphone2, telphone3, unsurSars, imageName){
    this.setIdentifier(potencyName);
    this.setLatLng(latlng);
    this.imageName = imageName;
    if(this.imageName){
        this.setSymbol(POTENCY_GIS_SYMBOL_URL + imageName);
    }else{
        this.setSymbol(POTENCY_INCIDENT_DEFAULT_SYMBOL_GIS);
    }
    var me = this;
    var popUpInfo =     "<b class='title'><u>Incident Potency</u></b>" +
    "<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
    "<br><b class='field'>Potency Name</b>  : <font class='value'>" + potencyName + "</font>" +
    "<br><b class='field'>Incident ID</b>  : <font class='value'>" + incidentID + "</font>" +
    "<br><b class='field'>Telphone 1</b>    : <font class='value'>" + telphone1 + "</font>" +
    "<br><b class='field'>Telphone 2</b>    : <font class='value'>" + telphone2 + "</font>" +
    "<br><b class='field'>Telphone 3</b>    : <font class='value'>" + telphone3 + "</font>" +
    "<br><ul class='list'><b class='list_title'>Unsur SAR ("+ unsurSars.length +")</b>  : ";
    for(i in unsurSars){
        popUpInfo = popUpInfo + "<li class='list_item'>"+ unsurSars[i].name + "("+ unsurSars[i].kuantitas +")" +"</li>";
    }
    popUpInfo = popUpInfo + "</ul>";
    this.setPopupInfo(popUpInfo);
    ge.getFeatures().appendChild(this.marker);
    this.addClickListener();
}


GIncidentPotency.prototype = new GObject();
    
    
    
function UnsurSar(name, kuantitas){
    this.name = name;
    this.kuantitas = kuantitas;
}
    
    
    

/**
     *Menampilkan pada Map semua marker potency yang ditampung pada array INC_POTENCY
     **/
function showAllGIncidentPotency(){
    showAllObjectMarker(INC_POTENCY);
}
    
	
/**
    *Remove semua Object Potency yang ada pada Map
    */
	
function removeAllGIncidentPotency(){
    removeAllObjectMarker(INC_POTENCY);
}
	
/**
     *Remove pada map GIncidentPotency berdasarkan IDnya 
     **/
    
function removeGIncidentPotency(incidentPotencyID){
    removeObjectMarker(incidentPotencyID, INC_POTENCY);
}
    
    

