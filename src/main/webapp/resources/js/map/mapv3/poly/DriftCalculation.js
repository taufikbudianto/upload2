/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var current_driftCalculation;
var AUTO_GENERATE_STATE = 0; //1 untuk true dan 0 untuk false

function createCurrentDriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angleLeeway, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine){
    current_driftCalculation = new DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angleLeeway, parrentID, lkpToDatumDistance, lkpToDatumAngle);
    removeAndSetCurrentSearchArea(current_driftCalculation.searchArea);
}



/**
 *this methode be interface to the SarCoreApp
 */
function removeAndCreateCurrentSearchAreaFreeDefine(centerCoordinate, radius){
    if(current_driftCalculation){
        current_driftCalculation.remove();
    }
    
    removeAndSetCurrentSearchArea(current_driftCalculation.searchArea);
    AUTO_GENERATE_STATE = 1;
}



/**
 *this methode be interface to the SarCoreApp
 */
function removeAndCreateCurrentDriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angleLeeway, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine){
    if(current_driftCalculation){
        current_driftCalculation.remove();
    }
    current_driftCalculation = new DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angleLeeway, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine);
    removeAndSetCurrentSearchArea(current_driftCalculation.searchArea);
    AUTO_GENERATE_STATE = 1;
}




function removeCurrentDriftCalculation(){
    if(current_driftCalculation){
        current_driftCalculation.remove();
        setCurrentSearchArea(null);
        current_driftCalculation = null;
        AUTO_GENERATE_STATE = 0;
    }
}






function DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angleLeeway, parrentID, lkpToDatumDistance, lkpToDatumAngle, shape, drawLine){
    this.lkp = lkp;
    this.datum = datum;
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
    this.parrentID = parrentID;
    map.setCenter(datum);
    map.setZoom(15);
    this.leftLeeway = leftLeeway;
    this.rightLeeway = rightLeeway;
    this.radius = radius;
    this.angleLeeway = angleLeeway;
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
    var me = this;
    if(this.drawLine){
        this.lkpMarker = CustomLabelMarker(DRIFTCALCULATION_LKP_DEFAULT_SYMBOL_GIS, this.lkp, "<h5>LKP</h5>");
        google.maps.event.addListener(this.lkpMarker, "click", function() {
            var infoPopup = 
            "<b class='title'><u>Last Known Position</u></b>"+
            "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.getLatLng().lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.getLatLng().lng(), LONGITUDE_MODE) + "</font>" +
            "<br><b class='field'>LKP-DATUM Distance</b>   : <font class='value'>" + me.distanceLKPtoDatum + "</font>" +
            "<br><b class='field'>LKP-DATUM Angle</b>   : <font class='value'>" + me.lkpToDatumAngle + "°" + "</font>";
            eBubble.openOnMarker(this, infoPopup);
        });
        map.addOverlay(this.lkpMarker);
        this.lkpToDatumLine = new google.maps.Polyline({
            strokeColor : "#FFFFFF",
            path : [this.lkp , this.datum],
            clickable : false,
            strokeWeight : 2, 
            strokeOpacity : 1
        });
        map.addOverlay(this.lkpToDatumLine);
        var lineSymbol = {
            path: 'M 0,-1 0,1',
            strokeOpacity: 1,
            scale: 4
        };
        this.lkpToleftLeeway = new google.maps.Polyline({
            strokeColor : "#FF0000",
            path : [this.lkp , this.leftLeeway],
            clickable : false,
            strokeWeight : 0, 
            strokeOpacity : 1,
            icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '20px'
            }]
        });
        map.addOverlay(this.lkpToleftLeeway);
        this.lkpToRigthLeeway = new google.maps.Polyline({
            strokeColor : "#FF0000",
            path : [this.lkp , this.rightLeeway],
            clickable : false,
            strokeWeight : 0, 
            strokeOpacity : 1,
            icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '20px'
            }]
        });
        map.addOverlay(this.lkpToRigthLeeway);
    }
    this.searchArea = new SearchArea(null, null, {
        tilt : this.angleLeeway , 
        pivot : this.datum, 
        radius : this.radius
    }, null, this.parrentID, this.shape, this.radius);
    if(this.drawLine){
        this.lineLeftToRightLeeway = new google.maps.Polyline({
            strokeColor : "#FF0000",
            path : [this.leftLeeway, this.rightLeeway],
            clickable : false,
            strokeWeight : 0, 
            strokeOpacity : 1,
            icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '20px'
            }]
        });
        map.addOverlay(this.lineLeftToRightLeeway);
    }
    this.datumMarker = CustomIconMarker(DRIFTCALCULATION_DATUM_DEFAULT_SYMBOL_GIS, this.datum);
    google.maps.event.addListener(this.datumMarker, "click", function() {
        var infoPopup;
        if(me.drawLine){
            infoPopup = 
            "<b class='title'><u>Datum Position</u></b>"+
            "<br><b class='f    ield'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.getLatLng().lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.getLatLng().lng(), LONGITUDE_MODE) + "</font>" + 
            "<br><b class='field'>DATUM-LKP Distance</b>   : <font class='value'>" + me.distanceLKPtoDatum + "</font>" +
            "<br><b class='field'>DATUM-LKP Angle</b>   : <font class='value'>" + me.datumToLkpAngle + "°" + "</font>";
        }else{
            infoPopup = 
            "<b class='title'><u>Datum Position</u></b>"+
            "<br><b class='field'>Latitude</b>    : <font class='value'>" + deg_to_dms(this.getLatLng().lat(), LATITUDE_MODE) + "</font>" +
            "<br><b class='field'>Longitude</b>   : <font class='value'>" + deg_to_dms(this.getLatLng().lng(), LONGITUDE_MODE) + "</font>" + 
            "<br><b class='field'>Radius Pencarian</b>   : <font class='value'>" + me.radius + "</font>";
        }
        eBubble.openOnMarker(this, infoPopup);
    });
    map.addOverlay(this.datumMarker);
    map.fitBounds(this.searchArea.getBounds());
}


DriftCalculation.prototype.getLkp = function(){
    return this.lkp;
}

DriftCalculation.prototype.getAngleLeeway = function(){
    return this.angleLeeway;
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
    if(this.lineLeftToRightLeeway) map.removeOverlay(this.lineLeftToRightLeeway);
    this.searchArea.remove();
    if(this.lkpToRigthLeeway) map.removeOverlay(this.lkpToRigthLeeway);
    if(this.lkpToleftLeeway) map.removeOverlay(this.lkpToleftLeeway);
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