/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function GObject(){
    
}

GObject.prototype.setLatLng = function(latlng){
    // Set the placemark's location.  
    var point = ge.createPoint('');
    point.setLatitude(latlng.lat());
    point.setLongitude(latlng.lng());
    this.marker.setGeometry(point);
}





GObject.prototype.setIdentifier = function(identifier){
    // Set the placemark's identifier
    this.marker = ge.createPlacemark('');
    this.marker.setName(identifier);
    markerAdditionalEventForMeasure(this.marker);
}

GObject.prototype.setSymbol = function(url){
    // Define a custom icon.
    var icon = ge.createIcon('');
    icon.setHref(url);
    var style = ge.createStyle(''); //create a new style
    style.getIconStyle().setIcon(icon); //apply the icon to the style
    this.marker.setStyleSelector(style); //apply the style to the placemark
}


GObject.prototype.setPopupInfo = function(htmlString){
    this.htmlString = htmlString;
}

GObject.prototype.addClickListener = function(){
    var me = this;
    google.earth.addEventListener(this.marker, 'click', function(event) {
        var balloon = ge.createHtmlStringBalloon('');
        balloon.setFeature(me.marker);
        balloon.setContentString(me.htmlString);
        event.preventDefault();
        measureIntegrationPOI(balloon, me.marker);
        ge.setBalloon(balloon);
    });
    google.earth.addEventListener(this.marker, 'mouseout', function(event) {
        isMeasureEditSession = false;
    });
    google.earth.addEventListener(this.marker, 'mouseover', function(event) {
        isMeasureEditSession = true;
    });
}


GObject.prototype.show = function(){
    this.marker.setVisibility(true);
}

GObject.prototype.hide = function(){
    this.marker.setVisibility(false);
} 






/**
     *Remove pada map berdasarkan IDnya 
     **/
function removeObjectMarker(keyInHash, hash){
    ge.getFeatures().removeChild(hash.getItem(keyInHash).marker);
    hash.getItem(keyInHash).marker.release();
    hash.removeItem(keyInHash);
}





function removeAllObjectMarker(hash){
    for(i in hash.items){
        if(hash.items[i] != null){
            ge.getFeatures().removeChild(hash.items[i].marker);
            hash.items[i].marker.release();
        }
    }
    hash.clear();
}