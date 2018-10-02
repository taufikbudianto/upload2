/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function GLatLng(latitude, longitude){
    this.latitude = latitude;
    this.longitude = longitude;
    this.x = longitude;
    this.y = latitude;
}

GLatLng.prototype.lat = function(){
    return this.latitude;
}

GLatLng.prototype.lng = function(){
    return this.longitude;
}

GLatLng.prototype.toString = function(){
    return "(" + this.latitude + "," +  this.longitude + ")";
}

