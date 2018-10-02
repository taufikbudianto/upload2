/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *Membuat Polyline Object yang memiliki tooltip yang menampilkan berapa panjang Polyline
 *berdasarkan permukaan Bumi 2D
 **/
function MeasurePolyline(latlng, latlngs){
    this.tooltip = new Tooltip("Measure", 1, map);
    this.isOverMeasureUndoButton = false;
    if(latlngs){
        this.l = latlngs;
        this.draw();
    }else{
        this.l = new Array();
        this.l.push(latlng);
        this.poly = new GPolyline(this.l, "#972F01");
        map.addOverlay(this.poly);
        this.poly.enableDrawing();
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
    var undo = document.createElement('img');
    undo.src = MEASURE_UNDO_MARKER;
    undo.style.width = '24px';
    undo.style.height = '24px';           
    undo.style.MozOpacity=0.3;
    var me = this;
    undo.onmouseover = function(){
        undo.style.MozOpacity=1;
        me.isOverMeasureUndoButton = true;
    };
    undo.onmouseout = function(){
        undo.style.MozOpacity=0.3;
        me.isOverMeasureUndoButton = false;
    };  
    undo.onclick = function(){
        me.removeLast();
        me.isOverMeasureUndoButton = false;
    }
    var lastPoint = this.l[this.l.length - 1];
    var bfrLstPoint = this.l[this.l.length - 2];
    //Validasi ketika garis mearuse terakhir diremove
    if(lastPoint && bfrLstPoint){
        var midLastPoint = midPointNotAccuracy(lastPoint, bfrLstPoint);
        this.tooltip.show(midLastPoint, stringHtmlOnMap(panjang), undo);
    }
}



MeasurePolyline.prototype.draw = function(){
    this.poly = new GPolyline(this.l, "#972F01");
    map.addOverlay(this.poly);
    this.poly.enableEditing({
        maxVertices : this.poly.getVertexCount()
    });
    this.installListener();
    this.reinstallMeasureTooltip();
}

MeasurePolyline.prototype.installListener = function(){
    var me = this;
    GEvent.addListener(this.poly,"lineupdated",function(){
        var updateVertex;
        for(i = 0; i < this.getVertexCount(); ++i){
            var vertex = this.getVertex(i);
            if(!me.l[i].equals(vertex)){
                updateVertex = vertex;
                me.l[i] = vertex;  	
                break;
            }
        }
        me.reinstallMeasureTooltip();
    });
    GEvent.addListener(this.poly,"click",function(latlng){
        me.tooltip.redraw(true, latlng);
        me.tooltip.div_.style.visibility = 'visible';
    });
}

MeasurePolyline.prototype.removeLast = function(){
    if(this.poly){
        this.poly.disableEditing();
        this.poly.deleteVertex(this.poly.getVertexCount() - 1); 
        this.l.pop();
        if(this.l.length > 1){
            this.poly.enableEditing({
                maxVertices : this.poly.getVertexCount() - 1
            });
            this.reinstallMeasureTooltip();
        }else{
            redrawMeasure();
        }
    }
}


MeasurePolyline.prototype.add = function(latlng){
    //condisional apakh buton undo sedang dalam keadaan mouse over
    if(this.isOverMeasureUndoButton) return;
    this.l.push(latlng);
    if(this.isDrawing){
        if(this.poly){
            this.poly.disableEditing();
            map.removeOverlay(this.poly);
        }
        this.draw();
        this.isDrawing = false;
    }else if(this.poly){
        this.poly.disableEditing();
        this.poly.enableEditing({
            maxVertices : this.poly.getVertexCount() + 1
        });
        this.poly.insertVertex(this.poly.getVertexCount(), latlng);
        this.reinstallMeasureTooltip();
    }else{
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
    GEvent.addListener(marker, "mouseout", function(latlng){
        if(onMouseOutCallback) onMouseOutCallback(latlng, this);
        if(MODE == 'MEASURE' || MODE == 'FREE_DEFINE_PATTERN'){
            this.setImage(this.getIcon().image);
        }
    });
    GEvent.addListener(marker, "mouseover", function(latlng){
        if(onMouseOverCallback) onMouseOverCallback(latlng, this);
        if(MODE == 'MEASURE'){
            this.setImage(MEASURE_FIRST_POINT_MARKER);
        }else if(MODE == 'FREE_DEFINE_PATTERN'){
            this.setImage(FREE_DEFINE_PATTERN_ICON);
        }
    });
    GEvent.addListener(marker, "click", function(latlng){
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
        polylineMeasure.poly.disableEditing();
        polylineMeasure.tooltip.hide();
        polylineMeasure.tooltip = null;
        if(polylineMeasure.poly){
            map.removeOverlay(polylineMeasure.poly);
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
