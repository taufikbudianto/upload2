
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function TSNPattern(start, trackSpacing, searchLeg, heading, pivot, latlngs, color){
    this.color = color;
    this.start = start;
    this.trackSpacing = trackSpacing;
    this.searchLeg = searchLeg;
    this.heading = heading;
    if(!pivot){
        this.pivot = new GLatLng(start.lat() + (searchLeg/2 * ONE_NM_TO_DEG), start.lng());
    }else{
        this.pivot = pivot;
    }
    if(latlngs){
        this.l = latlngs;
        this.draw();
    }else{
        this.init();
        this.fristHeadings();
        this.draw();
    }
}

TSNPattern.prototype = new SearchPattern();

TSNPattern.prototype.init = function(){
    this.l = new Array();
    this.l.push(this.start);
    var point1 = new GLatLng(this.start.lat() + (this.searchLeg * ONE_NM_TO_DEG), this.start.lng());
    var point2 = new GLatLng(point1.lat(), (point1.lng() + (this.trackSpacing * ONE_NM_TO_DEG)));
    var point3 = new GLatLng(this.start.lat(), point2.lng());
    var point4 = new GLatLng(this.start.lat(), (this.start.lng() - (this.trackSpacing * ONE_NM_TO_DEG)));
    var point5 = new GLatLng(point2.lat(), point4.lng());
    this.rotateState = 0;
    this.l.push(point1);
    this.l.push(point2);
    this.l.push(point3);
    this.l.push(point4);
    this.l.push(point5);
}

TSNPattern.prototype.fristHeadings = function(){
    rotationGLatLngs(this.l, this.start, this.heading);
    this.pivot = rotationGLatLng(this.start, this.pivot, this.heading);
}

TSNPattern.prototype.headings = function(){
    rotationGLatLngs(this.l, this.pivot, this.heading);
}

TSNPattern.prototype.inFocus = function(){
    map.setCenter(this.pivot, 10);
}

TSNPattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.start = this.l[0];
    this.end = this.l[this.f.length];
    this.pivot = to;
    this.remove();
    this.draw();
}

TSNPattern.prototype.initWaypointToolTip = function(){
    var i, mouseOverCallback, mouseOutCallback;
	if(this.waypointsMarkers){
        for(i in this.waypointsMarkers){
            this.waypointsMarkers[i].setLatLng(this.l[i]);
        }
    }else{
        this.waypointsMarkers = [];
        for(i = 0; i < this.l.length; i++){
			var title = i + 1;
            var waypointMarker = CustomIconMarker(TRANSPARANT_ICON, this.l[i], title.toString());
            mouseOverCallback = function(latlng, marker){
                SEARCH_PATTERN_TOOLTIP.show(latlng, stringHtmlOnMap("Waypoint " + marker.getTitle()));
            };
            mouseOutCallback = function(latlng, marker){
                SEARCH_PATTERN_TOOLTIP.hide();
            };
            measurementPointIntegration(waypointMarker, mouseOverCallback, mouseOutCallback);
            map.addOverlay(waypointMarker);
            this.waypointsMarkers.push(waypointMarker);
        }
    }
}


TSNPattern.prototype.getContentString = function(){
    return "<b class='title'><u>Track Search No-Return Search</u></b>" +
    "<br><b class='field'>Search Leg</b>      : <font class='value'>" + this.searchLeg +  " NM</font>" +
    "<br><b class='field'>Heading </b> : <font class='value'>" + this.heading + " °</font>" +
    "<br><b class='field'>Track Spacing </b> : <font class='value'>" + this.trackSpacing + " NM</font>" +
    "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
}