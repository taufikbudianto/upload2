/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//createTSN(1,new GLatLng(1.5200548106444896,103.568115234375),1,5,45,new GLatLng(1.412973212770802,103.79470825195312),null)
function TSRPattern(start, trackSpacing, searchLeg, heading, pivot, latlngs, color){
    this.color = color;
    this.start = start;
    this.trackSpacing = trackSpacing;
    this.searchLeg = searchLeg;
    this.heading = heading;
    if(!pivot){
        this.pivot = new GLatLng((start.lat() + ((searchLeg/2)*ONE_NM_TO_DEG)), (start.lng() + ((trackSpacing/2)*ONE_NM_TO_DEG)));
    }else{
        this.pivot = pivot;
    }
    if(latlngs){
        this.l = latlngs;
        this.draw();
    }else{
        this.init();
        this.firstHeadings();
        this.draw();
    }
}

TSRPattern.prototype = new SearchPattern();

TSRPattern.prototype.firstHeadings = function(){
    var firstHeadingPivot = new GLatLng(this.start.lat(), (this.start.lng() + (this.trackSpacing / 2 * ONE_NM_TO_DEG)));  
    rotationGLatLngs(this.l, firstHeadingPivot, this.heading);
    this.pivot = rotationGLatLng(firstHeadingPivot, this.pivot, this.heading);
}

TSRPattern.prototype.init = function(){
    this.l = new Array();
    this.l.push(this.start);
    var point1 = new GLatLng(this.start.lat() + (this.searchLeg * ONE_NM_TO_DEG), this.start.lng());
    var point2 = new GLatLng(point1.lat(), point1.lng() + (this.trackSpacing * ONE_NM_TO_DEG));
    var point3 = new GLatLng(this.start.lat(), point2.lng())
    this.rotateState = 0;
    this.l.push(point1);
    this.l.push(point2);
    this.l.push(point3);
}

TSRPattern.prototype.headings = function(){
    rotationGLatLngs(this.l, this.pivot, this.heading);
}

TSRPattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.start = this.l[0];
    this.end = this.l[this.l.length];
    this.pivot = to;
    this.remove();
    this.draw();
}

TSRPattern.prototype.getHeading = function(){
    return this.heading;
}

TSRPattern.prototype.getTrackSpacing = function(){
    return this.trackSpacing;    
}

TSRPattern.prototype.getSearchLeg = function(){
    return this.searchLeg;
}

TSRPattern.prototype.inFocus = function(){
    map.setCenter(this.pivot, 10);
}


TSRPattern.prototype.getPivot = function(){
    return this.pivot;
}

TSNPattern.prototype.initWaypointToolTip = function(){
    this.waypointsMarkers = [];
    var i;
    for(i = 0; i < this.l.length; i++){
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

function getTSRStartForGenerateValue(heading, trackSpacing, lkp){
    var orginalStart = new GLatLng(lkp.lat(), (lkp.lng() - (trackSpacing/2 * ONE_NM_TO_DEG)));
    return rotationLatLng(lkp, orginalStart, heading);
}

TSRPattern.prototype.getContentString = function(){
    return "<b class='title'><u>Track Search Return Search</u></b>" +
    "<br><b class='field'>Search Leg</b>      : <font class='value'>" + this.searchLeg +  " NM</font>" +
    "<br><b class='field'>Heading </b> : <font class='value'>" + this.heading + " °</font>" +
    "<br><b class='field'>Track Spacing </b> : <font class='value'>" + this.trackSpacing + " NM</font>" +
    "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
}