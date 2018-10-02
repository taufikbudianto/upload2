/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function GObject(){
    this.marker = null;
}

GObject.prototype.show = function(){
    if(this.marker.getVisible()) return;
    this.marker.setVisible(true);
    clusterMarker.addMarker(this.marker);
    clusterManager.refresh();
}

GObject.prototype.hide = function(){
    if(!this.marker.getVisible()) return;
    this.marker.setVisible(false);
    clusterMarker.removeMarker(this.marker);
    clusterManager.refresh();
}

GObject.prototype.setID = function(id){
    this.id = id;
}

GObject.prototype.getID = function(){
    return this.id;
}

GObject.prototype.addToClusterManager = function(){
    clusterManager.add(this);
}

GObject.prototype.remove = function(keyInHash, hash){
    this.removeFromClusterManager(keyInHash, hash);
}

GObject.prototype.removeFromClusterManager = function(keyInHash, hash){
    clusterManager.remove(keyInHash, hash);
}


function CustomIconMarker(imageName, latlng, title){
    var opts = {
        icon: new google.maps.MarkerImage(
            imageName,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            ),
        clickable: true,
        title : title,
        position : latlng
    };
    var marker = new google.maps.Marker(opts);
    markerAdditionalEventForMeasure(marker);
    return marker;
}


function tooltipTransparantMarkerMarker(latlng, title, mouseOverFunction, mouseOutFunction){
    var opts = {
        icon: new google.maps.MarkerImage(
            TRANSPARANT_ICON,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            ),
        position: latlng,
        clickable: true,
        title : title
    };
    var marker = new google.maps.Marker(opts);
    if(mouseOverFunction) google.maps.event.addListener(marker, 'mouseover', mouseOverFunction);
    if(mouseOutFunction) google.maps.event.addListener(marker, 'mouseout', mouseOutFunction);
    markerAdditionalEventForMeasure(marker);
    return marker;
}


function CustomLabelMarker(imageName, latlng, labelText, labelOffset){
    var icon;
    if(imageName == 'WITHOUT_ICON'){
        icon = {};
    }else{
        icon = new google.maps.MarkerImage(
            imageName,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            );
    }
    var marker = new MarkerWithLabel({
        icon: icon,
        position: latlng,
        clickable : true,
        labelContent: labelText,
        labelAnchor: new google.maps.Point(22, 0),
        labelClass: ".marker-with-label", // the CSS class for the label
        labelStyle: {
            opacity: 0.75
        }
    });
    markerAdditionalEventForMeasure(marker);
    return marker;
}

function CustomIconPlacemark(imageName, latlng, labelText){
    var opts;
    var marker = new MarkerWithLabel({
        icon: new google.maps.MarkerImage(
            imageName,
            null, /* size is determined at runtime */
            null, /* origin is 0,0 */
            null, /* anchor is bottom center of the scaled image */
            new google.maps.Size(24, 24)
            ),
        clickable : true,
        position: latlng,
        labelContent: labelText,
        labelAnchor: new google.maps.Point(22, 0),
        labelClass: ".marker-with-label", // the CSS class for the label
        labelStyle: {
            opacity: 0.75
        }
    });
    markerAdditionalEventForMeasure(marker);
    return marker;
}



/*
 *Menampilkan pada Map semua marker yang ditampung pada hash collaction
 */
function showAllObjectMarker(hash){
    for(i in hash.items){
        if(hash.items[i] != null){
            map.addOverlay(hash.items[i].marker);
        }
    }
}


/*
*Remove pada map berdasarkan IDnya
*/
function removeObjectMarker(keyInHash, hash){
    var marker = hash.getItem(keyInHash);
    marker.remove(keyInHash, hash);
}


function removeAllObjectMarker(hash){
    clusterManager.clearSpecifiedHash(hash);
}

function markerIdentifier(id, name){
    return id + "###" + name;
}

function markerIdentifierDisplay(markerIdentifier){
    return markerIdentifier.split('###')[1];
}

