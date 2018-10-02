/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//MarkerWithLabel change content

function changeMarkerWithLabelContent(marker, content){
    marker.labelContent = content;
    marker.label.labelDiv_.innerHTML = content;
    marker.label.eventDiv_.innerHTML = marker.label.labelDiv_.innerHTML;
}


//COORDINAT
function rotationCoordinat(poros, pi, degree){
    var x_ = poros.x , y_ = poros.y,
    x = pi.x, y = pi.y,
    xGenerate, yGenerate,
    cos , sin;

    if(((toDeg(toRad(degree)))/90)%2 == 1)
        cos = 0;
    else
        cos = Math.cos(toRad(degree));
    sin = Math.sin(toRad(degree));

    xGenerate = (x*cos)-(y*sin)+(x_)-(x_*cos) + (y_*sin);
    yGenerate = (x*sin) + (y*cos) + (y_)-(x_*sin) - (y_*cos);

    return new GPoint(xGenerate, yGenerate);
}





function rotationLatLng(pivot, pi, degree){
    var pointOfPivot = map.fromLatLngToContainerPixel(pivot); 
    var pointOfPi = map.fromLatLngToContainerPixel(pi);
    return map.fromContainerPixelToLatLng(rotationCoordinat(pointOfPivot, pointOfPi, degree));
}

function latlngsToPoints(latlngs){
    var points = new Array();
    for(i in latlngs){
        points.push(map.fromLatLngToContainerPixel(latlngs[i]));
    }
    return points;
}

function pointsToLatlngs(points){
    var latlngs = new Array();
    for(i in points){
        latlngs.push(map.fromContainerPixelToLatLng(points[i]));
    //        latlngs.push(map.fromContainerPixelToLatLng(points[i]));
    }
    return latlngs;
}

/**
 *this function will change value of any item in points array. without you reference it to variable
 **/
function rotatePoints(center, points, angle){
    var point;
    for(i in points){
        point = rotationCoordinat(center, points[i], angle);
        points[i] = new GPoint(point.x , point.y);
    }
}

function getChangedVertex(poly, l){
    for(i in l){
        var vertex = poly.getVertex(i);
        if(!l[i].equals(vertex))
            return {
                from : l[i], 
                to : vertex
            };
    }
    return {
        from : -1, 
        to : -1
    };
}

function setMapFocus(boundsLatLng, center){
    var mapSize = map.getSize();
    var maxZoom = map.getCurrentMapType().getBoundsZoomLevel(boundsLatLng, mapSize);
    map.setCenter(center, maxZoom);
}

/**
 *@param value orginal String
 *@return string yang Font-nya telah disesuaikan dengan tampilan Map. agar dapat
 *        terlihat dengan baik
 */

function stringHtmlOnMap(value){
    if(map.getMapTypeId() == google.maps.MapTypeId.ROADMAP || map.getMapTypeId() == google.maps.MapTypeId.TERRAIN){
        return value.bold().fontsize(2).fontcolor('black');
    }else{
        return value.bold().fontsize(2).fontcolor('white');
    }
}



function markerAdditionalEventForMeasure(marker){
//    if(MODE == 'MEASURE'){
//        GEvent.addListener(marker, 'click', function(latlng){
//            if(polylineMeasure){
//                polylineMeasure = polylineMeasure.add(latlng);
//            }else{
//                polylineMeasure = new MeasurePolyline(latlng);
//            } 
//        });
//    }
}
