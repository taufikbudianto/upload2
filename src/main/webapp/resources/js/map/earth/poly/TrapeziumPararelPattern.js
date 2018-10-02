/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *dalam pembuatan awal vertexs tidak diperlukan 
 *dalam pembuatan dari load database startTrapez
 */

function TrapeziumPararelPattern(start, minSearchLeg, width, trackSpacing, heading, maxSearchLeg, vertexs, color){
    this.color = color;
    this.minSearchLeg = minSearchLeg;
    this.maxSearchLeg = maxSearchLeg;
    this.width = width;
    this.heading = heading;
    this.trackSpacing = trackSpacing;
    this.start = start;
    if(this.start){
	this.l = new Array();
		this.l.push(start);
		this.initDirection();
		this.create();
        rotationGLatLngs(this.l, this.start, heading);
		this.initPivot();
	}else{
        this.l = vertexs;
        this.start = this.l[0];
		this.initPivot();
    }
    this.draw();
}

TrapeziumPararelPattern.prototype = new SearchPattern();

TrapeziumPararelPattern.prototype.initPivot = function(){
	var lastPoint = rotationGLatLng(this.start, this.l[this.l.length - 1], (360-this.heading));
	var beforeLastPoint = rotationGLatLng(this.start, this.l[this.l.length - 2], (360-this.heading));
	var sw, ne;
	if(lastPoint.lng() > beforeLastPoint.lng()){
		ne = lastPoint;
		sw = new GLatLng(this.start.lat(), beforeLastPoint.lng());
	}else{
		ne = beforeLastPoint;
		sw = new GLatLng(this.start.lat(), lastPoint.lng());
	}
	var latPivot, lngPivot;
	latPivot = sw.lat() + ((ne.lat() - sw.lat()) / 2);
	lngPivot = sw.lng() + ((ne.lng() - sw.lng()) / 2);
	this.pivot = rotationGLatLng(this.start, new GLatLng(latPivot, lngPivot), this.heading);
}


TrapeziumPararelPattern.prototype.initDirection = function(){
    this.e = true;
    this.w = false;
    this.n = false;
    this.WEST = 0;
    this.EAST = 1; 
}

TrapeziumPararelPattern.prototype.create = function (){
    this.dynamicLeg = this.minSearchLeg;
    var jumlahLooping = Math.floor(this.width / this.trackSpacing);
    var perbedaanJarakPerGaris = (this.maxSearchLeg - this.minSearchLeg) / jumlahLooping;
    for(var i = 1; i <= jumlahLooping; i++){
        this.createNextEastOrWestPoint(perbedaanJarakPerGaris);
        if(i != jumlahLooping){
            this.createNorthPoint();
        }
    }
    var modulWidthAndS = this.width % this.trackSpacing;
    if(modulWidthAndS > (0.5 * this.trackSpacing)){
        this.createResidualNorthPoint(modulWidthAndS);
        this.createResidualEastOrWestPoint();
    }else{
		this.dynamicLeg -= perbedaanJarakPerGaris;
	}
}

TrapeziumPararelPattern.prototype.createNextEastOrWestPoint = function(perbedaanJarakPerGaris){
    var lastPoint, nextPoint;
    lastPoint = this.l[this.l.length - 1];
    if(this.e){
        nextPoint = new GLatLng(lastPoint.lat(), lastPoint.lng() + (ONE_NM_TO_DEG * this.dynamicLeg));
        this.l.push(nextPoint);
        this.dynamicLeg += perbedaanJarakPerGaris;
        this.e = false;
        this.n = true;
        this.NEXT_DIRECTION_FOR_NORTH = this.WEST;
    }else if(this.w){
        nextPoint = new GLatLng(lastPoint.lat(), lastPoint.lng() - (ONE_NM_TO_DEG * this.dynamicLeg));
        this.l.push(nextPoint);
        this.dynamicLeg += perbedaanJarakPerGaris;
        this.w = false;
        this.n = true;
        this.NEXT_DIRECTION_FOR_NORTH = this.EAST;
    }
}

TrapeziumPararelPattern.prototype.createNorthPoint = function(){
    var nextPoint, lastPoint;
    lastPoint = this.l[this.l.length - 1];
    nextPoint = new GLatLng(lastPoint.lat() + (ONE_NM_TO_DEG * this.trackSpacing), lastPoint.lng());
    this.l.push(nextPoint);
    this.n = false;
    if(this.NEXT_DIRECTION_FOR_NORTH == this.EAST){
        this.e = true;
    }else if(this.NEXT_DIRECTION_FOR_NORTH == this.WEST){
        this.w = true;
    }
}

TrapeziumPararelPattern.prototype.createResidualNorthPoint = function(residual){
    var nextPoint, lastPoint;
    lastPoint = this.l[this.l.length - 1];
    nextPoint = new GLatLng(lastPoint.lat() + (ONE_NM_TO_DEG * residual), lastPoint.lng());
    this.l.push(nextPoint);
}

TrapeziumPararelPattern.prototype.createResidualEastOrWestPoint = function(){
    var lastPoint, nextPoint;
    lastPoint = this.l[this.l.length - 1];
    if(this.NEXT_DIRECTION_FOR_NORTH == this.EAST){
        nextPoint = new GLatLng(lastPoint.lat(), lastPoint.lng() + (ONE_NM_TO_DEG * this.dynamicLeg));
        this.l.push(nextPoint);
    }else if(this.NEXT_DIRECTION_FOR_NORTH == this.WEST){
        nextPoint = new GLatLng(lastPoint.lat(), lastPoint.lng() - (ONE_NM_TO_DEG * this.dynamicLeg));
        this.l.push(nextPoint);
    }
}

TrapeziumPararelPattern.prototype.inFocus = function(){
    map.setCenter(this.pivot, 10);
}

TrapeziumPararelPattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.start = this.l[0];
    this.end = this.f[this.f.length]
    this.pivot = to;
    this.remove();
    this.draw();
}

TrapeziumPararelPattern.prototype.getContentString = function(){
    return "<b class='title'><u>Trapezium Search</u></b>" +
    "<br><b class='field'>Large Search Leg</b>      : <font class='value'>" + this.maxSearchLeg +  " NM</font>" +
    "<br><b class='field'>Small Search Leg</b>      : <font class='value'>" + this.minSearchLeg +  " NM</font>" +
    "<br><b class='field'>Heading </b> : <font class='value'>" + this.heading + " °</font>" +
    "<br><b class='field'>Track Spacing </b> : <font class='value'>" + this.trackSpacing + " NM</font>" +
    "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
}
