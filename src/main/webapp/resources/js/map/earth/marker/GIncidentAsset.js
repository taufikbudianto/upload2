/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var INC_ASSET = new Hash();
function setNewGIncidentAsset(){
    INC_ASSET = new Hash();
}



function GIncidentAsset( latlng , incidentAssetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, imageName){
    this.setIdentifier(assetName);
    this.setLatLng(latlng);
    this.imageName = imageName;
    if(this.imageName){
        this.setSymbol(ASSET_TYPE_GIS_SYMBOL_URL + imageName);
    }else{
        this.setSymbol(ASSET_INCIDENT_DEFAULT_SYMBOL_GIS);
    }
    var popUpInfo = "<b class='title'><u>Incident Asset</u></b>"+
    "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
    "<br><b class='field'>Nama</b>  : <font class='value'>" + assetName + "</font>" +
    "<br><b class='field'>Rusak Ringan</b>   : <font class='value'>" + otherOty + "</font>" +
    "<br><b class='field'>Baik</b>    : <font class='value'>" + goodQty + "</font>" +
    "<br><b class='field'>Rusak Berat</b>  : <font class='value'>" + rejectQty + "</font>" +
    "<br><b class='field'>Dalam Perbaikan</b> : <font class='value'>" + serviceQty + "</font>" +
    "<br><b class='field'>Kondisi</b>   : <font class='value'>" + kondisi + "</font>"; 
    
    this.setPopupInfo(popUpInfo);
    ge.getFeatures().appendChild(this.marker);
    this.addClickListener();
}
	
        
        
GIncidentAsset.prototype = new GObject();
        
/**
     *Menampilkan pada Map semua marker Asset yang ditampung pada array INC_ASSET
     **/
function showAllGIncidentAsset(){
    showAllObjectMarker(INC_ASSET);
}
	
/**
    *Remove semua Object Asset yang ada pada Map
    */
function removeAllGIncidentAsset(){
    removeAllObjectMarker(INC_ASSET);
}
	
	
/**
     *Remove pada map GIncidentAsset berdasarkan IDnya 
     **/
function removeGIncidentAsset(incidentAssetID){
    removeObjectMarker(incidentAssetID, INC_ASSET);
}