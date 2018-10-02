/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var PLACEMARK = new Hash();

//HELPER FOR SWICHTING GEE AND FUSION MAP
//for java side placemark writeScript

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
        PLACEMARK.setItem(jap[i].id,new Placemark(jap[i].latlng, jap[i].name, jap[i].desc, jap[i].image, jap[i].visible));
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


/**
 * function ini digunakan ketika ingin melakukan Switch dari Map ke Earth atau sebaliknya
 * Update Posisi dari Placemark yang akan di lempar ke Java Melalui title Change Listener
 * @return (document.title) String berformat UPDATE_PLACEMARK#(60.032, 17.93982)#(38.23738, 67.398238) atau
 * hanya UPDATE_PLACEMARK jika tidak ada Object Placemark sama sekali 
 * 
 */
function getPlacemarksPoint(table ,ids){
    var result = "UPDATE_PLACEMARK"+ table +"#";
    for(i in ids){
        if(PLACEMARK.hasItem(ids[i])) result += PLACEMARK.items[ids[i]].point + "#";
    }
    result.substring(0, result.length - 1);
    document.title = result;
}



function createPlacemark(id, latlng, name, des, image, visible){
    // apakah mengunakan placemark default
    if(image == "DEFAULT_PLACEMARK"){
        image = PLACEMARK_DEFAULT_SYMBOL_GIS;
    }else{
        image = PLACEMARKS_ICON_PATH + image;
    }
    PLACEMARK.setItem(id, new Placemark(latlng, name, des, image, visible));
}

//from map on click when MODE PLACEMARK
//for membuat placemark dari sisi javaScript
function makePlacemark(latlng){
    //ID for placemark use date time milisecond just for unique key in hash don't just user presentation
    var id = new Date().getTime().toString();

//    var id = PLACEMARK.length;
    document.title = "NEW_PLACEMARK_ADDED_STATE#" + id + "#" +latlng;
}

//from java side placemark removeScript
//for menghapus placemark berdasarkan id-nya
function removePlacemark(id){
    PLACEMARK.getItem(id).remove();
    PLACEMARK.removeItem(id);
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
    placemark.marker.setLatLng(placemark.point);
    placemark.marker.setImage(placemark.image);
    if(placemark.tooltip.div_.style.visibility == 'visible'){
        placemark.reInitTooltip();
    }
}

function Placemark(latlng, name, desc, image, visible){
    this.name = name;
    this.point = latlng;
    this.desc = desc;
    this.image = image;
    var gambar = new GIcon();
    this.tooltip = new Tooltip("Placemark Name", 1, map);
    gambar.image = image;
    gambar.iconSize = new GSize(24, 24);
    gambar.iconAnchor = new GPoint(16, 16);
    gambar.infoWindowAnchor = new GPoint(25, 7);
    var opts = {
        icon : gambar, 
        clickable : true, 
        draggable : true, 
        dragCrossMove : true
    };
    this.marker = new GMarker(this.point, opts);
    var me = this;
    GEvent.addListener(this.marker, 'click', function(latlng){
        /*
         *since sarcore 13/07/12 placemark don't appearing pop up when it's clicked
         *it's will show and hide it's tooltip
        var node =  "<b class='title'><u>"+ me.name +"</u></b><br>" +
        "<b class='field'>Position  :</b>" + position_ui(this.getLatLng().lat(), this.getLatLng().lng()) +"<br>" +
        "<b class='field'>Description  :</b><font class='value'>" + me.desc + "</font>";
        eBubble.openOnMarker(this, node);
        */
       if(me.tooltip.div_.style.visibility == 'hidden'){
           me.reInitTooltip();
       }else{
           me.tooltip.hide();
       }
    });
    //tooltip visibillity before it's dragged
    var visBeforeDrag;
    GEvent.addListener(this.marker, "dragstart", function(latlng){
        if(me.tooltip.div_.style.visibility == 'visible'){
           visBeforeDrag = true;
           me.tooltip.hide();
        }else{
           visBeforeDrag = false;
        }
    });
    GEvent.addListener(this.marker, "dragend", function(latlng){
        me.point = latlng;
        if(visBeforeDrag){
           me.reInitTooltip();
        }
    });
    map.addOverlay(this.marker);
    this.reInitTooltip();
    if(!visible) this.marker.hide();
}

Placemark.prototype.show = function(){
    this.marker.show();
    if(this.tooltip.div_.style.visibility == 'visible'){
        this.reInitTooltip();
    }
}

Placemark.prototype.reInitTooltip = function(){
    var latitude = deg_to_dms(this.marker.getLatLng().lat(), LATITUDE_MODE);
    var longitude = deg_to_dms(this.marker.getLatLng().lng(), LONGITUDE_MODE);
    var tooltip = this.name + " (" + latitude + "  " + longitude + ")";
    this.tooltip.show(this.marker.getLatLng(), stringHtmlOnMap(tooltip));
}

Placemark.prototype.hide = function(){
    this.marker.hide();
    if(this.tooltip.div_.style.visibility == 'visible'){
        this.tooltip.hide();
    }
}

Placemark.prototype.inFocus = function(){
    map.setCenter(this.marker.getLatLng(), 7);
}

Placemark.prototype.remove = function(){
    map.removeOverlay(this.marker);
    this.tooltip.remove();
    this.tooltip = null;
}

Placemark.prototype.getLat = function(){
    return this.marker.getLatLng().lat();
}

Placemark.prototype.setLatLng = function(latlng){
    this.marker.setLatLng(latlng);
    /*
    if(eBubble.visible){
        eBubble.hide();
    }
    */
}

Placemark.prototype.getLng = function(){
    return this.marker.getLatLng().lng();
}

