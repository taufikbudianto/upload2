/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



var LayerCollection = new Hash();

function createESRILayer(id,url,opacity) {
    var layer = new esri.arcgis.gmaps.DynamicMapServiceLayer(url,null, opacity);
    LayerCollection.setItem(id, layer);
    map.addOverlay(layer); 
}


function createGoogleKML(id,url) {
    //Fecth KML for FusionTable . notes: GFusionMap instance doesn't support GeoXml class 
}

function removeLayer(id){
    if(LayerCollection.hasItem(id)){
        map.removeOverlay(LayerCollection.getItem(id));
    }
}

