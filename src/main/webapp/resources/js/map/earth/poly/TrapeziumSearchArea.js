/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var TRAPEZIUM_HASH = new Hash();
var idTrapezium = "";

function TrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs){
    this.smallRoundPivot = smallRoundPivot;
    this.largeRoundPivot = largeRoundPivot;
    this.smallRoundRadius = smallRoundRadius;
    this.largeRoundRadius = largeRoundRadius;
    this.vertexs = vertexs;
    this.draw();
}

TrapeziumSearchArea.prototype.draw = function(){
    //    this.largeRound = new Circle(this.largeRoundPivot, this.largeRoundRadius);
    //    this.smallRound = new Circle(this.smallRoundPivot, this.smallRoundRadius);
    this.lkpMarker = createMarker("LKP", this.smallRoundPivot, DRIFTCALCULATION_LKP_DEFAULT_SYMBOL_GIS);
    var popupInfoLkp = 
    "<b class='title'><u>Last Known Position</u></b>"+
    "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.smallRoundPivot.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class= 'field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.smallRoundPivot.lng(), LONGITUDE_MODE) + "</font>";
    ballonForMarker(this.lkpMarker, popupInfoLkp);
    
    this.datumMarker = createMarker("DATUM", this.largeRoundPivot, DRIFTCALCULATION_DATUM_DEFAULT_SYMBOL_GIS);
    var popupInfoDatum = 
    "<b class='class'><u>Datum Position</u></b>"+
    "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.largeRoundPivot.lat(), LATITUDE_MODE) + "</font>" +
    "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.largeRoundPivot.lng(), LONGITUDE_MODE) + "</font>";
    ballonForMarker(this.datumMarker, popupInfoDatum);
    
    this.smallRoundPivotToLargeRoundPivotLine = createPolyline([this.smallRoundPivot , this.largeRoundPivot], "#990000ff", 2);
    
    this.l = this.vertexs;
    this.l.push(this.l[0]);
    
    this.polygon = ge.createPlacemark('');

    // Create the polygon.
    var polygon = ge.createPolygon('');
    polygon.setAltitudeMode(ge.ALTITUDE_CLAMP_TO_GROUND);
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
    
    map.addOverlay(this.lkpMarker);
    map.addOverlay(this.datumMarker);
    map.addOverlay(this.polygon);
    map.addOverlay(this.smallRoundPivotToLargeRoundPivotLine);
}

TrapeziumSearchArea.prototype.remove = function(){
    //    this.removeRoundOverlay();
    map.removeOverlay(this.lkpMarker);
    map.removeOverlay(this.datumMarker);
    map.removeOverlay(this.polygon);
    map.removeOverlay(this.smallRoundPivotToLargeRoundPivotLine);
}

TrapeziumSearchArea.prototype.removeRoundOverlay = function(){
    map.removeOverlay(this.largeRound.poly);
    map.removeOverlay(this.smallRound.poly);
    this.largeRound = null;
    this.smallRound = null; 
}



var currentTrapeziumSearchArea;



function createAndRemoveCurrentTrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID){
    removeCurrentTrapeziumSearchArea();
    createCurrentTrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID);
}

function removeCurrentTrapeziumSearchArea(){
    removeTrapeziumSearchArea(idTrapezium);
}

function createCurrentTrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID){
    createTrapeziumSearchArea(idTrapezium, smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID);
}


function createTrapeziumSearchArea(id, smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID){
    TRAPEZIUM_HASH.setItem(id, new TrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs));
    idTrapezium = id;
    if(id == idTrapezium){
        var current = TRAPEZIUM_HASH.getItem(idTrapezium);
        current.l.pop();
        setCurrentSearchArea(new SearchArea(null, current.l, null, current.polygon, parrentID, 'Trapezium'));
        AUTO_GENERATE_STATE = 1;
    }
}

function removeTrapeziumSearchArea(id){
    if(TRAPEZIUM_HASH.hasItem(id)){
        TRAPEZIUM_HASH.getItem(id).remove();
        TRAPEZIUM_HASH.removeItem(id);
        if(id == idTrapezium){
            setCurrentSearchArea(null);
            AUTO_GENERATE_STATE = 0;
        }
    }
}