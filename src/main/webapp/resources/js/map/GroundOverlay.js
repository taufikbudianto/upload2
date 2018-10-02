//Historical Overlay
var historicalOverlay;
function groundOverlay() {
    var map = new google.maps.Map(document.getElementById('idMap'), {
        zoom: 13,
        center: {lat: 40.740, lng: -74.18},
        mapTypeId: 'satellite'
    });
    var imageBounds = {
        north: 40.773941,
        south: 40.712216,
        east: -74.12544,
        west: -74.22655
    };
    historicalOverlay = new google.maps.GroundOverlay(
            'https://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg',
            imageBounds);
    historicalOverlay.setMap(map);
}
//End