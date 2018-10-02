/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



function PositionBarControl(map) {
    var position_bar = document.createElement('div');
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
    position_bar.index = 1;
    map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(position_bar);
    google.maps.event.addListener(map, 'mousemove', function(mouseEvent){
        var latValue = document.getElementById('lat_value');
        var lngValue = document.getElementById('lng_value');
        if(latValue){
            latValue.innerHTML = deg_to_dms(mouseEvent.latLng.lat(), LATITUDE_MODE);
        }
        if(lngValue){
            lngValue.innerHTML = deg_to_dms(mouseEvent.latLng.lng(), LONGITUDE_MODE);
        }
        
    });
}


function IconBasarnas(map, position){
    var icon_basarnas = document.createElement('div');
    icon_basarnas.id = 'basarnas-icon-div';
    icon_basarnas.innerHTML = "<img style='width:60px;height:60px' src='"+ BASARNAS_BRAND_IMAGE +"'/>"
    icon_basarnas.index = 1;
    if(position == null) position  = google.maps.ControlPosition.LEFT_BOTTOM;
    map.controls[position].push(icon_basarnas);
}
    
