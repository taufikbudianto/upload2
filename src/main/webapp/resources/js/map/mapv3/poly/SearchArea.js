/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var SEARCH_AREA = new Array();
function setNewSearchArea(){
    SEARCH_AREA = new Array();
}

//BUAT DARI writeScript methode
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
    // log("SearchArea in JS:" + start.toString()+ "," +latlngs.toString() +"," + 
    //     constructData.toString() +"," + polygon.toString()+ "," + 
    //     parrentID.toString()+","+ shape.toString()+ "," + radius.toString());
    this.start = start;
    this.parrentID = parrentID;
    this.shape = shape;
    this.radius = radius;
    this.polygon = null;
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
        this.poly = new google.maps.Polyline({
            path : [start],
            editable : true
        });
        map.addOverlay(this.poly);
        this.l = new Array();
        this.l.push(this.start);
    } else{
        this.l = latlngs;
        this.pivot = getPivotPoint(this.l);
        if(!polygon){
            this.install();
        }else{
            /*
             *Search Area work flow code below will be taken if state search area is create new of trapezium
             */
            this.polygon = polygon;
        /*
            *uncomment code dibawah jika ingin menampilkan urutan trapezium search area
            *this.initTooltipMarker();
            */
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




SearchArea.prototype.add = function(latlng){
    if(this.polygon != null){
        if(this.polygon.getBounds().contains(latlng)) //Jika latlng berada dalam area polygon
            return;
    }
    this.l.push(latlng);
    this.install();
}


SearchArea.prototype.install = function(){
    if(this.l.length > 2){
        //remove polygon yang lama
        if(this.poly != null){
            map.removeOverlay(this.poly);
            this.poly = null;
        }
        if(this.polygon != null){
            map.removeOverlay(this.polygon);
        }
        //agar vertex yang baru ditambah bisa diupdate tambahkan posisi awal vertex pada Array
        
        if(this.shape == 'Circle'){
            this.polygon = new google.maps.Circle({
                center :  getPivotPoint(this.l),
                radius : (this.radius * 1.852) * 1000,
                strokeColor: "#FF0000",
                strokeOpacity: 1.0,
                strokeWeight: 5,
                fillColor: "#000000",
                fillOpacity: 0.1,
                map : map
            });
        }else{
            this.polygon = new google.maps.Polygon({
                clickable: false,
                paths: this.l,
                strokeColor: "#FF0000",
                strokeOpacity: 1.0,
                strokeWeight: 3,
                fillColor: "#000000",
                fillOpacity: 0.1,
                map : map
            });
        }
        this.initTooltipMarker();
    }
}



SearchArea.prototype.remove = function(){
    if(this.poly){
        map.removeOverlay(this.poly);
    }
    if(this.polygon){
        map.removeOverlay(this.polygon);
        this.polygon = null;
        if(this.arrayTooltpMarker){
            for(i in this.arrayTooltpMarker){
                map.removeOverlay(this.arrayTooltpMarker[i]);
            }
            this.arrayTooltpMarker = null;
        }
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
    var synchronizeL;
    this.checkSearchAreaType();
    this.arrayTooltpMarker = new Array();
    if(this.type == "TRAPEZ"){
        synchronizeL = new Array();
        var index = "A";
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
            label = stringHtmlOnMap("Point " + index);
            marker = this.createLabelPoint(this.l[i], label);
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
    synchronizeL = new Array();
    for(i in this.l){
        var arc = getAngle2(this.pivot, zd, this.l[i]);
        arc = Math.floor(arc);
        if(arc < 0) arc += 360;
        if(arc > 270 && arc <= 360 || arc == 0){
            label = stringHtmlOnMap("Point A");
            synchronizeL[0] = this.l[i];
            marker = this.createLabelPoint(this.l[i], label);
        } else if(arc > 0 && arc <= 90){
            label = stringHtmlOnMap("Point B");
            synchronizeL[1] = this.l[i];
            marker = this.createLabelPoint(this.l[i], label);
        } else if(arc > 90 && arc <= 180){
            label = stringHtmlOnMap("Point C");
            synchronizeL[2] = this.l[i];
            marker = this.createLabelPoint(this.l[i], label);
        } else if(arc > 180 && arc <= 270){
            label = stringHtmlOnMap("Point D");
            synchronizeL[3] = this.l[i];
            marker = this.createLabelPoint(this.l[i], label);
        }
        
        this.arrayTooltpMarker.push(marker);
        map.addOverlay(marker);
    }
    // synchronize vertex of search area
    this.l = synchronizeL;
}





SearchArea.prototype.createLabelPoint = function(latlng, label){
    var marker = new MarkerWithLabel({
       position: latlng,
       labelContent: label,
       labelAnchor: new google.maps.Point(22, 0),
       labelClass: ".marker-with-label", // the CSS class for the label
       labelStyle: {opacity: 0.75},
       icon: {}
     });
    return marker;
}


SearchArea.prototype.getBounds = function(){
    return this.polygon.getBounds();
}

SearchArea.prototype.doAfterLineUpdate = function(poly, l){
    this.l = poly.getPath().getArray();
}








/**/
function removeCurrentSearchArea(){
    if(current_SearchArea){
        current_SearchArea.remove();
        current_SearchArea = null;
    }
    AUTO_GENERATE_STATE = 0;
}


var current_SearchArea;
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
//harus di modif di sini untuk array waypoint
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