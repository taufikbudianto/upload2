/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *@param (trackSpacing) parameter dalam bentuk satuan NM(nautical mile)
 *@param (searchRadius) parameter dalam bentuk satuan NM(nautical mile)
 *@param (heading) satuan dalam bentuk derajat
 *@param (pivot) untuk inisialisasi posisi marker, khusus untuk search pattern type ini parameter ini didefinisikan diawal, pembuatan SquarePattern bukan ketika SquarePattern yang sebelumnya sudah ditampilkan lalu di view lagi ke map
 *@param (latlngs) (vertexs) untuk mengambar langsung search pattern, parameter mulai diisi ketika squarepattern yang sebelumnya sudah ditampilkan lalu di view lagi ke map 
 **/





function SquarePattern(trackSpacing, radius, pivot, heading, latlngs, color){
    this.color = color;
    this.trackSpacing = trackSpacing;
    this.currentSearchLeg = trackSpacing; //nilai yang akan terus bertambah ketika proess penghitungan menyesuaikan dengan kodisi seach leg terpanjang pada saat itu
    this.heading = heading;
    this.radius = radius; 
    this.pivot = pivot;
    if(latlngs == null){
        this.init();
        rotationGLatLngs(this.l, this.pivot, heading);
    }else{
        this.l = latlngs;
    }
    this.draw();
}

SquarePattern.prototype = new SearchPattern();

SquarePattern.prototype.init = function(){
    this.n = true, this.s = false;
    this.create();
}

SquarePattern.prototype.create = function(){
    var loopCount;
    var tolerancyLeg; 
    var halfLeg = this.radius-(this.trackSpacing/2); // setengah search leg
    if(halfLeg%this.trackSpacing == 0){
        loopCount = (halfLeg*2)/this.trackSpacing;
    }else{
        tolerancyLeg = this.radius-(this.trackSpacing/10);
		var sisaToleransi = tolerancyLeg%this.trackSpacing;
        if(sisaToleransi==0){
            loopCount = (tolerancyLeg*2)/this.trackSpacing;
        }else{
            loopCount = ((tolerancyLeg*2)-sisaToleransi)/this.trackSpacing;
        }
    }
	this.prosesDrawing(loopCount);
}


SquarePattern.prototype.prosesDrawing = function(loopCount){
    this.l = new Array();
    this.l.push(this.pivot);
    var nextPoint;
    var nextLeg;
    for(var i = 1;i<=loopCount;++i){
        nextLeg = i * this.trackSpacing;
        if(this.n){
            nextPoint = this.l[this.l.length-1];
            nextPoint = new GLatLng(nextPoint.lat() + (ONE_NM_TO_DEG * nextLeg), nextPoint.lng());
            this.l.push(nextPoint);
            nextPoint = new GLatLng(nextPoint.lat(), nextPoint.lng() + (ONE_NM_TO_DEG * nextLeg));
            this.l.push(nextPoint);
            this.n = false, this.s = true;
        }else if(this.s){
            nextPoint = this.l[this.l.length-1];
            nextPoint = new GLatLng(nextPoint.lat() - (ONE_NM_TO_DEG * nextLeg), nextPoint.lng());
            this.l.push(nextPoint);
			nextPoint = new GLatLng(nextPoint.lat(), nextPoint.lng() - (ONE_NM_TO_DEG * nextLeg));
            this.l.push(nextPoint);
			this.n = true, this.s = false;
        }
    }
    if(this.s){
        this.l.push(new GLatLng(nextPoint.lat() - (ONE_NM_TO_DEG * nextLeg), nextPoint.lng()));
    }else{
        // jika direction n(NORTH)
        this.l.push(new GLatLng(nextPoint.lat() + (ONE_NM_TO_DEG * nextLeg), nextPoint.lng()));
    }
}



SquarePattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.pivot = this.l[0];
    this.remove();
    this.draw();
}
 
SquarePattern.prototype.inFocus = function(){
    map.setCenter(this.pivot, 10);
}


SquarePattern.prototype.getContentString = function(){
    return "<b class='title'><u>Square Search</u></b>" +
    "<br><b class='field'>Search Radius</b>      : <font class='value'>" + this.radius +  " NM</font>" +
    "<br><b class='field'>Max Leg</b>     : <font class='value'>" + this.radius*2 + " NM</font>" +
    "<br><b class='field'>Heading</b>     : <font class='value'>" + this.heading + " °</font>" +
    "<br><b class='field'>Track Spacing</b> : <font class='value'>" + this.trackSpacing + " NM</font>" +
    "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
}