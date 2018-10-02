/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function FreeDefinePattern(start, latlngs, pivot){
    this.start = start;
    if(latlngs && latlngs.length > 1){
        this.l = latlngs;
        this.pivot = pivot;
        this.draw();
    }else{
        this.l = new Array();
        this.pivot = start;
        this.l.push(start);
        this.initMarkerPivot();
    }
}

FreeDefinePattern.prototype = new SearchPattern();


FreeDefinePattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.start = this.l[0];
    this.pivot = to;
    this.remove();
    this.draw();
}

FreeDefinePattern.prototype.add = function(latlng){
    this.l.push(latlng);
    if(this.l.length > 2){
        this.poly.getGeometry().getCoordinates().pushLatLngAlt(latlng.lat(), latlng.lng(), 700);
    }else{
        this.poly = createPolyline(this.l, "#99ff0000", 5);
        map.addOverlay(this.poly);
    }
    this.length = this.getLength();
    this.repaintMarker();
}


FreeDefinePattern.prototype.repaintMarker = function(){
    var latlng = this.getCenterOfPolyBounds();
    var point = this.marker.getGeometry();
    point.setLatitude(latlng.lat());
    point.setLongitude(latlng.lng());
    this.pivot = latlng;
}

FreeDefinePattern.prototype.inFocus = function(){
    //TODO USE BOUNDS IN CODE getCenterOfPolyBounds
    map.setCenter(this.pivot, 10);
}

FreeDefinePattern.prototype.remove = function(){
    if(this.poly){
        map.removeOverlay(this.poly);
    }
    if(this.marker){
        map.removeOverlay(this.marker);
    }
    if(this.endWaypoint){
        map.removeOverlay(this.endWaypoint);
    }
    if(this.startWaypoint){
        map.removeOverlay(this.startWaypoint);
    }
}

FreeDefinePattern.prototype.draw = function(){
    this.poly = createPolyline(this.l, "#99ff0000", 5);
    map.addOverlay(this.poly);
    if(this.marker){
        var point = this.marker.getGeometry();
        point.setLatitude(latlng.lat());
        point.setLongitude(latlng.lng());
    }else{
        this.initMarkerPivot();	
    }
    this.length = this.getLength();
    this.initWaypointToolTip();
}





FreeDefinePattern.prototype.getCenterOfPolyBounds = function(){
    if(this.l.length == 1) return this.l[0];
    var e = 0, w = 0, n = 0, s = 0;
    var lat, lng;
    var lats = [], lngs = [];
    for(i in this.l){
        lats.push(this.l[i].lat());
        lngs.push(this.l[i].lng());
    }
    for(var j=0;j<this.l.length;j++){
        if(j == 0){
            s = lats[j];
            n = lats[j];
            e = lngs[j];
            w = lngs[j];
        }else{
            if(lats[j] > n) n = lats[j];
            else if(lats[j] < s) s = lats[j];
            if(lngs[j] > e) e = lngs[j];
            else if(lngs[j] < w) w = lngs[j];
        }
    }
    lng = w + ((e - w)/2);
    lat = s + ((n - s)/2);
    return new GLatLng(lat, lng);
}