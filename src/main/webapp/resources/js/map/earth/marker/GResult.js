/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var RESULT = new Hash();
function setNewGResult(){
    RESULT = new Hash();
}

var GRESULT_ICON = [
GRESULT_MARKER_A_ICON,
GRESULT_MARKER_B_ICON,
GRESULT_MARKER_C_ICON,
GRESULT_MARKER_D_ICON,
GRESULT_MARKER_E_ICON,
GRESULT_MARKER_F_ICON,
GRESULT_MARKER_G_ICON,
GRESULT_MARKER_H_ICON,
GRESULT_MARKER_I_ICON,
GRESULT_MARKER_J_ICON
];

function GResult( latlng , resultID , formattedAddress , viewport , pIcon){
        this.setIdentifier(formattedAddress);
        this.setLatLng(latlng);
        this.viewport = viewport;
        this.setSymbol(GRESULT_ICON[resultID]);
        //jika icon tidak kosong
        // TODO give placemark with label in variable LABEL_TEXTS[resultID]
            
        var infoPopUp = "<b class='title'><u>"+ formattedAddress + "</u></b><br>" +
            "<b class='field'>Position  :</b>" + position_ui(latlng.lat(), latlng.lng());
    
        this.setPopupInfo(infoPopUp);
        ge.getFeatures().appendChild(this.marker);
        this.addClickListener();
    }
    
    GResult.prototype = new GObject();
    
    GResult.prototype.show = function(){
        this.marker.show();
    }
    
    GResult.prototype.hide = function(){
        this.marker.hide();
    }
    
    

     /**
     *Menampilkan pada Map semua marker Result yang ditampung pada array RESULT
     **/
    function showAllGResult(){
        showAllObjectMarker(RESULT);
    }
	
    /**
    *Remove semua Object Result yang ada pada Map
    */
    function removeAllGResult(){
    	removeAllObjectMarker(RESULT);
    }
	
	
    /**
     *Remove pada map GResult berdasarkan IDnya 
     **/
    function removeGResult(resultID){
        removeObjectMarker(resultID, RESULT);
    }
    
    GResult.prototype.inFocus = function(){
        var point = this.marker.getGeometry();
        map.setCenter(new GLatLng(point.getLatitude(), point.getLongitude()), 10);
    }