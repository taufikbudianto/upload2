/**
 * The Tooltip class is an addon designed for the Google Maps GMarker class.
 * @constructor
 * @param {GMarker} marker
 * @param {String} text
 * @param {Number} padding
 */

function Tooltip(node, padding, map){
    this.node_ = node;
    this.padding_ = padding;
    this.map = map;
    this.position = null;
    var div = document.createElement("div");
    div.className = 'tooltip';
    div.style.position = 'absolute';
    div.style.visibility = 'hidden';
    this.map.getPane(G_MAP_FLOAT_PANE).appendChild(div);
    this.div_ = div;
    this.div_.style.width = 200;
    this.bindEvent();
}


Tooltip.prototype = new GOverlay();



Tooltip.prototype.remove = function(){
    this.div_.parentNode.removeChild(this.div_);
}

Tooltip.prototype.copy = function(){
    return new Tooltip(this.node_, this.padding_, this.map);
}

Tooltip.prototype.redraw = function(force, latlng){
    if (!force) return;
    var pos;
    if(latlng instanceof GLatLng){
        pos = this.map.fromLatLngToDivPixel(latlng);
        this.position = latlng;
    }else if(latlng instanceof GPoint){
        pos = latlng;
    }else{
        return;
    }
    var xPos = Math.round(pos.x - this.div_.clientWidth / 2);
    var yPos = pos.y - 10 - this.div_.clientHeight - this.padding_;
    this.div_.style.top = yPos + 'px';
    this.div_.style.left = xPos + 'px';
}


Tooltip.prototype.bindEvent = function(){
    var me = this;
    GEvent.bind(this.map, 'moveend', this, function(){
        if(me.div_.style.visibility == 'visible'){
            me.redraw(true , me.map.fromLatLngToDivPixel(me.position));
        }
    });
}



Tooltip.prototype.show = function(latlng, node, untextElement){
    if(node){
        this.node_ = node;
        if(untextElement){
            this.div_.innerHTML = this.node_;
            this.div_.appendChild(untextElement);
        }else{
            this.div_.innerHTML = this.node_;
        }
        if(this.node_.width) this.div_.style.width = this.node_.width();
    }
    this.redraw(true, latlng);
    this.div_.style.visibility = 'visible';
}

Tooltip.prototype.hide = function(){
    this.div_.style.visibility = 'hidden';
}