

var LayerCollection = new Hash();

function createESRILayer(id,url,opacity) {
    //TODO LAYER ESRI FOR Google Earth API 
}


function createGoogleKML(id,url) {
    google.earth.fetchKml(ge, url, function(object){
        if(object){
            LayerCollection.setItem(id, object);
            map.addOverlay(object);
        }
    });
}

function removeLayer(id){
    if(LayerCollection.hasItem(id)){
        map.removeOverlay(LayerCollection.getItem(id));
    }
}

