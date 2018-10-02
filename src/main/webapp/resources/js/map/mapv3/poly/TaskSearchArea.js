/* 
 * Java GIS INTERFACE
 * 1. clear clearTaskSearchArea().
 * 2. create_new createTaskSearchArea(id, enduranceSru, radiusType)
 * 3. view_from_database createTaskSearchArea(id, null, null, latlngs, pivot, width, height, tilt)
 * 4. remove removeTaskSearchArea(id)
 */


var TASK_SEARCH_AREA = new Hash();

function createTaskSearchAreaLoadDatabase(id, latlngs, pivot, width, height, tilt){
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

TaskSearchArea.prototype.draw = function(){
    this.polygon = new google.maps.Polygon({
                clickable: false,
                paths: this.l,
                strokeColor: "#00FF00",
                strokeOpacity: 1,
                strokeWeight: 3,
                fillColor: "#000000",
                fillOpacity: 0.1
            });
    map.addOverlay(this.polygon);
}

TaskSearchArea.prototype.remove = function(){
    map.removeOverlay(this.polygon);
}

TaskSearchArea.prototype.inFocus =  function(){
    map.setCenter(this.pivot);
}

//1.TASK_SEARCH_AREA_TODO
TaskSearchArea.prototype.setSelected = function(flag){
    if(flag){
        this.polygon.setOptions({
            strokeColor : '#0000ff',
            strokeWeight : 5,
            strokeOpacity : 1.0
        });
        /*
        this.polygon.setStrokeStyle({
            color : '#0000ff', 
            weight : 3, 
            opacity : 1
        });
        */
    }else{
        this.polygon.setOptions({
            strokeColor : '#00ff00',
            strokeWeight : 5,
            strokeOpacity : 1.0
        });
        /*
        this.polygon.setStrokeStyle({
            color : '#00ff00', 
            weight : 3, 
            opacity : 1
        });
        */
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