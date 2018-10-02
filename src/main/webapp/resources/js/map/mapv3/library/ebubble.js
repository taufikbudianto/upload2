// EBubble.js 
//
//   This Javascript is provided by Mike Williams
//   Community Church Javascript Team
//   http://www.bisphamchurch.org.uk/   
//   http://econym.org.uk/gmap/
//
//   This work is licenced under a Creative Commons Licence
//   http://creativecommons.org/licenses/by/2.0/uk/
//
// Version 0.0  13/07/2007 Initial version
// Version 0.1  14/07/2007 Bugfix: Was failing to apply the position offset.
// Version 0.2  30/07/2007 Bugfix: Wasn't clearingout the old contents.
// Version 0.3  21/09/2007 Added noCloseOnClick parameter
// version 0.4  25/12/2007 Bugfix: Problem with offset
// version 0.5  13/01/2009 Bugfix: Problem with offset (thanks to Joerg Wagner)

function EBubble() {
    this.visible = false;
    this.infowindow = new  google.maps.InfoWindow();
} 
      

EBubble.prototype.openOnMap = function(html, latlng, point, offset) {
    this.infowindow.setContent(html);
    this.infowindow.setPosition(latlng);
    this.infowindow.open(map);
    this.visible = true;
}


EBubble.prototype.setContent = function(html) {
    this.infowindow.setContent(html);
}

      
EBubble.prototype.openOnMarker = function(marker, html) { 
    if((typeof html === 'string') && !html.startsWith("<div")){
        html = "<div style='height:250px' class='scroll-pane'>" + html;
    }
    var a = '';
    if(MODE == 'MEASURE'){
        if(polylineMeasure){
            a = getMeasurementScript(2, marker.getPosition(), 2);
        }else{
            a = getMeasurementScript(1, marker.getPosition(), 2);
        }
        html += a;
    }else if(MODE == 'FREE_DEFINE_PATTERN'){
        if(currentFreeDefine){
            a = getMeasurementScript(2, marker.getPosition(), 2);
        }else{
            a = getMeasurementScript(1, marker.getPosition(), 2);
        }
        html += a;
    }
    html += "</div>";
    this.infowindow.setContent(html);
    this.infowindow.open(map, marker);
    this.visible = true;
}
      

EBubble.prototype.show = function() {
    this.infowindow.open(map);
    this.visible = true;
}
      
EBubble.prototype.hide = function() {
    this.infowindow.close();
    this.visible = false;
}        