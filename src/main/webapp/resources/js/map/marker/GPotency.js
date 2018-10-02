/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var POTENCY = new Hash();
function setNewGPotency(){
    POTENCY = new Hash();
}

    
    function GPotency(latlng, potencyID, potencyName, telphone1, telphone2, telphone3, unsurSars, imageName, degreeToIncident, distanceToIncident){
        this.imageName = imageName;
        if(this.imageName){
           this.marker = CustomIconMarker(POTENCY_GIS_SYMBOL_URL + this.imageName , latlng, potencyName + " (Potency)");
        }else{
           this.marker = CustomIconMarker(POTENCY_DEFAULT_SYMBOL_GIS, latlng, potencyName + " (Potency)");
        }
        GEvent.addListener(this.marker, "click", function() {
            var popUpInfo =     "<b class='title'><u>Potency</u></b>" +
	"<br><b class='field'>Latitude</b>      : <font class='value'>" + deg_to_dms(latlng.lat(), LATITUDE_MODE) + "</font>" +
	"<br><b class='field'>Longitude</b>     : <font class='value'>" + deg_to_dms(latlng.lng(), LONGITUDE_MODE) + "</font>" +
        "<br><b class='field'>Nama</b>  : <font class='value'>" + potencyName + "</font>" +
        "<br><b class='field'>Telphone 1</b>    : <font class='value'>" + telphone1 + "</font>" +
        "<br><b class='field'>Telphone 2</b>    : <font class='value'>" + telphone2 + "</font>" +
        "<br><b class='field'>Telphone 3</b>    : <font class='value'>" + telphone3 + "</font>";
        if(degreeToIncident || distanceToIncident){
            popUpInfo += "<br><b class='field'>Arah ke Incident</b>    : <font class='value'>" + degreeToIncident + " °" + "</font>" + 
                         "<br><b class='field'>Jarak Ke Incident</b>    : <font class='value'>" + distanceToIncident.toFixed(2) + " NM"+ "</font>" +
                         "<br><b class='field'>Jarak Ke Incident</b>    : <font class='value'>" + (distanceToIncident * KM_TO_NM_EXT).toFixed(2) + " KM" + "</font>";
        }
        popUpInfo += "<br><ul class='list'><b class='list_title'>Unsur SAR ("+ unsurSars.length +")</b>  : ";
        for(i in unsurSars){
            popUpInfo = popUpInfo + "<li class='list_item'>"+ unsurSars[i].name + "("+ unsurSars[i].kuantitas +")" +"</li>";
        }
        popUpInfo = popUpInfo + "</ul>";
        eBubble.openOnMarker(this, popUpInfo);
        });
        this.addToClusterManager();
    }


    GPotency.prototype = new GObject();
    
    GPotency.prototype.isImageExist = function(){
        var tester= new Image();
        tester.src= POTENCY_GIS_SYMBOL_URL + this.imageName;
        if(tester.complete){
            return true;
        }else{
            return false;
        }
    }
    
    
    function UnsurSar(name, kuantitas){
        this.name = name;
        this.kuantitas = kuantitas;
    }
    
    
    

     /**
     *Menampilkan pada Map semua marker potency yang ditampung pada array POTENCY
     **/
    function showAllGPotency(){
        showAllObjectMarker(POTENCY);
    }
    
	
    /**
    *Remove semua Object Potency yang ada pada Map
    */
	
    function removeAllGPotency(){
    	removeAllObjectMarker(POTENCY);
    }
	
    /**
     *Remove pada map GPotency berdasarkan IDnya 
     **/
    
    function removeGPotency(potencyID){
        removeObjectMarker(potencyID, POTENCY);
    }
    
    

