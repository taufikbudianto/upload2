/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function MapObject(){
}


MapObject.prototype.addOverlay = function(overlay){
    ge.getFeatures().appendChild(overlay);
}

MapObject.prototype.removeOverlay = function(overlay){
    ge.getFeatures().removeChild(overlay);
}

MapObject.prototype.clearOverlays = function(){
    var overlays = ge.getFeatures().getChildNodes();
    for(i in overlays){
        ge.getFeatures().removeChild(overlays[i]);
    }
}

MapObject.prototype.setCenter =  function(latlng, zoomLevel){
    var topLevel, range;
    if(zoomLevel){
        topLevel = 40000000;
        range = 40000000;
        for(i=1;i<=20;i++){
            if(i == zoomLevel) break;
            range = range / 2;
        }
    }else{
        range = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND).getRange();
    }
    var lookAt = ge.createLookAt('');
    lookAt.set(latlng.lat(), latlng.lng(), 0, ge.ALTITUDE_RELATIVE_TO_GROUND, 0, 0, range);
    ge.getView().setAbstractView(lookAt);
}

MapObject.prototype.getCenter =  function(){
    var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
    return new GLatLng(lookAt.getLatitude(), lookAt.getLongitude());
}


MapObject.prototype.getZoom =  function(){
    var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
    var currentRange = lookAt.getRange();
    var range = 40000000;
    var oldRange;
    for(i=1;i<=20;i++){
        oldRange = range;
        range = range / 2;
        if(currentRange <= oldRange && currentRange >= range) return i;
    }
    if(currentRange > 40000000){
        return 1;
    }else{
        return 20;
    }
}

