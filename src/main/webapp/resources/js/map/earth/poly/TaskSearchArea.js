/* 
 * Java GIS INTERFACE
 * 1. clear clearTaskSearchArea().
 * 2. create_new createTaskSearchArea(id, enduranceSru, radiusType)
 * 3. view_from_database createTaskSearchArea(id, null, null, latlngs, pivot, width, height, tilt)
 * 4. remove removeTaskSearchArea(id)
 */

var TASK_SEARCH_AREA = new Hash();

function createTaskSearchAreaLoadDatabase(id, latlngs, pivot, width, height, tilt){
    latlngs.push(latlngs[0]);
    TASK_SEARCH_AREA.setItem(id, new TaskSearchArea(latlngs, pivot, width, height, tilt));
}

function createTaskSearchAreaCreateNew(id, width, height, unrotateStart, unrotatePivot){
    current_driftCalculation.subSearchAreaCollection.add(id, width, height, unrotateStart, unrotatePivot);
}

function removeTaskSearchArea(id){
    if(TASK_SEARCH_AREA.hasItem(id)){
        TASK_SEARCH_AREA.getItem(id).remove();
        TASK_SEARCH_AREA.removeItem(id);
    }
}



function clearTaskSearchArea(){
    for(i in TASK_SEARCH_AREA.items){
        TASK_SEARCH_AREA.getItem(i).remove();
    }
    TASK_SEARCH_AREA.clear();
}

function TaskSearchArea(latlngs, pivot, width, height, tilt){
    this.l = latlngs;
    this.pivot = pivot;
    this.width = width;
    this.height = height;
    this.tilt = tilt;
    this.draw();
}

TaskSearchArea.prototype.inFocus =  function(){
    map.setCenter(this.pivot);
}

TaskSearchArea.prototype.draw = function(){
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
    lineStyle.setWidth(5);
    lineStyle.getColor().set('ff00ff00');
    var polyColor =
    this.polygon.getStyleSelector().getPolyStyle().getColor();
    polyColor.setA(10);
    polyColor.setB(0);
    polyColor.setG(0);
    polyColor.setR(0);
    // Add the placemark to Earth.
    map.addOverlay(this.polygon);
    this.l.pop();
}

TaskSearchArea.prototype.remove = function(){
    map.removeOverlay(this.polygon);
}


TaskSearchArea.prototype.setSelected = function(flag){
    var lineStyle = this.polygon.getStyleSelector().getLineStyle();
    if(flag){
        lineStyle.getColor().set('ffff0000');
    }else{
        lineStyle.getColor().set('ff00ff00');
    }
}

var selectedTaskSearchArea;

function setSelectedTaskSearchArea(id){
    if(TASK_SEARCH_AREA.hasItem(id)){
        if(selectedTaskSearchArea){
            selectedTaskSearchArea.setSelected(false);
        }
        selectedTaskSearchArea = TASK_SEARCH_AREA.getItem(id);
        selectedTaskSearchArea.setSelected(true);
    }else if(id == 'Nothing'){
        for(i in TASK_SEARCH_AREA.items){
            TASK_SEARCH_AREA.getItem(i).setSelected(false);
        }
    }
}   