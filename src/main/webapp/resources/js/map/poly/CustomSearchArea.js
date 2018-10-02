/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function setNewCustom(){
    CUSTOM_SEARCH_AREA = new Array();
}

function CustomSearchArea(){}

CustomSearchArea.prototype.moving = function(from, to){
    function selisih(awal , hasil){// menghasilkan Object yang menyimpan nilai selisih latlng antara latlng vertex awal dan latlng vertex hasil rotasi
        result = {
            lat : (hasil.lat() - awal.lat()) , 
            lng : (hasil.lng() - awal.lng())
        };
        return result;
    }

    function resultLatlng(oldlatlng, selisih){ // menghasilkan new GLatLng yang akan mengisi properti Array l yang dijadikan untuk membangun GPolyli yang baru
        var lat = oldlatlng.lat() + selisih.lat,
        lng = oldlatlng.lng() + selisih.lng;
        return new GLatLng(lat, lng);
    }
    this.f = this.l;
    this.l = new Array();
    var differ = selisih(from, to);
    for(var i in this.f){
        this.l. push(resultLatlng(this.f[i], differ));
    }
    return differ;
}

CustomSearchArea.prototype.move = function(from, to){}

CustomSearchArea.prototype.draw = function(){
    if(!this.color.startsWith("#")){
        this.synchronizedColor();
    }
    this.poly = createPolyline(this.l, this.color, 5);
    this.length = this.getLength();
    this.initMarkerPivot();
    ge.getFeatures().appendChild(this.poly);
    this.initWaypointToolTip();
}

CustomSearchArea.prototype.getContentStringOfLength = function(){
    var lengthInKm = this.length;
    var lengthInNm = (lengthInKm / 1.85200).toFixed(2);
    return lengthInKm.toFixed(2) + " KM ("+ lengthInNm +" NM)";
}

//inisialisasi marker point tengah dari search pattern
CustomSearchArea.prototype.initMarkerPivot = function(){
    var me = this;
    this.marker = createMarker("", this.pivot);
    google.earth.addEventListener(this.marker, 'click', function(event) {
        var balloon = ge.createHtmlStringBalloon('');
        balloon.setFeature(me.marker);
        balloon.setContentString(me.getContentString());
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
    ge.getFeatures().appendChild(this.marker);
}

CustomSearchArea.prototype.remove = function(){
    if(this.poly){
        ge.getFeatures().removeChild(this.poly);
    }
}

CustomSearchArea.prototype.initWaypointToolTip = function(){
    this.endWaypoint = createMarker('', this.l[this.l.length - 1], TRANSPARANT_ICON);
    google.earth.addEventListener(this.endWaypoint, 'mouseover', function(event) {
        this.setName('Waypoint Terakhir');
    });
    google.earth.addEventListener(this.endWaypoint, 'mouseout', function(event) {
        this.setName('');
    });
    measureIntegrationMarker(this.endWaypoint);
    map.addOverlay(this.endWaypoint);
     
    this.startWaypoint = createMarker('', this.l[0], TRANSPARANT_ICON);
    google.earth.addEventListener(this.startWaypoint, 'mouseover', function(event) {
        this.setName('Waypoint Pertama');
    });
    google.earth.addEventListener(this.startWaypoint, 'mouseout', function(event) {
        this.setName('');
    });
    measureIntegrationMarker(this.startWaypoint);
    map.addOverlay(this.startWaypoint);
}

CustomSearchArea.prototype.synchronizedColor = function(){
    var RR = this.color.substring(2,4);
    var BB = this.color.substring(6,8);
    var GG = this.color.substring(4,6);
    var AA = this.color.substring(0,2);
    this.color = "#" + AA + BB + GG + RR;
}

CustomSearchArea.prototype.getLength = function(){
    var result = 0;
    for(var i=0;i<this.l.length;i++){
        var j = i + 1;
        if(this.l.length == j) return result;
        result += harversine(this.l[i].lat(), this.l[i].lng(), this.l[j].lat(), this.l[j].lng());
    }
    return result;
}

function setCurentCustomSearchArea(value){
    currentCustomArea = value;
}

function removeCustomSearchArea(id){
    for(i in CUSTOM_SEARCH_AREA[id].waypointsMarkers){
        map.removeOverlay(CUSTOM_SEARCH_AREA[id].waypointsMarkers[i]);
    }
    if(CUSTOM_SEARCH_AREA[id].endWaypoint){
        map.removeOverlay(CUSTOM_SEARCH_AREA[id].endWaypoint);
    }
    if(CUSTOM_SEARCH_AREA[id].startWaypoint){
        map.removeOverlay(CUSTOM_SEARCH_AREA[id].startWaypoint);
    }
    map.removeOverlay(CUSTOM_SEARCH_AREA[id].poly);
    map.removeOverlay(CUSTOM_SEARCH_AREA[id].marker);
    CUSTOM_SEARCH_AREA[id] = null;
}

function createCustomArea (start, id, latlngs, pivot){
    CUSTOM_SEARCH_AREA[id] = new CustomSearchArea(start, latlngs, pivot);
    currentCustomArea = CUSTOM_SEARCH_AREA[id];
}