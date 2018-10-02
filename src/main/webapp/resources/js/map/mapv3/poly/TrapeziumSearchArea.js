/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var TRAPEZIUM_HASH = new Hash();
var ARR_IDTRAPEZIUM = new Array();
var index = 0;
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
    this.lkpMarker = CustomLabelMarker(DRIFTCALCULATION_LKP_DEFAULT_SYMBOL_GIS, this.smallRoundPivot, "<h5>LKP</h5>");
    google.maps.event.addListener(this.lkpMarker, "click", function() {
        this.openInfoWindowHtml(
            "<b class='title'><u>Last Known Position</u></b>"+
            "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.getLatLng().lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.getLatLng().lng(), LONGITUDE_MODE) + "</font>"
            )
        });
    this.datumMarker = CustomIconMarker(DRIFTCALCULATION_DATUM_DEFAULT_SYMBOL_GIS, this.largeRoundPivot);
    google.maps.event.addListener(this.datumMarker, "click", function() {
        this.openInfoWindowHtml(
            "<b class='title'><u>Datum Position</u></b>"+
            "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.getLatLng().lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.getLatLng().lng(), LONGITUDE_MODE) + "</font>"
            )
        });
    this.l = this.vertexs;
    this.polygon = new google.maps.Polygon({
                clickable: false,
                paths: this.l,
                strokeColor: "#FF0000",
                strokeOpacity: 1,
                strokeWeight: 3,
                fillColor: "#000000",
                fillOpacity: 0.1
            });
    this.smallRoundPivotToLargeRoundPivotLine = new google.maps.Polyline({
        strokeColor : "#FFFFFF",
        path : [this.smallRoundPivot , this.largeRoundPivot],
        clickable : false,
        strokeWeight : 0, 
        strokeOpacity : 1,
        icons: [{
            icon: {
                path: 'M 0,-1 0,1',
                strokeOpacity: 1,
                scale: 4
            },
            offset: '0',
            repeat: '20px'
        }]
    });
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
    //    removeTrapeziumSearchArea("Current");
    alert("removeCurrentTrapeziumSearchArea:"+idTrapezium);
    removeTrapeziumSearchArea(idTrapezium);
}

function createCurrentTrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID){
    //    createTrapeziumSearchArea("Current", smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID);
    createTrapeziumSearchArea(idTrapezium, smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID);
}


function createTrapeziumSearchArea(id, smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs, parrentID){
    TRAPEZIUM_HASH.setItem(id, new TrapeziumSearchArea(smallRoundPivot, largeRoundPivot, smallRoundRadius, largeRoundRadius, vertexs));
    idTrapezium = id;
    if(id == idTrapezium){
        index = idTrapezium.replace("Current", "");
            
        //		var current = TRAPEZIUM_HASH.getItem("Current");
        var current = TRAPEZIUM_HASH.getItem(id);
        setCurrentSearchArea(new SearchArea(null, current.l, null, current.polygon, parrentID, 'Trapezium'));
        ARR_IDTRAPEZIUM[index] = new SearchArea(null, current.l, null, current.polygon, parrentID, 'Trapezium');
        AUTO_GENERATE_STATE = 1;
                
    }
}

function removeTrapeziumSearchArea(id){
    if(TRAPEZIUM_HASH.hasItem(id)){
        TRAPEZIUM_HASH.getItem(id).remove();
        TRAPEZIUM_HASH.removeItem(id);
        //		if(id == "Current"){
        if(id == idTrapezium){
            index = idTrapezium.replace("Current", "");
            if(idTrapezium == "Current0"){
                setCurrentSearchArea(null);
                AUTO_GENERATE_STATE = 0;
            }else{
                setCurrentSearchArea(ARR_IDTRAPEZIUM[index - 1]);
                idTrapezium = "Current" + (index - 1);
                AUTO_GENERATE_STATE = 1;
            }
			
        }
    }
}