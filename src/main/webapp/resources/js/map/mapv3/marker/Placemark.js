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
    placemark.marker.setPosition(placemark.point);
    placemark.marker.setIcon(new google.maps.MarkerImage(
        placemark.image,
        null, /* size is determined at runtime */
        null, /* origin is 0,0 */
        null, /* anchor is bottom center of the scaled image */
        new google.maps.Size(24, 24)
        ));
    placemark.reInitTooltip();
}

function Placemark(latlng, name, desc, image, visible){
    this.name = name;
    this.point = latlng;
    this.desc = desc;
    this.image = image;
    var latitude = deg_to_dms(latlng.lat(), LATITUDE_MODE);
    var longitude = deg_to_dms(latlng.lng(), LONGITUDE_MODE);
    var tooltip = this.name + " (" + latitude + "  " + longitude + ")";
    var labelContent = stringHtmlOnMap(tooltip);
    
    this.marker = new MarkerWithLabel({
        icon: new google.maps.MarkerImage(
            image,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            ),
        position: this.point,
        draggable: true,
        raiseOnDrag: true,
        map: map,
        labelContent: labelContent,
        labelAnchor: new google.maps.Point(10, 0),
        labelClass: ".marker-with-label", // the CSS class for the label
        labelStyle: {
            opacity: 0.75
        }
    });
    var me = this;
    google.maps.event.addListener(this.marker, 'click', function(latlng){
        /*
         *since sarcore 13/07/12 placemark don't appearing pop up when it's clicked
         *it's will show and hide it's tooltip
        var node =  "<b class='title'><u>"+ me.name +"</u></b><br>" +
        "<b class='field'>Position  :</b>" + position_ui(this.getLatLng().lat(), this.getLatLng().lng()) +"<br>" +
        "<b class='field'>Description  :</b><font class='value'>" + me.desc + "</font>";
        eBubble.openOnMarker(this, node);
        */
        if(me.marker.label.getMap() == null){
            //Show Label Marker and Update Position Value, which displayed on that label
            me.reInitTooltip();
            me.marker.label.setMap(map);
        }else{
            //Hide Label Marker
            me.marker.label.setMap(null);
        //me.marker.label.labelDiv_.innerHTML.parentNode.removeChild(me.marker.label.labelDiv_.innerHTML);
        }
    });
    //tooltip visibillity before it's dragged
    /* since upgrading to v3, commented code bellow not used again.
    var visBeforeDrag;
    google.maps.event.addListener(this.marker, "dragstart", function(latlng){
        if(me.tooltip.div_.style.visibility == 'visible'){
            visBeforeDrag = true;
            me.tooltip.hide();
        }else{
            visBeforeDrag = false;
        }
    });
    */
    google.maps.event.addListener(this.marker, "dragend", function(mouseevent){
        me.point = mouseevent.latLng;
        me.reInitTooltip();
    });
    this.marker.setMap(map);
    if(!visible) this.marker.setVisible(false);
}

Placemark.prototype.show = function(){
    this.marker.setVisible(true);
    this.reInitTooltip();
    this.marker.label.setMap(map);
}

Placemark.prototype.reInitTooltip = function(){
    var latitude = deg_to_dms(this.marker.getPosition().lat(), LATITUDE_MODE);
    var longitude = deg_to_dms(this.marker.getPosition().lng(), LONGITUDE_MODE);
    var tooltip = this.name + " (" + latitude + "  " + longitude + ")";
    var tooltipInfo = stringHtmlOnMap(tooltip);
    this.marker.labelContent = tooltipInfo;
    this.marker.label.labelDiv_.innerHTML = tooltipInfo;
    this.marker.label.eventDiv_.innerHTML = this.marker.label.labelDiv_.innerHTML;
}

Placemark.prototype.hide = function(){
    this.marker.setVisible(false);
    this.marker.label.setMap(null);
}

Placemark.prototype.inFocus = function(){
    map.setCenter(this.marker.getPosition(), 7);
}

Placemark.prototype.remove = function(){
    this.marker.setMap(null);
}

Placemark.prototype.getLat = function(){
    return this.marker.getPosition().lat();
}

Placemark.prototype.setLatLng = function(latlng){
    this.marker.setPosition(latlng);
}

Placemark.prototype.getLng = function(){
    return this.marker.getPosition().lng();
}

