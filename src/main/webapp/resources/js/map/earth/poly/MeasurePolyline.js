/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *Membuat Polyline Object yang memiliki tooltip yang menampilkan berapa panjang Polyline
 *berdasarkan permukaan Bumi 2D
 **/

function MeasurePolyline(latlng, latlngs){
    if(latlngs){
        this.l = latlngs;
        this.draw();
    }else{
        this.l = new Array();
        this.l.push(latlng);
        this.firstPoint = createMarker('Point pertama pengukuran', latlng, MEASURE_FIRST_POINT_MARKER);
        map.addOverlay(this.firstPoint);
    }
}


MeasurePolyline.prototype.getLengthString = function(){
    var panjang;
    var distanceInKm = this.getLength();
    var distanceInNm = distanceInKm / KM_TO_NM_EXT;
    var angle = Math.floor(calculateAngle(this.l[0], this.l[this.l.length - 1]));
    if(angle < 0) angle+= 360;
    try{
        panjang = distanceInKm.toFixed(2) + " KM / "+ distanceInNm.toFixed(2) +" NM("+ angle +"')";
    }catch(error){
        panjang = distanceInKm + " KM "+ distanceInNm +" NM("+ angle +"')";
    }
    return panjang;
}

MeasurePolyline.prototype.reinstallMeasureTooltip = function(){
    var lastPoint = this.poly.getGeometry().getCoordinates().get(this.l.length - 1);
    var bfrLstPoint = this.poly.getGeometry().getCoordinates().get(this.l.length - 2);
    var lastMidPoint = new geo.Point(lastPoint).midpoint(new geo.Point(bfrLstPoint));
    if(this.marker){
        this.marker.getGeometry().setLatitude(lastMidPoint.lat());
        this.marker.getGeometry().setLongitude(lastMidPoint.lng());
        this.marker.setName(this.getLengthString());
    }else{
        this.marker = createMarker(this.getLengthString(), new GLatLng(lastMidPoint.lat(), lastMidPoint.lng()), MEASURE_UNDO_MARKER);
        var me = this;
        google.earth.addEventListener(this.marker, 'click', function(event) {
            isMeasureEditSession = true;
            event.preventDefault();
            me.removeLast();
        });
    }
}






// this.l and this.poly.getGeometry().getCoordinates() exaclly same but 
//this.l for java data repesentation
MeasurePolyline.prototype.draw = function(){
    this.poly = createPolyline(this.l, "#99012F97", 5);
    var me = this;
    google.earth.addEventListener(this.poly, 'mouseover', function(event){
        isMeasureEditSession = true;
    });
    gex.edit.editLineString(this.poly.getGeometry(), false, {
        editCallback : function(){
            isMeasureEditSession = true;
        },
        dropCallback : function(index, value){
            me.l[index] = value;
            me.reinstallMeasureTooltip();
        }
    });
    this.reinstallMeasureTooltip();
    map.addOverlay(this.poly); 
    map.addOverlay(this.marker);
}

MeasurePolyline.prototype.add = function(latlng){
    this.l.push(latlng);
    if(this.firstPoint){
        map.removeOverlay(this.firstPoint);
        this.firstPoint = null;
        this.draw();
    }else if(this.poly != null){
        var me = this;
        this.poly.getGeometry().getCoordinates().pushLatLngAlt(latlng.lat(), latlng.lng(), 0);
        this.reinstallMeasureTooltip();
        gex.edit.editLineString(this.poly.getGeometry(), false, {
            editCallback : function(){
                isMeasureEditSession = true;
            },
            dropCallback : function(index, value){
                me.l[index] = value;
                me.reinstallMeasureTooltip();
            }
        });
    }
}


MeasurePolyline.prototype.getLength = function(){
    var result = 0;
    for(var i=0;i<this.l.length;i++){
        var j = i + 1;
        if(this.l.length == j) return result;
        result += harversine(this.l[i].lat(), this.l[i].lng(), this.l[j].lat(), this.l[j].lng());
    }
    return result;
}


MeasurePolyline.prototype.removeLast = function(){
    this.l.pop();
    if(this.l.length > 1){	
        this.poly.getGeometry().getCoordinates().pop();
        gex.edit.editLineString(this.poly.getGeometry(), false, {
            editCallback : function(){
                isMeasureEditSession = true;
            },
            dropCallback : function(index, value){
                me.l[index] = value;
                me.reinstallMeasureTooltip();
            }
        });
	      
        this.reinstallMeasureTooltip();
    }else{
        redrawMeasure();
    }
}


function removeMeasure(){
    if(polylineMeasure){
        if(polylineMeasure.marker){
            map.removeOverlay(polylineMeasure.marker);
        }
        if(polylineMeasure.firstPoint){
            map.removeOverlay(polylineMeasure.firstPoint);
        }
        if(polylineMeasure.poly){
            gex.edit.endEditLineString(polylineMeasure.poly.getGeometry());
            map.removeOverlay(polylineMeasure.poly);
        }
        polylineMeasure = null;
    }
}


function measureIntegrationPOI(balloon, marker){
    if(MODE == 'MEASURE'){
        if(polylineMeasure){
            balloon.setContentString(balloon.getContentString() + getMeasurementScript(2, marker, balloon));
        }else{
            balloon.setContentString(balloon.getContentString() + getMeasurementScript(1, marker, balloon));
        }
    }
}

// from MeasurePolyline.measureIntegrationPOI()
// for : integrasi measurement dengan POI dengan keadaan measure belum bibuat sebelumnya
function createMeasureItegratePOI(latlng){
    polylineMeasure = new MeasurePolyline(latlng);
    ge.setBalloon(null);
}


// from MeasurePolyline.measureIntegrationPOI()
// for : integrasi measurement dengan POI dengan keadaan menambah point ke obhject existing measure
function addMeasureItegratePOI(latlng){
    polylineMeasure.add(latlng);
    ge.setBalloon(null);
}



// param 1 = create new
// param 2 = add existing
// for mendapatkan script tag html untuk menintegrasikan Measure ke POI
function getMeasurementScript(type, marker){
    var lat = marker.getGeometry().getLatitude();
    var lng = marker.getGeometry().getLongitude();
    if(type == 2){
        return "<a  href='#' onclick='addMeasureItegratePOI(new GLatLng("+ lat +", "+ lng +"))' style='font-size:10px;position:absolute;bottom=0;right:0;margin-top:50px;'>titik pengukuran selanjutnya</a>";
    }else if(type == 1){
        return "<a href='#' onclick='createMeasureItegratePOI(new GLatLng("+ lat +", "+ lng +"))' style='font-size:10px;position:absolute;bottom=0;right:0;margin-top:50px;'>jadikan titik pertama pengukuran</a>";
    }
}



// Integrasikan measure pada Marker
// Icon marker akan berubah memjadi icon measure ketika di mouseover 
// Icon marker akan berubah memjadi sediakala ketika di mouseout 
// Point measure akan bertambah pada saat mengclik marker
function measureIntegrationMarker(marker){
    var oldStyle = marker.getStyleSelector();
    if(oldStyle){
        // old Style variable menyimpan nilai url image 
        oldStyle = oldStyle.getIconStyle().getIcon().getHref();
    }
    google.earth.addEventListener(marker, 'mouseover', function(event){
        if(MODE == 'MEASURE'){
            if(oldStyle){
                marker.getStyleSelector().getIconStyle().getIcon().setHref(MEASURE_FIRST_POINT_MARKER);
            }else{
                marker.setStyleSelector(measureStyle);
            }
        }
    });
    google.earth.addEventListener(marker, 'mouseout', function(event){
        if(MODE == 'MEASURE'){
            if(oldStyle){
                marker.getStyleSelector().getIconStyle().getIcon().setHref(oldStyle);
            }else{
                marker.setStyleSelector(null);
            }
        }
    });
    google.earth.addEventListener(marker, 'click', function(event){
        event.preventDefault();
        if(MODE == 'MEASURE'){
            var latlng = marker.getGeomerty();
            latlng = new GLatLng(latlng.getLatitude(), latlng.getLongitude());
            if(polylineMeasure){
                addMeasureItegratePOI(latlng);
            }else{
                createMeasureItegratePOI(latlng);
            }
        }
    });
}


function redrawMeasure(){
    if(polylineMeasure){
        var firstPoint = polylineMeasure.l[0];
        removeMeasure();  
        isMeasureEditSession = false;
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


// function ini mengembalikan style buat measure. dib panggil pada initCB pada Earth.html dan GEE.html
function createMeasureStyle(){
    var icon = ge.createIcon('');
    icon.setHref(MEASURE_FIRST_POINT_MARKER);
    var style = ge.createStyle(''); 
    style.getIconStyle().setIcon(icon);
    return style;
}