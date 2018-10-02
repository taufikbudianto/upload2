/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function GObject(){
    this.marker = null;
}

GObject.prototype.show = function(){
    if(this.marker.visible) return;
    this.marker.setVisible(true);
    clusterMarker.addMarkers([this.marker]);
    clusterManager.refresh();
}

GObject.prototype.hide = function(){
    if(!this.marker.visible) return;
    this.marker.setVisible(false);
    clusterMarker.removeMarkers();
    clusterManager.reCusteringMarker();
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
    var icon = new GIcon();
    icon.image = imageName;
    icon.iconSize = new GSize(24, 24);
    icon.iconAnchor = new GPoint(16, 16);
    icon.infoWindowAnchor = new GPoint(25, 7);
    var opts = {
        "icon": icon,
        "clickable": true,
        "title" : title
    };
    var marker = new GMarker(latlng, opts);
    markerAdditionalEventForMeasure(marker);
    return marker;
}


function tooltipTransparantMarkerMarker(latlng, title, mouseOverFunction, mouseOutFunction){
    var icon = new GIcon();
    icon.image = TRANSPARANT_ICON;
    icon.iconSize = new GSize(10, 10);
    icon.iconAnchor = new GPoint(8, 8);
    var opts = {
        "icon": icon,
        "clickable": true,
        "title" : title
    };
    var marker = new GMarker(latlng, opts);
    if(mouseOverFunction) GEvent.addListener(marker, 'mouseover', mouseOverFunction);
    if(mouseOutFunction) GEvent.addListener(marker, 'mouseout', mouseOutFunction);
    markerAdditionalEventForMeasure(marker);
    return marker;
}


function CustomLabelMarker(imageName, latlng, labelText, labelOffset){
    var icon = new GIcon();
    icon.image = imageName;
    icon.iconSize = new GSize(24, 24);
    icon.iconAnchor = new GPoint(16, 16);
    icon.infoWindowAnchor = new GPoint(25, 7);
    var opts;
    if(labelText){
        opts = {
            "icon": icon,
            "clickable": true,
            "labelOffset": new GSize(-6, -17),
            "labelText" : labelText
        };
        if(labelOffset){
            opts.labelOffset = labelOffset;
        }
    }else{
        opts = {
            "icon": icon,
            "clickable": true,
            "labelOffset": new GSize(-6, -10)
        };
    }
    var marker = new LabeledMarker(latlng, opts);
    markerAdditionalEventForMeasure(marker);
    return marker;
}

function CustomIconPlacemark(imageName, latlng, labelText){
    this.icon = new GIcon();
    this.icon.image = imageName;
    this.icon.iconSize = new GSize(24, 24);
    this.icon.iconAnchor = new GPoint(16, 24);
    this.icon.infoWindowAnchor = new GPoint(25, 7);
    if(labelText){
        this.opts = {
            "icon": this.icon,
            "clickable": true,
            "labelOffset": new GSize(-5, -32),
            "labelText" : labelText
        };
    }else{
        this.opts = {
            "icon": this.icon,
            "clickable": true,
            "labelOffset": new GSize(-6, -10)
        };
    }
    var marker = new LabeledMarker(latlng, this.opts);
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

