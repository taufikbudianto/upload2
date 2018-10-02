
var SEARCH_AREA = new Array();

function setNewSearchArea(){
    SEARCH_AREA = new Array();
}

var current_SearchArea;


// BUAT DARI writeScript methode
function createSearchArea(start, id, latlngs, parrentID, shape, radius){
    SEARCH_AREA[id] = new SearchArea(start, latlngs, null, null, parrentID, shape, radius);
}

//REMOVE DARI removeScript methode
function removeSearchArea(id){
    SEARCH_AREA[id].remove();
    SEARCH_AREA[id] = null
}


function clearSearchArea(){
    for(i in SEARCH_AREA){
        SEARCH_AREA[i].remove();
    }
    SEARCH_AREA = [];
}

function SearchArea(start, latlngs, constructData, polygon, parrentID, shape, radius){
    this.start = start;
    this.parrentID = parrentID;
    this.polygon = null;
    this.shape = shape;
    this.radius = radius;
    if(constructData){
        this.pivot = constructData.pivot;
        /*
         *tiban nilai radius jika berasal dari construct data
         */
        this.radius = constructData.radius;
        this.tilt = constructData.tilt;
        this.drawByConstructData();
        this.install();
    } else if(!latlngs){
        this.poly = createPolyline([start]);
        map.addOverlay(this.poly);
        this.l = new Array();
        this.l.push(this.start);
    } else{
        this.l = latlngs;
        if(polygon){
            /*
             *Search Area work flow code below will be taken if state search area is create new of trapezium
             */
            this.polygon = polygon;
        /*
            *uncomment code dibawah jika ingin menampilkan urutan trapezium search area
            *this.initTooltipMarker();
            */
        }else{
            this.install();
        }
    }
}

SearchArea.prototype.drawByConstructData = function(){
    this.l = new Array();
    var radToDec = this.radius * ONE_NM_TO_DEG;
    this.l.push(new GLatLng((this.pivot.lat() + radToDec), (this.pivot.lng() - radToDec)));
    this.l.push(new GLatLng((this.pivot.lat() + radToDec), (this.pivot.lng() + radToDec)));
    this.l.push(new GLatLng((this.pivot.lat() - radToDec), (this.pivot.lng() + radToDec)));
    this.l.push(new GLatLng((this.pivot.lat() - radToDec), (this.pivot.lng() - radToDec)));
    rotationGLatLngs(this.l, this.pivot, this.tilt);
}

SearchArea.prototype.install = function(){
    if(this.l.length > 2){
        if(this.polygon != null){
            map.removeOverlay(this.polygon);
        }
        //agar vertex yang baru ditambah bisa diupdate tambahkan posisi awal vertex pada Array
        this.l.push(this.l[0]);
        if(this.shape == "Circle"){
            var pivot = getPivotPoint(this.l);
            this.polygon = createCirclePolygon(pivot.lat(), pivot.lng(), this.radius);
            map.addOverlay(this.polygon);
        }else{
            this.polygon = ge.createPlacemark('');
            // Create the polygon.
            var polygon = ge.createPolygon('');
            polygon.setAltitudeMode(ge.ALTITUDE_CLAMP_TO_GROUND);
            polygon.setTessellate(true);
            this.polygon.setGeometry(polygon);

            // Add points for the outer shape.
            var outer = ge.createLinearRing('');
            for(i in this.l){
                outer.getCoordinates().pushLatLngAlt(this.l[i].lat(), this.l[i].lng(), 700);
            }
            polygon.setOuterBoundary(outer);
            //Create a style and set width and color of line
            this.polygon.setStyleSelector(ge.createStyle(''));
            var lineStyle = this.polygon.getStyleSelector().getLineStyle();
            lineStyle.setWidth(10);
            lineStyle.getColor().set('ff0000ff');
            var polyColor =
            this.polygon.getStyleSelector().getPolyStyle().getColor();
            polyColor.setA(10);
            polyColor.setB(0);
            polyColor.setG(0);
            polyColor.setR(0);
            // Add the placemark to Earth.
            map.addOverlay(this.polygon);
        }
        this.l.splice(this.l.length - 1, this.l.length);
        this.initTooltipMarker();
    }
}



SearchArea.prototype.checkSearchAreaType = function(){
    var r1, r2, n;
    r1 = hypotenuse(Math.abs(this.l[0].lng()-this.l[3].lng()), Math.abs(this.l[0].lat()-this.l[3].lat()));
    r2 = hypotenuse(Math.abs(this.l[1].lng()-this.l[2].lng()), Math.abs(this.l[1].lat()-this.l[2].lat()));
    n = Math.abs(r1 - r2);
    if(r1 == r2 || n < 0.0000000000001){
        this.type = "DRIFT";
    }else{
        this.type = "TRAPEZ";
    }
}





SearchArea.prototype.initTooltipMarker = function(){
    this.checkSearchAreaType();
    this.arrayTooltpMarker = new Array();
    var index = "A";
    var synchronizeL = new Array();
    if(this.type == "TRAPEZ"){
        for(i in this.l){
            if(i == 0){
                index = "B";
                synchronizeL[1] = this.l[0];
            }else if(i == 1){
                index = "A";
                synchronizeL[0] = this.l[1];
            }else if(i == 2){
                index = "D";
                synchronizeL[3] = this.l[2];
            }else if(i == 3){
                index = "C";
                synchronizeL[2] = this.l[3];
            }
            label = "Point " + index;
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
            
            this.arrayTooltpMarker.push(marker);
            map.addOverlay(marker);
        }
        // synchronize vertex of search area
        this.l = synchronizeL;
        return;
    }
    var label, r, zd, marker;
    r = hypotenuse(Math.abs(this.l[0].lng()-this.pivot.lng()), Math.abs(this.l[0].lat()-this.pivot.lat()));
    // zero degree variable
    zd = new GLatLng(this.pivot.lat() + r, this.pivot.lng());
    for(i in this.l){
        var arc = getAngle2(this.pivot, zd, this.l[i]);
        arc = Math.floor(arc);
        if(arc < 0) arc += 360;
        if(arc > 270 && arc <= 360 || arc == 0){
            label = "Point A";
            synchronizeL[0] = this.l[i];
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        } else if(arc > 0 && arc <= 90){
            label = "Point B";
            synchronizeL[1] = this.l[i];
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        } else if(arc > 90 && arc <= 180){
            label = "Point C";
            synchronizeL[2] = this.l[i];
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        } else if(arc > 180 && arc <= 270){
            label = "Point D";
            synchronizeL[3] = this.l[i];
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        }
        this.arrayTooltpMarker.push(marker);
        measureIntegrationMarker(marker);
        map.addOverlay(marker);
    }
    // synchronize vertex of search area
    this.l = synchronizeL;
}









SearchArea.prototype.remove = function(){
    if(this.polygon){
        map.removeOverlay(this.polygon);
        if(this.arrayTooltpMarker){
            for(i in this.arrayTooltpMarker){
                map.removeOverlay(this.arrayTooltpMarker[i]);
            }
            this.arrayTooltpMarker = null;
        }
    }
}

SearchArea.prototype.getBounds = function(){
    return this.polygon.getBounds();
}




/**/
function removeCurrentSearchArea(){
    if(current_SearchArea){
        current_SearchArea.remove();
        current_SearchArea = null;
    }
    AUTO_GENERATE_STATE = 0;
}


function setCurrentSearchArea(value){
    current_SearchArea = value;
}

/**Untuk mengecheck keberadaan dari Search Area apakah null, belum complite terbuat atau sudah complite terbuat
 *@return 0 = null
 *@return 1 = belum Complite Terbuat(belum tampak pada map)
 *@return 2 = sudah Complite terbuat(Sudah tampak pada Map)
 **/
function checkCurrentSearchArea(){
    if(current_SearchArea == null) return 0;
    else if(current_SearchArea.polygon == null){
        return 1;
    }else if(current_SearchArea.polygon != null){
        return 2;
    }
    return 3;
}


/**
 *this method interface for SarCoreApp
 *remove and create which reference in currentSearchArea variable
 */
function removeAndCreateCurrentSearchArea(start, latlngs, parrentID){
    if(current_SearchArea){
        current_SearchArea.remove();
    }
    current_SearchArea = new SearchArea(start, latlngs, null, null, parrentID);
    AUTO_GENERATE_STATE = 0;
}

/**
 *this method interface for DriftCalculation Object
 *remove and set which reference in currentSearchArea variable
 */
function removeAndSetCurrentSearchArea(searchArea){
    if(current_SearchArea){
        current_SearchArea.remove();
    }
    current_SearchArea = searchArea;
}



function getSearchAreaVertex(posisi1, posisi2, radius){    
    var rotate = getAngle(center, po, p1);
}