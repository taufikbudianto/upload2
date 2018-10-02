
function addPolylineSearchArea(color,startLat,startLng,endLat,endLng) {
    var map = PF('idnGmap').getMap();
    // Define a symbol using SVG path notation, with an opacity of 1.
    var lineSymbol = {
        path: 'M 0,-1 0,1',
        strokeOpacity: 1,
        scale: 3
    };
// Create the polyline, passing the symbol in the 'icons' property.
    // Give the line an opacity of 0.
    // Repeat the symbol at intervals of 20 pixels to create the dashed effect.
    var currentPolylineArea = new google.maps.Polyline({
        path: [{lat: startLat, lng: startLng}, {lat: endLat, lng: endLng}],
        strokeOpacity: 0,
        icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '10px'
            }],
        strokeColor:color,
        strokeWeight:2,
        StrokeOpacity:1,
        map: map
    });
}
