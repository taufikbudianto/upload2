/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function handlePointClick(event) {
    if (PF('widSobUtil').getJQ().find(':checked').val() === 'PLACEMARK') {
        document.getElementById('idLatPlacemark').value = event.latLng.lat();
        document.getElementById('idLngPlacemark').value = event.latLng.lng();
        remoteNewPlacemark();
    }
}

function updateMarker(lat, lng) {
    document.getElementById('idLatPlacemark').value = lat;
    document.getElementById('idLngPlacemark').value = lng;
}