

function createMarker(name, latlng, urlImage, size){
    
    var marker = ge.createPlacemark('');
    marker.setName(name);
    
    var point = ge.createPoint('');
    point.setLatitude(latlng.lat());
    point.setLongitude(latlng.lng());   
    marker.setGeometry(point);
    var icon, style;
    if(urlImage){
        icon = ge.createIcon('');
        icon.setHref(urlImage);
        style = ge.createStyle(''); //create a new style
        style.getIconStyle().setIcon(icon); //apply the icon to the style
        marker.setStyleSelector(style); //apply the style to the placemark
    }
    if(size){
        if(marker.getStyleSelector()){
            marker.getStyleSelector().getIconStyle().setScale(size);
        }else{
            style = ge.createStyle(''); //create a new style
            style.getIconStyle().setScale(size);
            marker.setStyleSelector(style);
        }
    }
    markerAdditionalEventForMeasure(marker);
    return marker;
}


function createTooltipMarker(stringTooltip, latlng){
    var marker = ge.createPlacemark('');
    marker.setName('');
    var point = ge.createPoint('');
    point.setLatitude(latlng.lat());
    point.setLongitude(latlng.lng());
    marker.setGeometry(point);
    var icon = ge.createIcon('');
    icon.setHref(TRANSPARANT_ICON);
    var style = ge.createStyle(''); //create a new style
    style.getIconStyle().setIcon(icon); //apply the icon to the style
    style.getIconStyle().setScale(0.3);
    marker.setStyleSelector(style); //apply the style to the placemark
    marker.setDescription(stringTooltip);
    google.earth.addEventListener(marker, 'mouseover', function(event) {
        this.setName(marker.getDescription());
    });
    google.earth.addEventListener(marker, 'mouseout', function(event) {
        this.setName('');
    });
    markerAdditionalEventForMeasure(marker);
    return marker;
}


function ballonForMarker(marker, htmlString){
    var balloon = ge.createHtmlStringBalloon('');
    balloon.setFeature(marker); // optional
    balloon.setContentString(htmlString);
    google.earth.addEventListener(marker, 'click', function(event) {
        event.preventDefault();
        ge.setBalloon(balloon);
    });
}


function createPolyline(vertexs, color, weight){
    var lineStringPlacemark = ge.createPlacemark('');
    var lineString = ge.createLineString('');
    for(i in vertexs){
        lineString.getCoordinates().pushLatLngAlt(vertexs[i].lat(), vertexs[i].lng(), 700);
    }
    lineString.setAltitudeMode(ge.ALTITUDE_CLAMP_TO_GROUND);
    lineString.setTessellate(true);
    if(color && weight){
        lineStringPlacemark.setStyleSelector(ge.createStyle(''));
        var lineStyle = lineStringPlacemark.getStyleSelector().getLineStyle();
        if(color){
            lineStyle.getColor().set(color);
        }
        if(weight){
            lineStyle.setWidth(weight);
        }
    }
    lineStringPlacemark.setGeometry(lineString);
    return lineStringPlacemark;
}


// bodyColorOption java script object literal {alpha:0,red:255,green:255,blue:255}
function createPolygon(latlngs, strokeColor, bodyColorOption){
    var result = ge.createPlacemark('');

    // Create the polygon.
    var polygon = ge.createPolygon('');
    polygon.setAltitudeMode(ge.ALTITUDE_CLAMP_TO_GROUND);
    result.setGeometry(polygon);

    // Add points for the outer shape.
    var outer = ge.createLinearRing('');
    for(i in latlngs){
        outer.getCoordinates().pushLatLngAlt(latlngs[i].lat(), latlngs[i].lng(), 700);
    }
    polygon.setOuterBoundary(outer);
    //Create a style and set width and color of line
    result.setStyleSelector(ge.createStyle(''));
    var lineStyle = result.getStyleSelector().getLineStyle();
    lineStyle.setWidth(5);
    lineStyle.getColor().set(strokeColor);
    if(bodyColorOption){
        var polyColor = result.getStyleSelector().getPolyStyle().getColor();
        polyColor.setA(bodyColorOption.alpha);
        polyColor.setB(bodyColorOption.blue);
        polyColor.setG(bodyColorOption.green);
        polyColor.setR(bodyColorOption.red);
    }
}



function createScreenOverlay(imageUrl, x, y, width, height){
    var screenOverlay = ge.createScreenOverlay('');
    screenOverlay.setIcon(ge.createIcon(''));
    screenOverlay.getIcon().
    setHref(imageUrl);

    // Set the point inside the overlay that is used as the positioning
    // anchor point.
    screenOverlay.getOverlayXY().setXUnits(ge.UNITS_PIXELS);
    screenOverlay.getOverlayXY().setYUnits(ge.UNITS_PIXELS);
    screenOverlay.getOverlayXY().setX(y);
    screenOverlay.getOverlayXY().setY(x);

    // Set screen position in fractions.
    screenOverlay.getScreenXY().setXUnits(ge.UNITS_PIXELS);
    screenOverlay.getScreenXY().setYUnits(ge.UNITS_PIXELS);
    screenOverlay.getScreenXY().setX(0);
    screenOverlay.getScreenXY().setY(0);


    // Set object's size in pixels.
    screenOverlay.getSize().setXUnits(ge.UNITS_PIXELS);
    screenOverlay.getSize().setYUnits(ge.UNITS_PIXELS);
    screenOverlay.getSize().setX(width);
    screenOverlay.getSize().setY(height);

    return screenOverlay;  
}


function createScreenOverlayFromButtomLeft(imageUrl, fractionX, fractionY, width, height){
    var screenOverlay = ge.createScreenOverlay('');
    screenOverlay.setIcon(ge.createIcon(''));
    screenOverlay.getIcon().
    setHref(imageUrl);
    // Set the point inside the overlay that is used as the positioning
    // anchor point.
    screenOverlay.getOverlayXY().setXUnits(ge.UNITS_FRACTION);
    screenOverlay.getOverlayXY().setYUnits(ge.UNITS_FRACTION);
    screenOverlay.getOverlayXY().setX(fractionX);
    screenOverlay.getOverlayXY().setY(fractionY);

    // Set screen position in fractions.
    screenOverlay.getScreenXY().setXUnits(ge.UNITS_FRACTION);
    screenOverlay.getScreenXY().setYUnits(ge.UNITS_FRACTION);
    screenOverlay.getScreenXY().setX(0);  // Random x.
    screenOverlay.getScreenXY().setY(0);  // Random y.

    // Set object's size in pixels.
    screenOverlay.getSize().setXUnits(ge.UNITS_PIXELS);
    screenOverlay.getSize().setYUnits(ge.UNITS_PIXELS);
    screenOverlay.getSize().setX(width);
    screenOverlay.getSize().setY(height);
    
    return screenOverlay;
}

function createCirclePolygon(lat, lng, radius) {
    //    function makeCircle(radius, lng, lat) {
    //
    //        var ring = ge.createLinearRing('');
    //        var steps = 41;
    //        var pi2 = Math.PI * 2;
    //        for (var i = 0; i < steps; i++) {
    //            var lat1 = lat + radius * Math.cos(i / steps * pi2);
    //            var lng1 = lng + radius * Math.sin(i / steps * pi2);
    //            ring.getCoordinates().pushLatLngAlt(lat1, lng1, 0);
    //            console.log(lat1+ " - "+ lng1);
    //        }
    //        return ring;
    //    }
    var ring = ge.createLinearRing('');
    // Degrees to radians 
    var d2r = Math.PI / 180;
    // Radians to degrees
    var r2d = 180 / Math.PI;
    // Earth radius is 3,963 miles => 6399.592 Km
    var cLat = (radius / 6378.8) * r2d;
    var cLng = cLat / Math.cos(lat * d2r);
 
    // Store points in array 
    var points = [];
 
    // Calculate the points
    // Work around 360 points on circle
    //for (var i=0; i < 360; i++) {
    for (var i=-1; i < 41; i++) {
        //var theta = Math.PI * (i/16);
        var theta = Math.PI * (i/(41/2));
        // Calculate next X point
        var circleX = lng + (cLng * Math.cos(theta));            
        // Calculate next Y point 
        var circleY = lat + (cLat * Math.sin(theta));
        // Add point to array 
        ring.getCoordinates().pushLatLngAlt(circleY, circleX, 0);
    //points.push(new GLatLng(circleX, circleY));
    }




    var polygonPlacemark = ge.createPlacemark('');
    var polygon = ge.createPolygon('');
    polygon.setAltitudeMode(ge.ALTITUDE_CLAMP_TO_GROUND);
    polygon.setTessellate(true);
    polygonPlacemark.setGeometry(polygon);
    var outer = ge.createLinearRing('');
    polygonPlacemark.getGeometry().setOuterBoundary(ring);
    polygonPlacemark.setStyleSelector(ge.createStyle(''));
    var lineStyle = polygonPlacemark.getStyleSelector().getLineStyle();
    lineStyle.setWidth(10);
    lineStyle.getColor().set('ff0000ff');
    var polyColor = polygonPlacemark.getStyleSelector().getPolyStyle().getColor();
    polyColor.setA(10);
    polyColor.setB(0);
    polyColor.setG(0);
    polyColor.setR(0);
    return polygonPlacemark;
}





function markerAdditionalEventForMeasure(marker){
//    google.earth.addEventListener(this.marker, 'click', function(event) {
//        if(MODE =='MEASURE'){
//            var latlng = new GLatLng(event.getLatitude(), event.getLongitude());
//            GEvent.addListener(marker, 'click', function(latlng){
//                if(polylineMeasure){
//                    polylineMeasure = polylineMeasure.add(latlng);
//                }else{
//                    polylineMeasure = new MeasurePolyline(latlng);
//                } 
//            });
//        }
//    });
}

