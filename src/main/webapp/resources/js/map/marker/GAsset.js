/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var ASSET = new Hash();
function setNewGAsset(){
    ASSET = new Hash();
}


function GAsset( latlng , assetID , assetName, goodQty, rejectQty, serviceQty, otherOty, kondisi, kapasitas, imageName, degreeToIncident, distanceToIncident){
        this.imageName = imageName;
        this.goodQty = goodQty;
        if(this.imageName){
            this.marker = CustomIconMarker(ASSET_TYPE_GIS_SYMBOL_URL + imageName, latlng, assetName + " (Asset)");
        }else{
            this.marker = CustomIconMarker(ASSET_DEFAULT_SYMBOL_GIS, latlng, assetName + " (ASSET)");
        }
        var me = this;
        GEvent.addListener(this.marker, "click", function() {
            var popUpInfo = "<b class='title'><u>Asset</u></b>"+
        "<br><b class='field'>Nama</b>  : <font class='value'>" + assetName + "</font>" +
        "<br><b class='field'>Jumlah</b>    : <font class='value'>" + me.goodQty + "</font>" +
        "<br><b class='field'>Kondisi</b>   : <font class='value'>" + kondisi + "</font>" +
        "<br><b class='field'>Kapasitas</b>   : <font class='value'>" + kapasitas + "</font>" +
	"<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
        "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>";
        if(degreeToIncident || distanceToIncident){
            popUpInfo  += "<br><b class='field'>Arah ke Incident</b>    : <font class='value'>" + degreeToIncident + " °" + "</font>" +
                         "<br><b class='field'>Jarak Ke Incident</b>    : <font class='value'>" + distanceToIncident.toFixed(2) + " NM" + "</font>" +
                         "<br><b class='field'>Jarak Ke Incident</b>    : <font class='value'>" + (distanceToIncident * KM_TO_NM_EXT).toFixed(2) + " KM" + "</font>";
        }
             eBubble.openOnMarker(this, popUpInfo);
        });
        this.addToClusterManager();
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