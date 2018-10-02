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
        //jika icon tidak kosong
        this.viewport = viewport;
        this.marker = CustomIconMarker(GRESULT_ICON[resultID], latlng, formattedAddress);
        var me = this;
        GEvent.addListener(this.marker, "click", function() {
            var infoPopUp = "<b class='title'><u>"+ formattedAddress + "</u></b><br>" +
            "<b class='field'>Position  :</b>" + position_ui(latlng.lat(), latlng.lng());        
            eBubble.openOnMarker(this, infoPopUp);
        });
        map.addOverlay(this.marker);
    }
    
    GResult.prototype.show = function(){
        this.marker.show();
    }
    
    GResult.prototype.hide = function(){
        this.marker.hide();
    }
    
    
    GResult.prototype.remove = function(){
        map.removeOverlay(this.marker);
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
    	for(i in RESULT.items){
            if(RESULT.items[i] != null){
                map.removeOverlay(RESULT.items[i].marker);
            }
        }
        RESULT.clear();
    }
	
	
    /**
     *Remove pada map GResult berdasarkan IDnya 
     **/
    function removeGResult(resultID){
        map.removeOverlay(RESULT.getItem(resultID).marker);
        RESULT.removeItem(resultID);
    }
    
    GResult.prototype.inFocus = function(){
        var fitZoomViewport;
        if(this.viewport){
            fitZoomViewport = map.getBoundsZoomLevel(this.viewport);
        }else{
            fitZoomViewport = 8;
        }
        map.setCenter(this.marker.getLatLng(), fitZoomViewport);
    }