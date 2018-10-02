/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var current_driftCalculation;
var AUTO_GENERATE_STATE = 0; //1 untuk true dan 0 untuk false

function createCurrentDriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angle, parrentID, lkpToDatumDistance, lkpToDatumAngle,shape, drawLine){
    current_driftCalculation = new DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angle, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine);
    removeAndSetCurrentSearchArea(current_driftCalculation.searchArea);
}

/**
 *this methode be interface to the SarCoreApp
 */
function removeAndCreateCurrentDriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angle, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine){
    if(current_driftCalculation){
        current_driftCalculation.remove();
    }
    current_driftCalculation = new DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angle, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine);
    removeAndSetCurrentSearchArea(current_driftCalculation.searchArea);
    AUTO_GENERATE_STATE = 1;
}

function removeCurrentDriftCalculation(){
    if(current_driftCalculation){
        current_driftCalculation.remove();
        current_driftCalculation = null;
        AUTO_GENERATE_STATE = 0;
    }
}

function DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angle, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine){
    this.parrentID = parrentID;
    this.shape = shape;
    this.drawLine = drawLine;
    /*
     * drawLine false indicate search area direct form incident point
     */
    if(drawLine){
        var distanceInKM = lkpToDatumDistance * KM_TO_NM_EXT;
        try{
            this.distanceLKPtoDatum = distanceInKM.toFixed(2) + "KM ("+ lkpToDatumDistance.toFixed(2) +" NM)";
        }catch(error){
            this.distanceLKPtoDatum = distanceInKM + "KM ("+ lkpToDatumDistance +" NM)";
        }
        this.lkpToDatumAngle = Math.floor(lkpToDatumAngle);
        if(this.lkpToDatumAngle < 0) this.lkpToDatumAngle += 360; 
        if(this.lkpToDatumAngle + 180 > 360){
            this.datumToLkpAngle = this.lkpToDatumAngle - 180;
        }else{
            this.datumToLkpAngle = this.lkpToDatumAngle + 180;
        }
    }
    this.lkp = lkp;
    this.datum = datum;
    map.setCenter(datum, 15);
    this.leftLeeway = leftLeeway;
    this.rightLeeway = rightLeeway;
    this.radius = radius;
    this.angle = angle;
    this.draw();
    this.createSubSearchArea();
}


DriftCalculation.prototype.createSubSearchArea = function(){
    var zeroDegreeFromDatum, nw, nwDegree; 
    zeroDegreeFromDatum = new GLatLng(this.datum.lat() + (this.radius * ONE_NM_TO_DEG),this.datum.lng() - (this.radius * ONE_NM_TO_DEG));
    nwDegree = this.getSubSearchAreaTilt(this.searchArea.l, this.datum, zeroDegreeFromDatum);
    this.subSearchAreaCollection = new SubSearchAreaCollection(this.searchArea.pivot, this.radius, -nwDegree);
}


DriftCalculation.prototype.getSubSearchAreaTilt = function(vertexs, pivot, zeroDegreeFromDatum){
    var result; 
    for(i in vertexs){
        result = vertexs[i];
        if((result.lat() > pivot.lat() && result.lng() < pivot.lng()) || ((result.lat() > pivot.lat() && result.lng() == pivot.lng()))){
            return toDeg(getAngle(pivot, zeroDegreeFromDatum, result));
        }
    }
}


DriftCalculation.prototype.draw = function(){
    if(this.drawLine){
        this.lkpMarker = createMarker("LKP", this.lkp, DRIFTCALCULATION_LKP_DEFAULT_SYMBOL_GIS);
        var popupInfoLkp = 
        "<b class='title'><u>Last Known Position</u></b>"+
        "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.lkp.lat(), LATITUDE_MODE) + "</font>" +
        "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.lkp.lng(), LONGITUDE_MODE) + "</font>" +
        "<br><b class='field'>LKP-DATUM Distance</b>   : <font class='value'>" + this.distanceLKPtoDatum + "</font>" +
        "<br><b class='field'>LKP-DATUM Angle</b>   : <font class='value'>" + this.lkpToDatumAngle + "°" + "</font>";
        ballonForMarker(this.lkpMarker, popupInfoLkp);
        map.addOverlay(this.lkpMarker);
        this.lkpToDatumLine = createPolyline([this.lkp , this.datum], "#990000ff", 2);
        map.addOverlay(this.lkpToDatumLine);
    //WARNING LKP TO LEFT LEEWAY NOT DRAWN
    //WARNING LKP TO RIGHT LEEWAY NOT DRAWN
    }
    this.searchArea = new SearchArea(null, null, {
        tilt : this.angle , 
        pivot : this.datum, 
        radius : this.radius
    }, null, this.parrentID, this.shape, this.radius);
    
    //WARNING LEFT LEEWAY TO RIGHT LEEWAY NOT DRAWN
    this.datumMarker = createMarker("DATUM", this.datum, DRIFTCALCULATION_DATUM_DEFAULT_SYMBOL_GIS);
    var popupInfoDatum;
    if(this.drawLine){
        popupInfoDatum =     
        "<b class='title'><u>Datum Position</u></b>"+
        "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.datum.lat(), LATITUDE_MODE) + "</font>" +
        "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.datum.lng(), LONGITUDE_MODE) + "</font>" + 
        "<br><b class='field'>DATUM-LKP Distance</b>   : <font class='value'>" + this.distanceLKPtoDatum + "</font>" +
        "<br><b class='field'>DATUM-LKP Angle</b>   : <font class='value'>" + this.datumToLkpAngle + "°"+ "</font>";
    }else{
        popupInfoDatum =     
        "<b class='title'><u>Datum Position</u></b>"+
        "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.datum.lat(), LATITUDE_MODE) + "</font>" +
        "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.datum.lng(), LONGITUDE_MODE) + "</font>" + 
        "<br><b class='field'>Radius Pencarian</b>   : <font class='value'>" + this.radius + "</font>";
    }
    ballonForMarker(this.datumMarker, popupInfoDatum);
    map.addOverlay(this.datumMarker);
    
    map.setCenter(this.datum, 5);  
}

DriftCalculation.prototype.getLkp = function(){
    return this.lkp;
}

DriftCalculation.prototype.getAngle = function(){
    return this.angle;
}

DriftCalculation.prototype.getRadius = function(){
    return this.radius;
}

DriftCalculation.prototype.getRightLeeway = function(){
    return this.rightLeeway;
}

DriftCalculation.prototype.getLeftLeeway = function(){
    return this.lefttLeeway;
}

DriftCalculation.prototype.getSubSearchAreaCollection = function(){
    return this.subSearchAreaCollection;
}

DriftCalculation.prototype.remove = function(){
    if(this.lkpToDatumLine) map.removeOverlay(this.lkpToDatumLine);
    this.searchArea.remove();
    if(this.datumMarker) map.removeOverlay(this.datumMarker);
    if(this.lkpMarker) map.removeOverlay(this.lkpMarker);
}

function checkingCurrentDriftCalculation(){
    if(current_driftCalculation){
        return 1;
    }else{
        return 0;
    }
}