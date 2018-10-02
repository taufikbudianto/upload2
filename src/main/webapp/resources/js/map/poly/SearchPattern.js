/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 *dipangil ketika clear semua Search Pattern
 **/
var SEARCH_PATTERN_TOOLTIP;

function setNewPatterns(){
    SEARCH_PATTERN = new Array();
}



function SearchPattern(){}

/**
 *@param from GLatLng marker sebelum di pindah
 *@param to GLatLng marker sesudah di pindah
 *
 *konsepnya menemukan selisih antara latlng marker awal dan
 * dan latlng marker setelah dipindah. lalu dengan oprasi Looping melakukan mengisian pada properti Array l
 *dengan nilai latlng vertex yang ditambah dengan object selisih
 *
 **/
SearchPattern.prototype.moving = function(from, to){
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
}
//OVERIDE METHOD INI DAN ISIKAN CODE YANG DIPERLUKAN SAAT SEARCHPATTERN DI MOVE
SearchPattern.prototype.move = function(from, to){}

SearchPattern.prototype.draw = function(){
    if(!this.color.startsWith("#")){
        this.color = "#" + this.color.substring(2,8);
    }
    this.poly = new GPolyline(this.l, this.color, 4, 0.5,{
        clickable : false
    });
    map.addOverlay(this.poly);
    if(this.marker == null){
        this.marker = new GMarker(this.pivot, {
            draggable : true, 
            dragCrossMove : true
        });
        map.addOverlay(this.marker);
        var me = this;
        GEvent.addListener(this.marker, "dragend", function(latlng){
            me.move(me.pivot,latlng);
        });
        GEvent.addListener(this.marker, "click", function(latlng){
            eBubble.openOnMarker(this, me.getContentString());
        });
    }else{
        this.marker.setLatLng(this.pivot);
    }
    this.initWaypointToolTip();
}


SearchPattern.prototype.getContentStringOfLength = function(){
    var length = this.poly.getLength() / 1000;
    var panjangInKM = new String(length.toFixed(2) + " KM");
    var panjangInNM = new String((length / 1.85200).toFixed(2) + " NM");
    return panjangInKM + " ("+ panjangInNM +")";
}

SearchPattern.prototype.initWaypointToolTip = function(){
    var mouseOverCallback, mouseOutCallback; 
    if(this.endWaypoint){
        this.endWaypoint.setLatLng(this.l[this.l.length - 1]);
    }else{
        this.endWaypoint = CustomIconMarker(TRANSPARANT_ICON, this.l[this.l.length - 1], '');
        mouseOverCallback = function(latlng){
            SEARCH_PATTERN_TOOLTIP.show(latlng, stringHtmlOnMap("Waypoint akhir"));
        };
        mouseOutCallback = function(latlng){
            SEARCH_PATTERN_TOOLTIP.hide();
        };
        measurementPointIntegration(this.endWaypoint, mouseOverCallback, mouseOutCallback);
        map.addOverlay(this.endWaypoint);
    }
     
    if(this.startWaypoint){
        this.startWaypoint.setLatLng(this.l[0]);
    }else{
        this.startWaypoint = CustomIconMarker(TRANSPARANT_ICON, this.l[0], '');
        mouseOverCallback = function(latlng){
            SEARCH_PATTERN_TOOLTIP.show(latlng, stringHtmlOnMap("Waypoint pertama"));
        };
        mouseOutCallback = function(latlng){
            SEARCH_PATTERN_TOOLTIP.hide();
        };
        measurementPointIntegration(this.startWaypoint, mouseOverCallback, mouseOutCallback);
        map.addOverlay(this.startWaypoint);
    }
}


SearchPattern.prototype.remove = function(){
    if(this.poly){
        map.removeOverlay(this.poly);
    }
}




var current_SearchPattern;
function setCurrentSearchPattern(value){
    current_SearchPattern = value;
}


function setCurrentFreeDefine(value){
    currentFreeDefine = value;
}

/**
 *Meremove Overlay dan mengset nullkan Search Pattern berdasarkan id dan type
 **/
function removeSearchPattern(id){
    for(i in SEARCH_PATTERN[id].waypointsMarkers){
        map.removeOverlay(SEARCH_PATTERN[id].waypointsMarkers[i]);
    }
    if(SEARCH_PATTERN[id].endWaypoint){
        map.removeOverlay(SEARCH_PATTERN[id].endWaypoint);
    }
    if(SEARCH_PATTERN[id].startWaypoint){
        map.removeOverlay(SEARCH_PATTERN[id].startWaypoint);
    }
    map.removeOverlay(SEARCH_PATTERN[id].poly);
    map.removeOverlay(SEARCH_PATTERN[id].marker);
    SEARCH_PATTERN[id] = null;
}

function createSector(id, start, searchRadius, heading, pivot, latlngs, color){
    SEARCH_PATTERN[id] = new SectorPattern(start, searchRadius, heading, pivot, latlngs, color);
}

function createSquare(id, trackSpacing, searchRadius , start, heading, latlngs, color){
    SEARCH_PATTERN[id] = new SquarePattern(trackSpacing, searchRadius, start, heading, latlngs, color);
}

function createPararel(id, start, searchLeg, width, trackSpacing, heading, pivot, latlngs, color){
    SEARCH_PATTERN[id] = new PararelPattern(start, searchLeg, width, trackSpacing, heading, pivot, latlngs, color);
}

function createFreeDefine(start, id, latlngs, pivot){
    SEARCH_PATTERN[id] = new FreeDefinePattern(start, latlngs, pivot);
    currentFreeDefine = SEARCH_PATTERN[id];
}

function createTSN(id, start, trackSpacing, searchRadius, heading, pivot, vertexs, color){
    SEARCH_PATTERN[id] = new TSNPattern(start, trackSpacing, searchRadius, heading, pivot, vertexs, color);
}

function createTSR(id, start, trackSpacing, searchRadius, heading, pivot, vertexs, color){
    SEARCH_PATTERN[id] = new TSRPattern(start, trackSpacing, searchRadius, heading, pivot, vertexs, color);
}

function createTrapeziumPararelPattern(id, name, startPosition, minSearchLeg, width, trackSpacing, heading, maxSearchLeg, vertexs, color){
    SEARCH_PATTERN[id] = new TrapeziumPararelPattern(startPosition, minSearchLeg, width, trackSpacing, heading, maxSearchLeg, vertexs, color);
}