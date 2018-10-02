/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var ASSET = new Hash();
function setNewGAsset(){
    ASSET = new Hash();
}

//createGAsset(new GLatLng(5.555833333333333,95.32027777777778),'JKT-1111-2075','Pesud NOMAD P-832,P-841,P-842','0','0','0','0','BAIK','20 ORANG','asset.png','92.405525',5891.5786)

function GAsset( latlng , assetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, kapasitas, imageName, degreeToIncident, distanceToIncident){
    this.setIdentifier(assetName);
    this.setLatLng(latlng);
    this.imageName = imageName;
    this.goodQty = goodQty;
    if(this.imageName){
        this.setSymbol(ASSET_TYPE_GIS_SYMBOL_URL + imageName);
    }else{
        this.setSymbol(ASSET_DEFAULT_SYMBOL_GIS);
    }
    var popUpInfo = "<b class='title'><u>Asset</u></b>"+
    "<br><b class='field'>Nama</b>  : <font class='value'>" + assetName + "</font>" +
    "<br><b class='field'>Jumlah</b>    : <font class='value'>" + this.goodQty + "</font>" +
    "<br><b class='field'>Kondisi</b>   : <font class='value'>" + kondisi + "</font>" +
    "<br><b class='field'>Kapasitas</b>   : <font class='value'>" + kapasitas + "</font>" +
    "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>";
    if(degreeToIncident || distanceToIncident){
        popUpInfo += "<br><b class='field'>Arah ke Incident</b>    : <font class='value'>" + degreeToIncident + " °" + "</font>" +
        "<br><b class='field'>Jarak Ke Incident</b>    : <font class='value'>" + distanceToIncident.toFixed(2) + " NM" + "</font>" +
        "<br><b class='field'>Jarak Ke Incident</b>    : <font class='value'>" + (distanceToIncident * KM_TO_NM_EXT).toFixed(2) + " KM" + "</font>";
    };
    
    this.setPopupInfo(popUpInfo);
    ge.getFeatures().appendChild(this.marker);
    this.addClickListener();
}
	
        
        
GAsset.prototype.setQty = function(newQty){
    this.goodQty = newQty;
}    
        
        
        
GAsset.prototype = new GObject();
        
/**
     *Menampilkan pada Map semua marker Asset yang ditampung pada array ASSET
     **/
function showAllGAsset(){
    showAllObjectMarker(ASSET);
}
	
/**
    *Remove semua Object Asset yang ada pada Map
    */
function removeAllGAsset(){
    removeAllObjectMarker(ASSET);
}
	
	
/**
     *Remove pada map GAsset berdasarkan IDnya 
     **/
function removeGAsset(assetID){
    removeObjectMarker(assetID, ASSET);
}