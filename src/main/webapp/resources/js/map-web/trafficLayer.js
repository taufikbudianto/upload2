function trafficLayerStart() {
    var map = PF('idnGmap').getMap();
    var trafficLayer = new google.maps.TrafficLayer();
    trafficLayer.setMap(map);
}

function trafficLayerEnd() {
    var trafficLayer = new google.maps.TrafficLayer();
    trafficLayer.setMap(null);
}
