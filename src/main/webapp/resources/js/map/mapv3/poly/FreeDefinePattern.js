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
        this.initFreeDefinePattern(start);
    }
}




FreeDefinePattern.prototype = new SearchPattern();


FreeDefinePattern.prototype.initFreeDefinePattern = function(start){
    this.l = new Array();
    this.pivot = start;
    this.l.push(start);
    this.marker = new google.maps.Marker({
        position : start,
        draggable : true, 
        crossOnDrag : true
    });
    var me = this;
    google.maps.event.addListener(this.marker, 'dragend', function(mouseevent){
        me.move(me.pivot, mouseevent.latLng);
    });
    google.maps.event.addListener(this.marker, "click", function(mouseevent){
        eBubble.openOnMarker(this, me.getContentString());
    });
    map.addOverlay(this.marker);
}


FreeDefinePattern.prototype.move = function(from, to){
    this.moving(from, to);
    this.start = this.l[0];
    this.pivot = to;
    if(this.poly && this.l.length >= 2){
        this.poly.setEditable(false);
        //map.removeOverlay(this.poly);
        this.poly.setPath(this.l);
        this.initPolylineListener();
        this.poly.setEditable(true);
        //this.draw();
        if(this.undoMarker && this.l.length > 2){
            this.reinitUndoButton();
        }
    }else{
        this.marker.setLatLng(this.pivot);
    }
}



FreeDefinePattern.prototype.add = function(latlng){
    if(this.l.length >= 2){
        this.poly.getPath().insertAt(this.poly.getPath().getArray().length, latlng);
    }else{
        this.l.push(latlng);
        this.poly = new google.maps.Polyline({
            strokeColor : "#0000FF",
            clickable : false,
            path : this.l,
            strokeWeight : 5,
            strokeOpacity : 0.5,
            map : map,
            editable : true
        });
        this.initPolylineListener();
        this.initWaypointToolTip();
        this.repaintMarker();
    }
}
			
                        
FreeDefinePattern.prototype.reinitUndoButton = function(){
    var lastPoint = this.l[this.l.length - 1];
    var bfrLstPoint = this.l[this.l.length - 2];
    //Validasi ketika garis free define terakhir diremove
    if(lastPoint && bfrLstPoint){
        var midLastPoint = midPointNotAccuracy(lastPoint, bfrLstPoint);
        if(!this.undoMarker){
            this.undoMarker = CustomIconMarker(MEASURE_UNDO_MARKER, midLastPoint, 'Undo');
            map.addOverlay(this.undoMarker);
            var me = this;
            google.maps.event.addListener(this.undoMarker, 'click', function(){
                me.removeLast();
            });
        }else{
            this.undoMarker.setLatLng(midLastPoint);
        }
    }
}        


FreeDefinePattern.prototype.removeLast = function(){
    if(this.poly){
        this.poly.getPath().pop();
        if(this.l.length > 1){
            if(this.l.length > 2){
                this.reinitUndoButton();
            }else{
                if(this.undoMarker){
                    map.removeOverlay(this.undoMarker);
                    this.undoMarker = null;
                }
            }
            this.initWaypointToolTip();
            this.repaintMarker();
        }else{
            map.removeOverlay(this.poly);
            this.poly = null;
            this.l.push(this.pivot);
            if(this.endWaypoint){
                map.removeOverlay(this.endWaypoint);
                this.endWaypoint = null;
            }
            if(this.startWaypoint){
                map.removeOverlay(this.startWaypoint);
                this.startWaypoint = null;
            }
            this.marker.setLatLng(this.pivot);
        }
    }
}



                        
FreeDefinePattern.prototype.repaintMarker = function(){
    var  bounds = this.poly.getBounds();
    if(bounds){
        this.marker.setLatLng(bounds.getCenter());
        this.pivot = bounds.getCenter();
    }
}
            

FreeDefinePattern.prototype.inFocus = function(){
    map.fitBounds(this.poly.getBounds());
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



FreeDefinePattern.prototype.initPolylineListener = function(){
    var me = this;
    google.maps.event.addListener(this.poly.getPath(), "insert_at", function(index){
        me.startWaypoint.setLatLng(me.l[0]);
        me.endWaypoint.setLatLng(me.l[me.l.length - 1]);
        me.l = me.poly.getPath().getArray();
        me.reinitUndoButton();
        me.repaintMarker(); 
    });
    google.maps.event.addListener(this.poly.getPath(), "remove_at", function(index, latlng){
        me.startWaypoint.setLatLng(me.l[0]);
        me.endWaypoint.setLatLng(me.l[me.l.length - 1]);
        me.l = me.poly.getPath().getArray();
        me.reinitUndoButton();
        me.repaintMarker(); 
    });
    google.maps.event.addListener(this.poly.getPath(index, latlng), "set_at", function(index, latlng){
        me.startWaypoint.setLatLng(me.l[0]);
        me.endWaypoint.setLatLng(me.l[me.l.length - 1]);
        me.l = me.poly.getPath().getArray();
        me.reinitUndoButton();
        me.repaintMarker(); 
    });
}
    
    
    
FreeDefinePattern.prototype.draw = function(){
    this.poly = new google.maps.Polyline({
        strokeColor : "#0000FF",
        clickable : false,
        path : this.l,
        strokeWeight : 5,
        strokeOpacity : 0.5,
        map : map,
        editable : true
    });
    
    this.initPolylineListener();
    
    if(this.marker){
        this.marker.setLatLng(this.pivot);
    }else{
        this.marker = new google.maps.Marker({
            position : start,
            draggable : true, 
            crossOnDrag : true
        });
        me = this;
        google.maps.event.addListener(this.marker, 'dragend', function(mouseevent){
            me.move(me.pivot, mouseevent.latLng);
            me.l = this.getPath().getArray();
        });
        google.maps.event.addListener(this.marker, "click", function(mouseevent){
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

