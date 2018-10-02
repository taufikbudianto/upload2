/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *@param (start) titik CSP(pertama kali SRU melakukan pencarian/oprasi)
 *@param (searchRadius) parameter dalam bentuk satuan NM(nautical mile)
 *@param (heading) satuan dalam bentuk derajat
 *@param (pivot) posisi poros dari search pattern ini. parameter mulai diisi ketika sectorpattern yang sebelumnya sudah ditampilkan lalu di view lagi ke map. untuk inisialisasi marker
 *@param (latlngs) (vertexs) untuk mengambar langsung search pattern, parameter mulai diisi ketika sectorpattern yang sebelumnya sudah ditampilkan lalu di view lagi ke map 
 **/

function SectorPattern(start, searchRadius, heading, pivot, latlngs, color){
    this.color = color;
    this.backUpPoints = new Array();
    this.start = start;  // GLatLng
    this.first = this.start;
    this.radius = searchRadius;
    this.heading = heading;
    this.diameter = searchRadius * 2; //Number(float)
    this.marker = null; //GMarker
    this.rotateState = 0;
    this.iterationCount = 0;
    if(latlngs == null){
        this.init();
        rotationGLatLngs(this.l, this.pivot, heading);
    }else{
        this.pivot = pivot;
        this.l = latlngs;
    }
    this.draw();
}

SectorPattern.prototype = new SearchPattern;
//cross diganti menjadi Cross angle
SectorPattern.prototype.init = function(){
    var staticAngleLat = this.start.lat() + (this.diameter * ONE_NM_TO_DEG); //Number(float)
    var pivotLat = this.start.lat() + ((this.diameter/2) * ONE_NM_TO_DEG); //Number(float)
    this.staticAngle = new GLatLng(staticAngleLat ,this.start.lng()); //GLatLng
    this.pivot = new GLatLng(pivotLat ,this.start.lng()); //GLatLng
    this.dynamicAngle = this.staticAngle //nilai default dari Dinamic angle adalah staticAngle
    this.create();
}

SectorPattern.prototype.create = function(){
    this.l = new Array();
    this.l.push(this.start);
    this.l.push(this.staticAngle);
    this.dynamicAngle = rotationGLatLng(this.pivot, this.dynamicAngle, 60);
    this.l.push(this.dynamicAngle);
    this.dynamicAngle = rotationGLatLng(this.pivot, this.dynamicAngle, 180);
    this.l.push(this.dynamicAngle);
    this.dynamicAngle = rotationGLatLng(this.pivot, this.dynamicAngle, 60);
    this.l.push(this.dynamicAngle);
    this.dynamicAngle = rotationGLatLng(this.pivot, this.dynamicAngle, 180);
    this.l.push(this.dynamicAngle);
    this.dynamicAngle = rotationGLatLng(this.pivot, this.dynamicAngle, 60);
    this.l.push(this.dynamicAngle);
}

SectorPattern.prototype.inFocus = function(){
    map.setCenter(this.pivot, 10);
}

SectorPattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.pivot = to;
    this.start = rotationGLatLng(this.pivot, this.l[0], -this.heading);
    this.remove();
    this.draw();
}

SectorPattern.prototype.getSearchRadius = function(){
    return this.diameter/2;
}


function getSectorStartForGenerateValue(pivot, searchRadius, heading){
    var orginalStart = new GLatLng(pivot.lat() - (searchRadius * ONE_NM_TO_DEG), pivot.lng());
    return rotationLatLng(pivot, orginalStart, heading);
}

SectorPattern.prototype.initWaypointToolTip = function(){
    this.waypointsMarkers = [];
    for(i = 0; i < this.l.length - 1; i++){
        var title = i + 1;
        var waypointMarker = createMarker('', this.l[i], TRANSPARANT_ICON);
        waypointMarker.setDescription(" Waypoint " + title.toString());
        google.earth.addEventListener(waypointMarker, 'mouseover', function(event) {
            this.setName(this.getDescription());
        });
        google.earth.addEventListener(waypointMarker, 'mouseout', function(event) {
            this.setName('');
        });
        measureIntegrationMarker(waypointMarker);
        map.addOverlay(waypointMarker);
        this.waypointsMarkers.push(waypointMarker);
    }
}


SectorPattern.prototype.getContentString = function(){
    return "<b class='title'><u>Sector Search</u></b>" +
    "<br><b class='field'>Search Radius</b>      : <font class='value'>" + this.radius +  " NM</font>" +
    "<br><b class='field'>Heading </b> : <font class='value'>" + this.heading + " °</font>" +
    "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
}