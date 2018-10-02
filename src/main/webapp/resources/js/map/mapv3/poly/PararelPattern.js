/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *@param (start) titik CSP
 *@param searchLeg parameter dalam bentuk satuan NM(nautical mile)
 *@param width parameter dalam bentuk satuan NM(nautical mile)
 *@param trackSpacing parameter dalam bentuk satuan NM(nautical mile)
 *@param heading parameter dalam bentuk satuan derajat
 *@param (pivot) posisi poros dari search pattern ini. parameter mulai diisi ketika PararelPattern yang sebelumnya sudah ditampilkan lalu di view lagi ke map. untuk inisialisasi marker
 *@param (latlngs) (vertexs) untuk mengambar langsung search pattern, parameter mulai diisi ketika ;PararelPattern yang sebelumnya sudah ditampilkan lalu di view lagi ke map 
 *
 **/



var EAST = 0, WEST = 1;

function PararelPattern(start, searchLeg, width, trackSpacing, heading, pivot, latlngs, color) {
    this.color = color;
    this.start = start; //GLatLng
    this.searchLeg = searchLeg; //Number
    this.width = width; //Number
    this.trackSpacing = trackSpacing; //Number
    this.heading = heading;
    this.iterationCount = 0;
    this.rotateState = 0;
    if (!latlngs) {
        this.init();
        rotationGLatLngs(this.l, this.pivot, heading);
    } else {
        this.l = latlngs;
        this.pivot = pivot;
    }
    this.draw();
}

PararelPattern.prototype = new SearchPattern();

PararelPattern.prototype.init = function () {
    this.e = true, this.w = false, this.s = false;
    var h = this.width * ONE_NM_TO_DEG, //Number
            w = this.searchLeg * ONE_NM_TO_DEG, //Number
            endLat = this.start.lat() - h, //Number
            endLng = this.start.lng() + w; //Number
    //if(this.l.length == 0){
    this.pivot = new GLatLng((this.start.lat() - h / 2), (this.start.lng() + w / 2)); //GLatLng
    this.end = new GLatLng(endLat, endLng); //GLatLng
    this.create();
}

PararelPattern.prototype.create = function () {
    var i = 0;
    this.pos = this.start;  //nilai this.pos akan dinamic maka start harus direference menjadi pos
    this.l = new Array();
    this.isAllowProsesLastVertex = false;
    this.l.push(this.pos);
    this.LJ = 0; //Number
    this.ARROW = WEST;
    if (this.iterationCount == 0) {
        while (this.width > this.LJ) {
            this.pos = this.generateAngle();
            if (this.pos) {
                this.l.push(this.pos);
                ++this.iterationCount;
            }
        }
        if (this.isAllowProsesLastVertex) {
            this.addLastVertex();
            ++this.iterationCount;
        }
    } else {
        var o;
        for (o = 0; o < this.iterationCount; ++o) {
            this.pos = this.generateAngle();
            this.l.push(this.pos);
        }
    }
}

PararelPattern.prototype.inFocus = function () {
    map.setCenter(this.pivot, 10);
}

PararelPattern.prototype.addLastVertex = function () {
    var nextLng, next;
    if (this.ARROW == EAST) {
        nextLng = this.l[this.l.length - 1].lng() - (ONE_NM_TO_DEG * this.searchLeg);
        next = new GLatLng(this.l[this.l.length - 1].lat(), nextLng);
        this.l.push(next);
    } else if (this.ARROW == WEST) {
        nextLng = this.l[this.l.length - 1].lng() + (ONE_NM_TO_DEG * this.searchLeg);
        next = new GLatLng(this.l[this.l.length - 1].lat(), nextLng);
        this.l.push(next);
    }
}


PararelPattern.prototype.generateAngle = function () {
    var latlng;
    var g = this.pos;
    var southLat = g.lat() - (this.trackSpacing * ONE_NM_TO_DEG);
    if (this.e)
        latlng = new GLatLng(g.lat(), this.end.lng());
    else if (this.s) { // pengechekan apakah kondisi pengambaran pattern di hentikan atau tidak
        if ((this.LJ + this.trackSpacing) > this.width) { // tidak langsung ditambah saat conditional karna jumlah LJ menentukan garis terakhir
            var selisih = this.width - this.LJ;
            if (selisih > 1 / 2 * this.trackSpacing) {
                this.isAllowProsesLastVertex = true;
                southLat = g.lat() - (selisih * ONE_NM_TO_DEG);
                this.LJ += this.trackSpacing; //hanya untuk membuat looping berhenti karna nilai LJ lebih besar
            } else {
                this.isAllowProsesLastVertex = false;
                this.LJ += this.trackSpacing; //hanya untuk membuat looping berhenti karna nilai LJ lebih besar
                return null;
            }
        } else {
            southLat = g.lat() - (this.trackSpacing * ONE_NM_TO_DEG);
            this.LJ += this.trackSpacing;
            this.isAllowProsesLastVertex = true;
        }
        latlng = new GLatLng(southLat, g.lng());
    } else if (this.w)
        latlng = new GLatLng(g.lat(), this.start.lng());
    this.flowDirection();
    return latlng;
}

PararelPattern.prototype.flowDirection = function () {
    if (this.e) {
        this.e = false;
        this.w = false;
        this.s = true;
    } else if (this.s) {
        if (this.ARROW == EAST)
        {
            this.e = true;
            this.w = false;
            this.s = false;
            this.ARROW = WEST;
        } else if (this.ARROW == WEST) {
            this.e = false;
            this.w = true;
            this.s = false;
            this.ARROW = EAST;
        }
    } else if (this.w) {
        this.e = false;
        this.w = false;
        this.s = true;
    }
}

PararelPattern.prototype.move = function (from, to) {
    this.moving(from, to);
    this.start = this.l[0];
    this.end = this.f[this.f.length]
    this.pivot = to;
    this.remove();
    this.draw();
}

function getPararelStartForGenerateValue(heading, trackSpacing, datum, searchLeg, width) {
    var orginalStartLat = (datum.lat() + (width / 2 * ONE_NM_TO_DEG)) - (trackSpacing / 2 * ONE_NM_TO_DEG);
    var orginalStartLng = (datum.lng() - (searchLeg / 2 * ONE_NM_TO_DEG)) + (trackSpacing / 2 * ONE_NM_TO_DEG);
    var orginalStart = new GLatLng(orginalStartLat, orginalStartLng);
    return rotationGLatLng(datum, orginalStart, heading);
}



PararelPattern.prototype.getContentString = function () {
    return "<b class='title'><u>Pararel Search</u></b>" +
            "<br><b class='field'>Search Leg</b>      : <font class='value'>" + this.searchLeg + " NM</font>" +
            "<br><b class='field'>Width </b> : <font class='value'>" + this.width + " NM</font>" +
            "<br><b class='field'>Track Spacing </b> : <font class='value'>" + this.trackSpacing + " NM</font>" +
            "<br><b class='field'>Heading </b> : <font class='value'>" + this.heading + " °</font>" +
            "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
}