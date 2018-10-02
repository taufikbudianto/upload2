/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var PLACEMARK = new Hash();


/**
 *@param jap JSON ARRAY Placemarks
 */
function generatePlacemark(jap){
    for(i in jap){
        if(jap[i].image == "DEFAULT_PLACEMARK"){
            jap[i].image = PLACEMARK_DEFAULT_SYMBOL_GIS;
        }else{
            jap[i].image = PLACEMARKS_ICON_PATH + jap[i].image;
        }
        PLACEMARK.setItem(jap[i].id, new Placemark(jap[i].latlng, jap[i].name, jap[i].desc, jap[i].image, jap[i].visible));
    }
}


/**
 *@param indexs ID dari Placemak Placemark yang akan di hilangkan dari Map.
 */
function removePlacemarks(keys){
    for(i in keys){
        removePlacemark(keys[i]);
    }
}




function changeIDPlacemark(id ,oldID){
    var tempPlacemark = PLACEMARK.getItem(oldID);
    PLACEMARK.removeItem(oldID);
    PLACEMARK.setItem(id, tempPlacemark);
}






//for java side placemark writeScript
function createPlacemark(id, latlng, name, des, image, visible){
    // apakah mengunakan placemark default
    if(image == "DEFAULT_PLACEMARK"){
        image = PLACEMARK_DEFAULT_SYMBOL_GIS;
    }else{
        image = PLACEMARKS_ICON_PATH + image;
    }
    PLACEMARK.setItem(id, new Placemark(latlng, name, des, image, visible));
}


/**
 * function ini digunakan ketika ingin melakukan Switch dari Map ke Earth atau sebaliknya
 * Update Posisi dari Placemark yang akan di lempar ke Java Melalui title Change Listener
 * @return (document.title) String berformat UPDATE_PLACEMARK#(60.032, 17.93982)#(38.23738, 67.398238) atau
 * hanya UPDATE_PLACEMARK jika tidak ada Object Placemark sama sekali 
 * 
 */
function getPlacemarksPoint(table, ids){
    var result = "UPDATE_PLACEMARK"+ table + "#";
    for(i in ids){
        if(PLACEMARK.hasItem(ids[i])) result += PLACEMARK.items[ids[i]].point + "#";
    }
    result.substring(0, result.length - 1);
    document.title = result;
}




//from map on click when MODE PLACEMARK
//for membuat placemark dari sisi javaScript
function makePlacemark(latlng){
    var id = new Date().getTime().toString();
//    var id = PLACEMARK.length;
    document.title = "NEW_PLACEMARK_ADDED_STATE#" + id + "#" +latlng;
}

//from java side placemark removeScript
//for menghapus placemark berdasarkan id-nya
function removePlacemark(id){
    if(PLACEMARK.hasItem(id)){
        PLACEMARK.getItem(id).remove();
        PLACEMARK.removeItem(id);
    }
}


//from java side placemark updateScript
//for update properties placemark
function updatePlacemark(id, latlng, name, desc, image){
    // apakah mengunakan placemark default
    if(image == "DEFAULT_PLACEMARK"){
        image = PLACEMARK_DEFAULT_SYMBOL_GIS;
    }else{
        image = PLACEMARKS_ICON_PATH + image;
    }
    var placemark = PLACEMARK.getItem(id);
    placemark.name = name;
    placemark.desc = desc;
    placemark.image = image;
    placemark.point = latlng;
    placemark.setLatLng(placemark.point);
    placemark.marker.getStyleSelector().getIconStyle().getIcon().setHref(placemark.image);
    if(placemark.marker.getName() != ''){
        placemark.reInitName();
    }
}



function Placemark(latlng, name, desc, image, visible){
    this.name = name;
    this.point = latlng;
    this.desc = desc;
    this.image = image;
    this.marker = createMarker(name, latlng, image);
    this.reInitName();
    var me = this;
    google.earth.addEventListener(this.marker, 'click', function(event){
        event.preventDefault();
        /*
         *since sarcore 13/07/12 placemark don't appearing pop up when it's clicked
         *it's will show and hide it's tooltip
        var node =  "<b class='title'><u>"+ me.name +"</u></b><br>" +
        "<b class='field'>Position  :</b>" + position_ui(me.point.lat(), me.point.lng()) +"<br>" +
        "<b class='field'>Description  :</b><font class='value'>" + me.desc + "</font>";
        var balloon = ge.createHtmlStringBalloon('');
        balloon.setFeature(me.marker);
        balloon.setContentString(node);
        measureIntegrationPOI(balloon, me.marker);
        ge.setBalloon(balloon);
        */
       
       if(this.getName() != ''){
           this.setName('');
       }else{
           me.reInitName();
       }
    });
    google.earth.addEventListener(this.marker, 'mouseout', function(event) {
        isMeasureEditSession = false;
    });
    google.earth.addEventListener(this.marker, 'mouseover', function(event) {
        isMeasureEditSession = true;
    });
    map.addOverlay(this.marker);
    //tooltip visibillity before it's dragged
    var visBeforeDrag;
    gex.edit.makeDraggable(this.marker,{
        dragCallback: function(){
            if(this.getName() != ""){
               visBeforeDrag = true;
            }else{
               visBeforeDrag = false;
            }
        },
        dropCallback: function(){
            me.point = new GLatLng(me.marker.getGeometry().getLatitude(), me.marker.getGeometry().getLongitude());
            if(visBeforeDrag){
               me.reInitName();
            }
        }
    });
    if(!visible) this.marker.setVisibility(false);
}


Placemark.prototype.reInitName = function(){
    var latitude = deg_to_dms(this.point.lat(), LATITUDE_MODE);
    var longitude = deg_to_dms(this.point.lng(), LONGITUDE_MODE);
    var tooltip = this.name + " (" + latitude + "  " + longitude + ")";
    this.marker.setName(tooltip);
}


Placemark.prototype.show = function(){
    this.marker.setVisibility(true);
}

Placemark.prototype.hide = function(){
    this.marker.setVisibility(false);
}

Placemark.prototype.inFocus = function(){
    map.setCenter(this.marker.getLatLng(), 7);
}

Placemark.prototype.remove = function(){
    map.removeOverlay(this.marker);
}

Placemark.prototype.getLat = function(){
    return this.point.lat();
}

Placemark.prototype.setLatLng = function(latlng){
    this.latlng = latlng;
    var point = this.marker.getGeometry();
    point.setLatitude(latlng.lat());
    point.setLongitude(latlng.lng());
}

Placemark.prototype.getLng = function(){
    return this.point.lng();
}

