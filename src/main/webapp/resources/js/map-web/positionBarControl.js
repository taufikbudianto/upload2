function IconBasarnas() {
    var map = PF('idnGmap').getMap();
    var icon_basarnas = document.createElement('div');
    icon_basarnas.id = 'basarnas-icon-div';
    icon_basarnas.innerHTML = "<img style='width:60px;height:60px' src='/resources/barcelona-layout/images/logo.png'/>"
    icon_basarnas.index = 1;
    var position = google.maps.ControlPosition.LEFT_BOTTOM;
    map.controls[position].push(icon_basarnas);
}

function PositionBarControl() {
    var map = PF('idnGmap').getMap();
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
    google.maps.event.addListener(map, 'mousemove', function (mouseEvent) {
        var latValue = document.getElementById('lat_value');
        var lngValue = document.getElementById('lng_value');
        if (latValue) {
            latValue.innerHTML = deg_to_dms(mouseEvent.latLng.lat(), 0);
        }
        if (lngValue) {
            lngValue.innerHTML = deg_to_dms(mouseEvent.latLng.lng(), 1);
        }
    });

    function deg_to_dms(deg, type) {

        var isMinus = false;
        if (parseFloat(deg).toString().indexOf("-") == 0) {
            isMinus = true;
        }

        var dfDecimal = Math.abs(parseFloat(deg));
        var dfDegree;
        if (isMinus) {
            dfDegree = Math.ceil(parseFloat(deg));
        } else {
            dfDegree = Math.floor(parseFloat(deg));
        }
        var dfFrac = Math.abs(parseFloat(deg) - dfDegree);
        var dfSec = dfFrac * 3600;
        var dfMinute = Math.floor(dfSec / 60);
        var dfSecond = dfSec - dfMinute * 60;
        if (Math.round(dfSecond) == 60) {
            dfMinute = dfMinute + 1;
            dfSecond = 0;
        }
        if (Math.round(dfMinute) == 60) {
            if (isMinus) {
                dfDecimal = dfDecimal - 1;
            } else // ( dfDegree => 0 )
            {
                dfDecimal = dfDecimal + 1;
            }

            dfMinute = 0;
        }
        var degree;
        if (isMinus) {
            degree = Math.ceil(dfDecimal);
            --degree;//asumsi Math.ceil(-0) == 1 maka harus dikurangi agar nilainya sesuai.
        } else {
            degree = Math.floor(dfDecimal);
        }
        var minute = Math.round(dfMinute);
        var second = Math.round(Math.floor(dfSecond));
        var p;
        if (isMinus) {
            if (type == 0)
                p = "LS";
            if (type == 1)
                p = "BB";
        } else {
            if (type == 0)
                p = "LU";
            if (type == 1)
                p = "BT";
        }
        return ("" + degree + "° " + minute + "' " + second + "-" + p);
    }
}