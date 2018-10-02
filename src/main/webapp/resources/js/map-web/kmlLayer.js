var ctaLayer;
function kmlLayerStart(url) {
    var map = PF('idnGmap').getMap();
    ctaLayer = new google.maps.KmlLayer({
        url: url,
        map: map
    });

}

function kmlLayerStop() {
    var map = PF('idnGmap').getMap();
    ctaLayer = new google.maps.KmlLayer({
        url: null,
        map: map
    });
}