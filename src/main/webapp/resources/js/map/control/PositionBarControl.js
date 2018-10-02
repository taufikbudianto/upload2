/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



function PositionBarControl() {}
    
    PositionBarControl.prototype = new GControl();

PositionBarControl.prototype.initialize = function(map){    
    var position_bar = document.createElement("div");
    position_bar.id = 'position_bar';
    position_bar.innerHTML = 
        "<p class='field_position' id='lat_field'>\n\
            <b>Latitude  : \n\
                <span id='lat_value'></span> \n\
            </b> \n\
        </p> \n\
        <p class='field_position' id='lng_field'> \n\
            <b>Longitude : \n\
                <span id='lng_value'></span> \n\
            </b> \n\
        </p>";
    map.getContainer().appendChild(position_bar);
    var latValue = document.getElementById('lat_value');
    var lngValue = document.getElementById('lng_value');
    GEvent.addListener(map, 'mousemove', function(latlng){
        latValue.innerHTML = deg_to_dms(latlng.lat(), LATITUDE_MODE);
        lngValue.innerHTML = deg_to_dms(latlng.lng(), LONGITUDE_MODE);
    });
    return position_bar;
}