
/*
 * @param pivot titik pusat dari SearchArea yang memiliki instance dari class ini
 * @param radius radius dari SearchArea
 * @param tilt kemiringan dari searchArea
 */

//from DriftCalculation.createSubSearchArea
function SubSearchAreaCollection(pivot, radius, tilt){
    this.pivot = pivot;
    this.radius = radius;
    this.tilt = tilt;
    this.subSearchAreaCount = 0;
}

 // from TaskSearchArea.createTaskSearchAreaCreateNew
SubSearchAreaCollection.prototype.add = function(id, width, height, unrotateStart, unrotatePivot){
    var vertexs = new Array();
    vertexs.push(unrotateStart);
    vertexs.push(new GLatLng(vertexs[0].lat(), vertexs[0].lng() + (ONE_NM_TO_DEG *width)));
    vertexs.push(new GLatLng(vertexs[1].lat() - (ONE_NM_TO_DEG *height), vertexs[1].lng()));
    vertexs.push(new GLatLng(vertexs[2].lat(), vertexs[0].lng()));
    vertexs.push(unrotateStart);
    var pivot = rotationGLatLng(this.pivot, unrotatePivot, this.tilt);
    rotationGLatLngs(vertexs, this.pivot, this.tilt);
    
    //instantiate,  put to hash new TaskSearchArea
    TASK_SEARCH_AREA.setItem(id, new TaskSearchArea(vertexs, pivot, width, height, this.tilt));
}

