/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




//TITLE CHANGE 
function changeTitle(newTitle){
    decoument.title = newTitle;
}

//STRING GET WIDTH TEXT FUNCTION
String.prototype.width = function(font) {
  var f = font || '12px arial',
      o = $('<div>' + this + '</div>')
            .css({'position': 'absolute', 'float': 'left', 'white-space': 'nowrap', 'visibility': 'hidden', 'font': f})
            .appendTo($('body')),
      w = o.width();

  o.remove();

  return w;
}


//HASHTABLE 
function Hash()
{
    this.length = 0;
    this.items = new Array();
    for (var i = 0; i < arguments.length; i += 2) {
        if (typeof(arguments[i + 1]) != 'undefined') {
            this.items[arguments[i]] = arguments[i + 1];
            this.length++;
        }
    }
   
    this.removeItem = function(in_key)
    {
        var tmp_previous;
        if (typeof(this.items[in_key]) != 'undefined') {
            this.length--;
            var tmp_previous = this.items[in_key];
            delete this.items[in_key];
        }
	   
        return tmp_previous;
    }

    this.getItem = function(in_key) {
        return this.items[in_key];
    }

    this.setItem = function(in_key, in_value)
    {
        var tmp_previous;
        if (typeof(in_value) != 'undefined') {
            if (typeof(this.items[in_key]) == 'undefined') {
                this.length++;
            }
            else {
                tmp_previous = this.items[in_key];
            }

            this.items[in_key] = in_value;
        }
	   
        return tmp_previous;
    }

    this.hasItem = function(in_key)
    {
        return typeof(this.items[in_key]) != 'undefined';
    }

    this.clear = function()
    {
        for (var i in this.items) {
            delete this.items[i];
        }

        this.length = 0;
    }
}


//ARRAY
function wasteElement(array, index){
    if(index == 0){
        array.shift();
        return array; 
    }else if(index == array.length - 1){
        array.pop();
        return array;
    }else{
        var path1 = array.slice(0, index);
        var path2 = array.slice(++index, array.length);
        return path1.concat(path2);
    }
}


//DEGREE TIME SECOND AND DECIMAL FORMAT
function deg_to_dms (deg, type) {
    
    var isMinus = false;
    if(parseFloat(deg).toString().indexOf("-") == 0){
        isMinus = true;                                       
    }
    
    var dfDecimal = Math.abs(parseFloat(deg));
    var dfDegree;
    if(isMinus){
        dfDegree = Math.ceil(parseFloat(deg)); 
    }else{
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
    if(isMinus){
        degree = Math.ceil(dfDecimal);
        --degree;//asumsi Math.ceil(-0) == 1 maka harus dikurangi agar nilainya sesuai.
    }else{
        degree = Math.floor(dfDecimal);
    }
    var minute = Math.round(dfMinute);
    var second = Math.round(Math.floor(dfSecond));
    var p;
    if(isMinus){ 
        if(type == LATITUDE_MODE) p = "LS";
        if(type == LONGITUDE_MODE) p = "BB";
    }else{
        if(type == LATITUDE_MODE) p = "LU";
        if(type == LONGITUDE_MODE) p = "BT";
    }
    return ("" + degree + "° " + minute + "' " + second +"-" + p);
    
    /*
    var dfDegree;
    if (dfDegree < 0) {
        dfDegree = Math.ceil(dfDecimal);
    }else{
        dfDegree = Math.floor(dfDecimal);
    }
    var dfFrac = Math.abs(dfDecimal - dfDegree);
    var dfSec = dfFrac * 3600;
    var dfMinute = Math.floor(dfSec / 60);
    var dfSecond = dfSec - dfMinute * 60;
    if (Math.round(dfSecond) == 60) {
        dfMinute = dfMinute + 1;
        dfSecond = 0;
    }
    if (Math.round(dfMinute) == 60) {
        if (dfDegree < 0) {
            dfDegree = dfDegree - 1;
        } else // ( dfDegree => 0 )
{
            dfDegree = dfDegree + 1;
        }

        dfMinute = 0;
    }
    var degree = Math.round(dfDegree);
    var minute = Math.round(dfMinute);
    var second = Math.round(Math.floor(dfSecond));
    var p;
    if(degree < 0){
        degree = Math.abs(degree);        
        if(type == LATITUDE_MODE) p = "LS";
        if(type == LONGITUDE_MODE) p = "BB";
    }else{
        if(type == LATITUDE_MODE) p = "LU";
        if(type == LONGITUDE_MODE) p = "BT";
    }
    return ("" + degree + "° " + minute + "' " + second +"-" + p);
    */
}

function dms_to_hdm(dms){
    dms = dms.replace("° ", "#");
    dms = dms.replace("' ", "#");
    dms = dms.replace(" ", "");
    dms = dms.replace("-", "#");
    var dmsSplit = dms.split("#");
    var degree = dmsSplit[0];
    var minute = dmsSplit[1];
    var second = dmsSplit[2];
    var p = dmsSplit[3];
    second = (parseFloat(second) / 60) + "";
    second = second.replace("0.", "");
    second = second.substring(0, 4)
    return ("" + degree + "°" + minute + "." + second +"-" + p);
}

function dcm_to_dcm_format(dcm, type){
    var decimalDegree = parseFloat(dcm);
    var result;
    if(decimalDegree < 0){
        if(type == LATITUDE_MODE){
            result = Math.abs(decimalDegree) + "";
            return result.substring(0, 9) + "-LS";
        } else{
            result = Math.abs(decimalDegree) + "";
            return result.substring(0, 9) + "-BB";
        }
    }else{
        if(type == LONGITUDE_MODE){
            result = decimalDegree + "";
            return result.substring(0, 9) + "-BT";
        } else{
            result = decimalDegree + "";
            return result.substring(0, 9) + "-LU"
        }
    }
}


function position_ui(lat, lng){
    var latHDM, lngHDM, latDMS, lngDMS, latDCM, lngDCM;
    latDCM = dcm_to_dcm_format(lat, LATITUDE_MODE);
    lngDCM = dcm_to_dcm_format(lng, LONGITUDE_MODE); 
    latDMS = deg_to_dms(lat, LATITUDE_MODE);
    lngDMS = deg_to_dms(lng, LONGITUDE_MODE); 
    latHDM = dms_to_hdm(latDMS);
    lngHDM = dms_to_hdm(lngDMS);
    return "<table class='position' border='0' cellpadding='4'><thead><tr><th>Latitude</th><th>Longitude</th></tr></thead><tbody><tr><td>"+ latDCM +"</td><td>"+ lngDCM +"</td></tr><tr><td>"+ latDMS +"</td><td>"+ lngDMS +"</td></tr><tr><td>"+ latHDM + "</td><td>"+ lngHDM + "</td></tr></tbody></table>";
}


//COORDINAT LATLNG
function rotationGLatLng(poros, pi, degree){
    if(!poros.x){
        poros.x = poros.lng();
    }
    if(!pi.x){
        pi.x = pi.lng();
    }
    if(!poros.y){
        poros.y = poros.lat();
    }
    if(!pi.y){
        pi.y = pi.lat();
    }
    degree = otherwise(degree);
    var x_ = poros.x , y_ = poros.y,
    x = pi.x, y = pi.y,
    xGenerate, yGenerate,
    cos , sin;

    if(((toDeg(toRad(degree)))/90)%2 == 1)
        cos = 0;
    else
        cos = Math.cos(toRad(degree));
    sin = Math.sin(toRad(degree));

    xGenerate = (x*cos)-(y*sin)+(x_)-(x_*cos) + (y_*sin);
    yGenerate = (x*sin) + (y*cos) + (y_)-(x_*sin) - (y_*cos);

    return new GLatLng(yGenerate, xGenerate);
}


function calculateAngle(latlngFrom, latlngTo){
    var x,y;
    x = latlngTo.lat() - latlngFrom.lat();
    y = latlngTo.lng() - latlngFrom.lng();
    return toDeg(Math.atan2(y,x));
}

//ARRAY OBJECT
function rotationGLatLngs(latlngs, poros, degree){
    for(i in latlngs){
        latlngs[i] =  rotationGLatLng(poros, latlngs[i], degree);
    }
}

function copyArray(array){
    var result = new Array();
    for(i in array){
        result[i] = array[i];
    }
    return result;
}



//MATH
function otherwise(value){
    if(value > 0){
        return value  - (Math.abs(value) * 2);
    }else{
        return value  + (Math.abs(value) * 2);
    }
}

// function for getting angle for resizeing searah jarum jam return minus radian

function getAngle(center, po, p1) {
    if(!center.x){
        center.x = center.lng();
    }
    if(!po.x){
        po.x = po.lng();
    }
    if(!p1.x){
        p1.x = p1.lng();
    }
    if(!center.y){
        center.y = center.lat();
    }
    if(!po.y){
        po.y = po.lat();
    }
    if(!p1.y){
        p1.y = p1.lat();
    }
    var dpo = {
        x: po.x - center.x ,
        y : po.y - center.y
    };
    var dp1 = {
        x : p1.x - center.x,
        y : p1.y - center.y
    };
    var angleRad = Math.atan2(dpo.y, dpo.x)-Math.atan2(dp1.y, dp1.x);
    angleDeg = angleRad * 180 / Math.PI;
    return -angleRad;

}


// function gunakan fungsi ini untuk melakukan development code. getAngle(center, po, pi) terdapat kesalahan pada return value-nya
// sedangkab function tersebut sudah dipakai oleh banyak modul dan modul tsb telah menyesuaikannya. maka dibuat function ini untuk menyesuaikannya
function getAngle2(center, po, p1) {
    if(!center.x){
        center.x = center.lng();
    }
    if(!po.x){
        po.x = po.lng();
    }
    if(!p1.x){
        p1.x = p1.lng();
    }
    if(!center.y){
        center.y = center.lat();
    }
    if(!po.y){
        po.y = po.lat();
    }
    if(!p1.y){
        p1.y = p1.lat();
    }
    var dpo = {
        x: po.x - center.x ,
        y : po.y - center.y
    };
    var dp1 = {
        x : p1.x - center.x,
        y : p1.y - center.y
    };
    var angleRad = Math.atan2(dpo.y, dpo.x)-Math.atan2(dp1.y, dp1.x);
    return toDeg(angleRad);
}



/**
 *@return point hasil rotasi dari point @param po dan dan @param center sebagai poros
 *@param angle sebagai sudut rotasinya dalam bentuk derajat
 */
function rotate(center, po, angle) {
    if(!center.x){
        center.x = center.lng();
    }
    if(!po.x){
        po.x = po.lng();
    }
    if(!center.y){
        center.y = center.lat();
    }
    if(!po.y){
        po.y = po.lat();
    }
    function roundNumber(num, dec) {
        var result = num*Math.pow(10,dec)/Math.pow(10,dec);  //Math.round()
        return result;
    }

    p1 = {
        x : Math.cos(angle) * (po.x-center.x) - Math.sin(angle) * (po.y-center.y) + center.x ,
        y : Math.sin(angle) * (po.x-center.x) + Math.cos(angle) * (po.y-center.y) + center.y
    };
    return p1;
}

function toRad(degree){
    return degree * (Math.PI/180);
}

function toDeg(radian){
    return radian * (180/Math.PI);
}

function scale(pivot, angle, des) {
    if(!pivot.x){
        pivot.x = pivot.lng();
    }
    if(!angle.x){
        angle.x = angle.lng();
    }
    if(!des.x){
        des.x = des.lng();
    }
    if(!pivot.y){
        pivot.y = pivot.lat();
    }
    if(!angle.y){
        angle.y = angle.lat();
    }
    if(!des.y){
        des.y = des.lat();
    }
    var DXangle = Math.abs(calc(pivot.x, angle.x)),
    DYangle = Math.abs(calc(pivot.y, angle.y)),
    DXdes = Math.abs(calc(pivot.x, des.x)),
    DYdes = Math.abs(calc(pivot.y, des.y));
    var hypotenouseAngle = hypotenuse(DXangle, DYangle),
    hypotenouseDes = hypotenuse(DXdes, DYdes);
    var result = hypotenouseDes / hypotenouseAngle;
    return result;
}

function hypotenuse(a, b) {
    return Math.sqrt(Math.pow(a, 2)
        + Math.pow(b, 2));
}

function calc(a, b){
    if(a>b){
        return a-b;
    }else if(b>a){
        return b-a;
    }else{
        return 0;
    }
}


//FILE
function checkImageFile(url){
    var image = new Image();
    image.src = url;
    if(image.complete){
        return true;
    }else{
        return false;
    }
}


//HARVESINE
function harversine(lat1, lng1, lat2, lng2){
    var R = EARTH_RADIUS_IN_KM; // earth's mean radius in km
    var dLat = toRad(lat2 - lat1);
    var dLong = toRad(lng2 - lng1);

    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
    + Math.cos(toRad(lat1))
    * Math.cos(toRad(lat2))
    * Math.sin(dLong / 2)
    * Math.sin(dLong / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c;

    return d;
}



//URL PARAMETER
function gup(paramtername)
{
    paramtername = paramtername.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    var regexS = "[\\?&]"+paramtername+"=([^&#]*)";
    var regex = new RegExp( regexS );
    var results = regex.exec( window.location.href );
    if( results == null )
        return "";
    else
        return results[1];
}


function midPointNotAccuracy(latlng1, latlng2){
    var e,s,w,n;
    if(latlng1.lat() > latlng2.lat()){
        n = latlng1.lat();
        s = latlng2.lat();
    }else{
        n = latlng2.lat(); 
        s = latlng1.lat();
    }
    
    if(latlng1.lng() > latlng2.lng()){
        e = latlng1.lng();
        w = latlng2.lng();
    }else{
        e = latlng2.lng();
        w = latlng1.lng();
    }
    var midLat = (n - s) / 2 + s;
    var midLng = (e - w) / 2 + w;
    return new GLatLng(midLat, midLng);
}



function getPivotPoint(points){
    var topLat = points[0].lat();
    var bottomLat = topLat;
    var rightLng = points[0].lng();
    var leftLng = rightLng;
    for(i = 1;i<points.length; ++i){
        var z = points[i].lat();
        if(z > topLat){
            topLat = z;
        }else if(z < bottomLat){
            bottomLat = z;
        }
        z = points[i].lng();
        if(z > rightLng){
            rightLng = z;
        }else if(z < leftLng){
            leftLng = z;
        }
    }
    var lat = (topLat - bottomLat) / 2 + bottomLat;
    var lng = (rightLng - leftLng) / 2 + leftLng;
    return new GLatLng(lat, lng);
}


function findUpperLeftPoint(points){
    var topLat = points[0].lat();
    var leftLng = points[0].lng();
    for(i = 1;i<points.length; ++i){
        var z = points[i].lat(),
        p = points[i].lng();
        
        if(z > topLat && z < leftLng){
            topLat = z;
            leftLng = p;
        }
    }
    return new GLatLng(topLat, leftLng);
}