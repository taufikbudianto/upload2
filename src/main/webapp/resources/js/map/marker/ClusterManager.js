/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function ClusterManager(){
    this.collection = [];
}

// berbeda dengan ClusterManager.prototype.remove2 yang menjadikan Class Array menjadi Collector nya
ClusterManager.prototype.remove = function(keyInHash, hash){
    clusterMarker.removeMarkers();
    hash.removeItem(keyInHash);
    this.reCusteringMarker();
}

// berbeda dengan ClusterManager.prototype.remove yang menjadikan Class Hash menjadi Collector nya
ClusterManager.prototype.remove2 = function(array, index){
    clusterMarker.removeMarkers();
    array[index] = null;
    this.reCusteringMarker();
}

ClusterManager.prototype.clearSpecifiedHash = function(hash){
    clusterMarker.removeMarkers();
    hash.clear();
    this.reCusteringMarker();
}



ClusterManager.prototype.reCusteringMarker = function(){
    var markerArray;
    if(ASSET.length > 0){
        markerArray = new Array();
        for(i in ASSET.items){
            if(ASSET.items[i].marker.visible) markerArray.push(ASSET.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(COMPOSITE.length > 0){
        markerArray = new Array();
        for(i in COMPOSITE.items){
            //one object beacon composite have 2 marker are : 1. dopplder marker and gps marker
            if(COMPOSITE.items[i].dopplerMarker.marker.visible) markerArray.push(COMPOSITE.items[i].dopplerMarker.marker);
            if(COMPOSITE.items[i].gpsMarker){
                if(COMPOSITE.items[i].gpsMarker.marker.visible) markerArray.push(COMPOSITE.items[i].gpsMarker.marker);
            }
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(ELEMENTAL.length > 0){
        markerArray = new Array();
        for(i in ELEMENTAL.items){
            if(ELEMENTAL.items[i].marker.visible) markerArray.push(ELEMENTAL.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(INCIDENT.length > 0){
        markerArray = new Array();
        for(i in INCIDENT.items){
            if(INCIDENT.items[i].marker.visible) markerArray.push(INCIDENT.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(INC_ASSET.length > 0){
        markerArray = new Array();
        for(i in INC_ASSET.items){
            if(INC_ASSET.items[i].marker.visible) markerArray.push(INC_ASSET.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(INC_POTENCY.length > 0){
        markerArray = new Array();
        for(i in INC_POTENCY.items){
            if(INC_POTENCY.items[i].marker.visible) markerArray.push(INC_POTENCY.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(KANSAR.length > 0){
        markerArray = new Array();
        for(i in KANSAR.items){
            if(KANSAR.items[i].marker.visible) markerArray.push(KANSAR.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(LOG.length > 0){
        markerArray = new Array();
        for(i in LOG.items){
            if(LOG.items[i].marker.visible) markerArray.push(LOG.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(POB.length > 0){
        markerArray = new Array();
        for(i in POB.items){
            if(POB.items[i].marker.visible) markerArray.push(POB.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(POSSAR.length > 0){
        markerArray = new Array();
        for(i in POSSAR.items){
            if(POSSAR.items[i].marker.visible) markerArray.push(POSSAR.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(POTENCY.length > 0){
        markerArray = new Array();
        for(i in POTENCY.items){
            if(POTENCY.items[i].marker.visible) markerArray.push(POTENCY.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
    if(SIGHTING.length > 0){
        markerArray = new Array();
        for(i in SIGHTING.items){
            if(SIGHTING.items[i].marker.visible) markerArray.push(SIGHTING.items[i].marker);
        }
        clusterMarker.addMarkers(markerArray);
    }
}

//Class yang bisa mengunakan Method ini adalah class yang marker-nya di referensi kan sebagai variable
//marker.
//addMarkers([gObject.marker]); tidak langsung memproeses marker untuk tampil pada map
//program harus mengexekusi code clusterManager.refresh() dikarnakan jika penambahan dilakukan lebih dari satu kali diwaktu yang sama maka akan pemboroskan proses
ClusterManager.prototype.add = function(gObject){
    clusterMarker.addMarkers([gObject.marker]);
}

ClusterManager.prototype.refresh = function(){
    eBubble.hide();
    map.closeInfoWindow();
    clusterMarker.refresh(true);
}


ClusterManager.prototype.refreshCauseRemoveOne = function(){
    clusterMarker = new ClusterMarker(map, {
        clusterMarkerClick:newClusterMarkerClick,
        markers:cluserMarker._mapMarkers
        });
    clusterMarker.refresh(true);
}