function FreeDefinePattern(start, latlngs, pivot){
    this.start = start;
    if(latlngs && latlngs.length > 1){
        this.l = latlngs;
        this.pivot = pivot;
        this.draw();
        this.initWaypointToolTip();
        if(latlngs.length > 2){
            this.reinitUndoButton();
        }
    }else{
        this.l = new Array();
        this.pivot = start;
        this.l.push(start);
        this.marker = new GMarker(start, {
            draggable : true, 
            dragCrossMove : true
        });
        var me = this;
        GEvent.addListener(this.marker, 'dragend', function(latlng){
            me.move(me.pivot, latlng)
        });
        GEvent.addListener(this.marker, "click", function(latlng){
            eBubble.openOnMarker(this, me.getContentString());
        });
        map.addOverlay(this.marker);
    }
}




FreeDefinePattern.prototype = new SearchPattern();


FreeDefinePattern.prototype.initFreeDefinePattern = function(start){
    this.l = new Array();
    this.pivot = start;
    this.l.push(start);
    this.marker = new GMarker(start, {
        draggable : true, 
        dragCrossMove : true
    });
    var me = this;
    GEvent.addListener(this.marker, 'dragend', function(latlng){
        me.move(me.pivot, latlng)
    });
    GEvent.addListener(this.marker, "click", function(latlng){
        eBubble.openOnMarker(this, me.getContentString());
    });
    map.addOverlay(this.marker);
}


FreeDefinePattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.start = this.l[0];
    this.pivot = to;
    if(this.poly && this.l.length >= 2){
        map.removeOverlay(this.poly);
        this.draw();
        if(this.undoMarker && this.l.length > 2){
            this.reinitUndoButton();
        }
    }else{
        this.marker.setLatLng(this.pivot);
    }
}



FreeDefinePattern.prototype.add = function(latlng){
    this.l.push(latlng);
    if(this.l.length > 2){
        this.poly.disableEditing();
        this.poly.enableEditing({
            maxVertices : this.poly.getVertexCount() + 1
        });
        this.poly.insertVertex(this.poly.getVertexCount() ,latlng);
        //agar posisi undo buton tidak bentrok dengan marker maka dibuat ketika polyline memiliki 3 point
        this.reinitUndoButton();
    }else{
        this.poly = new GPolyline(this.l, "#0000FF", 5, 0.5, {
            clickable : false
        });
        map.addOverlay(this.poly);
        this.poly.enableEditing({
            maxVertices : this.poly.getVertexCount()
        });
        var me = this;
        GEvent.addListener(this.poly,"lineupdated",function(){
            var updateVertex;
            for(i = 0; i < this.getVertexCount(); ++i){
                var vertex = this.getVertex(i);
                if(!me.l[i].equals(vertex)){
                    updateVertex = vertex;
                    me.l[i] = vertex;  	
                    if(i == 0){
                        me.startWaypoint.setLatLng(vertex);
                    }else if(i == (me.l.length - 1)){
                        me.endWaypoint.setLatLng(vertex);
                        if(me.l.length > 2) me.reinitUndoButton();
                    }else if(i == (me.l.length - 2) && me.l.length > 2){
                        me.reinitUndoButton();
                    }
                    me.repaintMarker();
                    break;
                }
            }
        }); 
    }
    this.initWaypointToolTip();
    this.repaintMarker();
}
			
                        
FreeDefinePattern.prototype.reinitUndoButton = function(){
    var lastPoint = this.l[this.l.length - 1];
    var bfrLstPoint = this.l[this.l.length - 2];
    //Validasi ketika garis free define terakhir diremove
    if(lastPoint && bfrLstPoint){
        var midLastPoint = midPointNotAccuracy(lastPoint, bfrLstPoint);
        if(!this.undoMarker){
            var icon = new GIcon();
            icon.image = MEASURE_UNDO_MARKER;
            icon.iconSize = new GSize(24, 24);
            icon.iconAnchor = new GPoint(16, 16);
            icon.infoWindowAnchor = new GPoint(25, 7);
            var opts = {
                "icon": icon,
                "clickable": true,
                "title" : 'undo'
            };
            this.undoMarker = new GMarker(midLastPoint, opts);
            map.addOverlay(this.undoMarker);
            var me = this;
            GEvent.addListener(this.undoMarker, 'click', function(){
                me.removeLast();
            });
        }else{
            this.undoMarker.setLatLng(midLastPoint);
        }
    }
}        


FreeDefinePattern.prototype.removeLast = function(){
    if(this.poly){
        this.poly.disableEditing();
        this.poly.deleteVertex(this.poly.getVertexCount() - 1); 
        this.l.pop();
        if(this.l.length > 1){
            this.poly.enableEditing({
                maxVertices : this.poly.getVertexCount() - 1
            });
            if(this.l.length > 2){
                this.reinitUndoButton();
            }else{
                if(this.undoMarker){
                    map.removeOverlay(this.undoMarker);
                    this.undoMarker = null;
                }
            }
            this.initWaypointToolTip();
        }else{
            if(this.poly){
                map.removeOverlay(this.poly);
                this.poly = null;
            }
            this.l.push(this.pivot);
            if(this.endWaypoint){
                map.removeOverlay(this.endWaypoint);
                this.endWaypoint = null;
            }
            if(this.startWaypoint){
                map.removeOverlay(this.startWaypoint);
                this.startWaypoint = null;
            }
        }
    }
    this.repaintMarker();
}



                        
FreeDefinePattern.prototype.repaintMarker = function(){
    var  bounds = this.poly.getBounds();
    if(bounds){
        this.marker.setLatLng(bounds.getCenter());
        this.pivot = bounds.getCenter();
    }
}
            

FreeDefinePattern.prototype.inFocus = function(){
    var mapSize = map.getSize();
    var polylineBounds = this.poly.getBounds();
    var maxZoom = map.getCurrentMapType().getBoundsZoomLevel(polylineBounds, mapSize);
    map.setCenter(this.marker.getLatLng(), maxZoom);
}



FreeDefinePattern.prototype.remove = function(){
    if(this.poly){
        map.removeOverlay(this.poly);
        this.poly = null;
    }
    if(this.marker){
        map.removeOverlay(this.marker);
        this.marker = null;
    }
    if(this.endWaypoint){
        map.removeOverlay(this.endWaypoint);
        this.endWaypoint = null;
    }
    if(this.startWaypoint){
        map.removeOverlay(this.startWaypoint);
        this.startWaypoint = null;
    }
    if(this.undoMarker){
        map.removeOverlay(this.undoMarker);
        this.undoMarker = null;
    }
}



FreeDefinePattern.prototype.draw = function(){
    var me;
    this.poly = new GPolyline(this.l, "#0000FF", 5, 0.5, {
        clickable : false
    });
    map.addOverlay(this.poly);
    this.poly.enableEditing({
        maxVertices : this.poly.getVertexCount()
    });
    me = this;
    GEvent.addListener(this.poly,"lineupdated",function(){
        var updateVertex;
        for(i = 0; i < this.getVertexCount(); ++i){
            var vertex = this.getVertex(i);
            if(!me.l[i].equals(vertex)){
                updateVertex = vertex;
                me.l[i] = vertex;
                if(i == 0){
                    me.startWaypoint.setLatLng(vertex);
                }else if(i == (me.l.length - 1)){
                    me.endWaypoint.setLatLng(vertex);
                    if(me.l.length > 2) me.reinitUndoButton();
                }else if(i == (me.l.length - 2) && me.l.length > 2){
                    me.reinitUndoButton();
                }
                me.repaintMarker();
                break;
            }
        }
    });
    if(this.marker){
        this.marker.setLatLng(this.pivot);
    }else{
        this.marker = new GMarker(this.pivot, {
            draggable : true, 
            dragCrossMove : true
        });
        me = this;
        GEvent.addListener(this.marker, 'dragend', function(latlng){
            me.move(me.pivot, latlng);
        });
        GEvent.addListener(this.marker, "click", function(latlng){
            eBubble.openOnMarker(this, me.getContentString());
        });
        markerAdditionalEventForMeasure(this.marker);
        map.addOverlay(this.marker);
    }
    this.initWaypointToolTip();
}


FreeDefinePattern.prototype.getContentString = function(){
    var contentString  = "<b class='title'><u>Free Define Search</u></b>";
    if(this.poly){
        contentString += "<br><b class='field'>Length</b> : <font class='value'>" + this.getContentStringOfLength() + "</font>";
    }else{
        contentString += "<br><b class='field'>Length</b> : <font class='value'>0 KM(0 NM)</font>";
    }
    return contentString;
}



function setCurrentFreeDefine(obj){
    currentFreeDefine = obj;
}

