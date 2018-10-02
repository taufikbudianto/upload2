/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function ClusterManager(){
    this.collection = [];
}

// berbeda dengan ClusterManager.prototype.remove2 yang menjadikan Class Array menjadi Collector nya
ClusterManager.prototype.remove = function(keyInHash, hash){
    var object = hash.removeItem(keyInHash);
    clusterMarker.removeMarker(object.marker);
}


ClusterManager.prototype.clearSpecifiedHash = function(hash){
    var itemsw = hash.items;
    var markers = [];
    for(i in itemsw){
        markers.push(itemsw[i].marker);
    }
    hash.clear();
    clusterMarker.removeMarkers(markers);
}

//Class yang bisa mengunakan Method ini adalah class yang marker-nya di referensi kan sebagai variable
//marker.
//addMarkers([gObject.marker]); tidak langsung memproeses marker untuk tampil pada map
//program harus mengexekusi code clusterManager.refresh() dikarnakan jika penambahan dilakukan lebih dari satu kali diwaktu yang sama maka akan pemboroskan proses
ClusterManager.prototype.add = function(gObject){
    clusterMarker.addMarker(gObject.marker);
}

ClusterManager.prototype.refresh = function(){
    //SarCoreApp need this function
    clusterMarker.resetViewport();
    clusterMarker.redraw();
}