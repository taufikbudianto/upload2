/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *Membuat Polyline Object yang memiliki tooltip yang menampilkan berapa panjang Polyline
 *berdasarkan permukaan Bumi 2D
 **/
function MeasurePolyline(latlng, latlngs){
    this.tooltip = new MarkerWithLabel({
        icon: new google.maps.MarkerImage(
            MEASURE_UNDO_MARKER,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            ),
        visible : false,
        map : map,
        position: (latlng? latlng : latlngs[latlngs.length - 1]),
        raiseOnDrag: true,
        labelContent: 'Measure',
        labelAnchor: new google.maps.Point(10, 0),
        labelClass: ".marker-with-label", // the CSS class for the label
        labelStyle: {
            opacity: 0.75
        }
    });
    var me = this;
    google.maps.event.addListener(this.tooltip, 'click', function(){
        me.removeLast();
    });
    if(latlngs){
        this.l = latlngs;
        this.draw();
    }else{
        this.l = new Array();
        this.l.push(latlng);
        //this.poly = new GPolyline(this.l, "#972F01");
        //map.addOverlay(this.poly);
        //this.poly.enableDrawing();
        this.poly = new google.maps.Polyline({
            strokeColor : "#972F01",
            path : this.l,
            map : map,
            editable : true
        });
        this.installListener();
        this.isDrawing = true;
    }
}

//var angleFirstToLastPoint = toDeg(getAngle(this.l[0], new GLatLng(0,9), new GLatLng(9,0)))
MeasurePolyline.prototype.reinstallMeasureTooltip = function(){
    var panjang;
    var distanceInKm = this.poly.getLength() / 1000;
    var distanceInNm = distanceInKm / KM_TO_NM_EXT;
    var angle = Math.floor(calculateAngle(this.l[0], this.l[this.l.length - 1]));
    if(angle < 0) angle += 360;
    try{
        panjang = new String(distanceInKm.toFixed(2) + " KM / "+ distanceInNm.toFixed(2) +" NM("+ angle +"°)");
    }catch(error){
        panjang = new String(distanceInKm + " KM / "+ distanceInNm +" NM("+ angle +"°)");
    }
    var lastPoint = this.l[this.l.length - 1];
    var bfrLstPoint = this.l[this.l.length - 2];
    //Validasi ketika garis mearuse terakhir diremove
    if(lastPoint && bfrLstPoint){
        var midLastPoint = midPointNotAccuracy(lastPoint, bfrLstPoint);
        var tooltipInfo = stringHtmlOnMap(panjang);
        this.tooltip.labelContent = tooltipInfo;
        this.tooltip.label.labelDiv_.innerHTML = tooltipInfo;
        this.tooltip.label.eventDiv_.innerHTML = this.tooltip.label.labelDiv_.innerHTML;
        this.tooltip.setPosition(midLastPoint);
        this.tooltip.setVisible(true);
    }
}



MeasurePolyline.prototype.draw = function(){
    this.poly = new google.maps.Polyline({
        strokeColor : "#972F01",
        path : this.l,
        map : map,
        editable : true
    });
    this.installListener();
    this.reinstallMeasureTooltip();
}

MeasurePolyline.prototype.installListener = function(){
    var me = this;
    google.maps.event.addListener(this.poly.getPath(), "insert_at", function(){
        me.updatePolyPath();
        me.reinstallMeasureTooltip();
    });
    google.maps.event.addListener(this.poly.getPath(), "remove_at", function(){
        me.updatePolyPath();
        me.reinstallMeasureTooltip();
    });
    google.maps.event.addListener(this.poly.getPath(), "set_at", function(){
        me.updatePolyPath();
        me.reinstallMeasureTooltip();
    });
}


MeasurePolyline.prototype.updatePolyPath = function(){
    this.l = this.poly.getPath().getArray();
    
}

MeasurePolyline.prototype.removeLast = function(){
    if(this.poly){
        this.poly.getPath().pop();
        this.updatePolyPath();
        if(this.l.length > 1){
            this.reinstallMeasureTooltip();
        }else{
            redrawMeasure();
        }
    }
}


MeasurePolyline.prototype.add = function(latlng){
    if(this.poly){
        this.poly.getPath().insertAt(this.poly.getPath().getArray().length, latlng);
    }else{
        if(!this.l){
            this.l = new Array();
        }   
        this.l.push(latlng);
        this.draw();
    }    
}


// from MeasurePolyline.measureIntegrationPOI()
// for : integrasi measurement dengan POI dengan keadaan measure belum bibuat sebelumnya
/*
 *Mulai dari sarcore versi 1.4.7 method ini tidak hanya dipakai pada saat mode measurment tapi juga free define search pattern
 *mode free define : 'FREE_DEFINE_PATTERN'
 *mode measure : 'MEASURE'
 */
function createMeasureItegratePOI(latlng){
    if(MODE == 'MEASURE'){
        polylineMeasure = new MeasurePolyline(latlng);
        eBubble.hide();
    }else if(MODE == 'FREE_DEFINE_PATTERN'){
        eBubble.hide();
        document.title = "FREE_DEFINE_PATTERN_CREATE_NEW#"+latlng.lat()+"#"+latlng.lng();
    }
}


// from MeasurePolyline.measureIntegrationPOI()
// for : integrasi measurement dengan POI dengan keadaan menambah point ke obhject existing measure
/*
 *Mulai dari sarcore versi 1.4.7 method ini tidak hanya dipakai pada saat mode measurment tapi juga free define search pattern
 *mode free define : 'FREE_DEFINE_PATTERN'
 *mode measure : 'MEASURE'
 */
function addMeasureItegratePOI(latlng){
    if(MODE == 'MEASURE'){
        polylineMeasure.add(latlng);
        eBubble.hide();
    }else if(MODE == 'FREE_DEFINE_PATTERN'){
        eBubble.hide();
        currentFreeDefine.add(latlng);
    }
}



// state 1 = create new
// state 2 = add existing
// type 1 = return Node
// type 2 = return Html String
// for mendapatkan script tag html untuk menintegrasikan Measure ke POI
/*
 *Mulai dari sarcore versi 1.4.7 method ini tidak hanya dipakai pada saat mode measurment tapi juga free define search pattern
 *mode free define : 'FREE_DEFINE_PATTERN'
 *mode measure : 'MEASURE'
 */
function getMeasurementScript(state, latlng, type){
    var a, label;
    if(state == 1){
        if(type == 1){
            a = document.createElement('a');
            a.style.marginTop = '50px';
            a.style.right = '50px';
            a.style.bottom = '30px';
            a.style.position = 'absolute';
            a.style.fontSize = '10px';
            a.id = 'measurment_button';
            a.setAttribute('href', '#');
            a.onclick = function(){
                createMeasureItegratePOI(latlng);
            }
            if(MODE == 'MEASURE'){
                a.innerHTML = "jadikan titik pertama pengukuran";
            }else if(MODE == 'FREE_DEFINE_PATTERN'){
                a.innerHTML = "jadikan titik pertama pembuatan pattern";
            }
            return a;
        }else if(type == 2){
            if(MODE == 'MEASURE'){
                label = "jadikan titik pertama pengukuran";
            }else if(MODE == 'FREE_DEFINE_PATTERN'){
                label = "jadikan titik pertama pembuatan pattern";
            }
            return "<a id='measurment_button' href='#' onclick='createMeasureItegratePOI(new GLatLng("+ latlng.lat() +", "+ latlng.lng() +"))' style='font-size:10px;position:absolute;bottom=0;right:0;margin-top:50px;'>"+ label +"</a>";
        }
    }else if(state == 2){
        if(type == 1){
            a = document.createElement('a');
            a.style.marginTop = '50px';
            a.id = 'measurment_button';
            a.style.right = '50px';
            a.style.bottom = '30px';
            if(MODE == 'MEASURE'){
                a.innerHTML = "titik pengukuran selanjutnya";
            }else if(MODE == 'FREE_DEFINE_PATTERN'){
                a.innerHTML = "titik pattern selanjutnya";
            }
            a.style.position = 'absolute';
            a.style.fontSize = '10px';
            a.setAttribute('href', '#');
            a.onclick = function(){
                addMeasureItegratePOI(latlng);
            }
            return a;
        }else if(type == 2){
            if(MODE == 'MEASURE'){
                label = "titik pengukuran selanjutnya";
            }else if(MODE == 'FREE_DEFINE_PATTERN'){
                label = "titik pattern selanjutnya";
            }
            return "<a id='measurment_button' href='#' onclick='addMeasureItegratePOI(new GLatLng("+ latlng.lat() +", "+ latlng.lng() +"))' style='font-size:10px;position:absolute;bottom=0;right:0;margin-top:50px;'>"+ label +"</a>";
        }
    }
}

//icon yang ingin di integrasikan dengan measurement
/*
 *Mulai dari sarcore versi 1.4.7 method ini tidak hanya dipakai pada saat mode measurment tapi juga free define search pattern
 *mode free define : 'FREE_DEFINE_PATTERN'
 *mode measure : 'MEASURE'
 *image source variable FREE_DEFINE_PATTERN_ICON
 */
function measurementPointIntegration(marker, onMouseOverCallback, onMouseOutCallback, onClickCallback){
    google.maps.event.addListener(marker, "mouseout", function(mouseevent){
        if(onMouseOutCallback) onMouseOutCallback(mouseevent.latLng, this);
        if(MODE == 'MEASURE' || MODE == 'FREE_DEFINE_PATTERN'){
            this.setIcon(this.getReserveIcon());
        }
    });
    google.maps.event.addListener(marker, "mouseover", function(mouseevent){
        if(onMouseOverCallback) onMouseOverCallback(mouseevent.latLng, this);
        if(MODE == 'MEASURE'){
            this.setReserveIcon(this.getIcon());
            this.setIcon(measure_icon);
        }else if(MODE == 'FREE_DEFINE_PATTERN'){
            this.setReserveIcon(this.getIcon());
            this.setIcon(free_define_pattern);
        }
    });
    google.maps.event.addListener(marker, "click", function(mouseevent){
        var latlng = mouseevent.latLng;
        if(onClickCallback) onClickCallback(latlng, this);
        if(MODE == 'MEASURE'){
            if(polylineMeasure){
                polylineMeasure.add(marker.getLatLng());
            }else{
                polylineMeasure = new MeasurePolyline(marker.getLatLng());
            }
        }else if(MODE == 'FREE_DEFINE_PATTERN'){
            if(currentFreeDefine){
                currentFreeDefine.add(latlng);
            }else{
                document.title = "FREE_DEFINE_PATTERN_CREATE_NEW#"+latlng.lat()+"#"+latlng.lng();
            }
        }
    });
}



function removeMeasure(){
    if(polylineMeasure){
        if(polylineMeasure.tooltip){
            polylineMeasure.tooltip.setMap(null);
            polylineMeasure.tooltip = null;
        }
        if(polylineMeasure.poly){
            polylineMeasure.poly.setMap(null);
            polylineMeasure.poly = null;
        }
        polylineMeasure = null;
    }
}

function redrawMeasure(){
    if(polylineMeasure){
        var firstPoint = polylineMeasure.l[0];
        removeMeasure();
        polylineMeasure = new MeasurePolyline(firstPoint);
    }
}


function getMeasureVertexs(){
    try{
        return polylineMeasure.l;
    }catch(error){
        return null;
    }
}
