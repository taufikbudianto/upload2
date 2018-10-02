/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* tidak dipakai lagi...
 * 
 function ESRIDynamic(){
    return new esri.arcgis.gmaps.DynamicMapServiceLayer
        ("http://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Demographics/ESRI_Population_World/MapServer",
        null, 0.75); 
}
*/

var LayerCollection = new Hash();

function createESRILayer(id,url,opacity) {
    try{
        var layer = new gmaps.ags.MapOverlay(url);
        LayerCollection.setItem(id, layer);
        map.addOverlay(layer); 
    }catch(err){
    }    
}


function createGoogleKML(id,url) {
    try{
        var layer = new google.maps.KmlLayer({
            url : url,
            clickable : false
        });
        LayerCollection.setItem(id, layer);
        map.addOverlay(layer);
    }catch(err){
    }    
}

function removeLayer(id){
    if(LayerCollection.hasItem(id)){
        map.removeOverlay(LayerCollection.getItem(id));
    }
}

