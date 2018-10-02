/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


GSEARCH_AREA = new Hash();

function createGSearchArea(id, incidentid, pivot, vertexs){
    if(!GSEARCH_AREA.hasItem(id)){
        GSEARCH_AREA.setItem(id, new GSearchArea(id, incidentid, pivot, vertexs));        
    }
}

function removeGSearchArea(id){
    if(GSEARCH_AREA.hasItem(id)){
        GSEARCH_AREA.getItem(id).remove();
        GSEARCH_AREA.removeItem(id);        
    }
}



function GSearchArea(id, incidentid, pivot, vertexs){
    this.id = id;
    this.incidentid = incidentid;
    this.l = vertexs;
    this.visible = true;
    this.pivot = pivot;
    this.draw();
}


GSearchArea.prototype.draw = function(){
    this.polygon = new google.maps.Polygon({
        paths: this.l,
        strokeColor: "#FF0000",
        strokeOpacity: 1,
        strokeWeight: 3,
        fillColor: "#000000",
        fillOpacity: 0.35
    });
    this.l.push(this.l[0]);
    this.polygon.setMap(map);
    this.initTooltipMarker();
}

GSearchArea.prototype.remove = function(){
    this.polygon.setMap(null);
    this.polygon = null;
    if(this.arrayTooltpMarker){
        for(i in this.arrayTooltpMarker){
            this.arrayTooltpMarker[i].setMap(null);
        }
        this.arrayTooltpMarker = null;
    }
}

GSearchArea.prototype.hide = function(){
    if(!this.visible) return;
    this.visible = false;
    this.polygon.setVisible(false);
    for(i in this.arrayTooltpMarker){
        this.arrayTooltpMarker[i].setVisible(false);
    }
}

GSearchArea.prototype.show = function(){
    if(this.visible) return;
    this.visible = true;
    this.polygon.setVisible(true);
    for(i in this.arrayTooltpMarker){
        this.arrayTooltpMarker[i].setVisible(true);
    }
}


GSearchArea.prototype.checkSearchAreaType = function(){
    var r1, r2, n;
    r1 = hypotenuse(Math.abs(this.l[0].lng()-this.l[3].lng()), Math.abs(this.l[0].lat()-this.l[3].lat()));
    r2 = hypotenuse(Math.abs(this.l[1].lng()-this.l[2].lng()), Math.abs(this.l[1].lat()-this.l[2].lat()));
    n = Math.abs(r1 - r2);
    if(r1 == r2 || n < 0.0000000000001){
        this.type = "DRIFT";
    }else{
        this.type = "TRAPEZ";
    }
}


GSearchArea.prototype.initTooltipMarker = function(){
    this.checkSearchAreaType();
    this.arrayTooltpMarker = new Array();
    if(this.type == "TRAPEZ"){
        var index = "A";
        for(i in this.l){
            if(i == 0){
                index = "B";
            }else if(i == 1){
                index = "A";
            }else if(i == 2){
                index = "D";
            }else if(i == 3){
                index = "C";
            }
            label = stringHtmlOnMap("Point " + index);
            marker = this.createLabelPoint(this.l[i], label);
            this.arrayTooltpMarker.push(marker);
            marker.setMap(map);
        }
        return;
    }
    var label, r, zd, marker;
    r = hypotenuse(Math.abs(this.l[0].lng()-this.pivot.lng()), Math.abs(this.l[0].lat()-this.pivot.lat()));
    // zero degree variable
    zd = new GLatLng(this.pivot.lat() + r, this.pivot.lng());
    for(i in this.l){
        var arc = getAngle2(this.pivot, zd, this.l[i]);
        arc = Math.floor(arc);
        if(arc < 0) arc += 360;
        if(arc > 270 && arc <= 360 || arc == 0){
            label = stringHtmlOnMap("Point A");
            marker = this.createLabelPoint(this.l[i], label);
        } else if(arc > 0 && arc <= 90){
            label = stringHtmlOnMap("Point B");
            marker = this.createLabelPoint(this.l[i], label);
        } else if(arc > 90 && arc <= 180){
            label = stringHtmlOnMap("Point C");
            marker = this.createLabelPoint(this.l[i], label);
        } else if(arc > 180 && arc <= 270){
            label = stringHtmlOnMap("Point D");
            marker = this.createLabelPoint(this.l[i], label);
        }
        this.arrayTooltpMarker.push(marker);
        marker.setMap(map);
    }
}





GSearchArea.prototype.createLabelPoint = function(latlng, label){
    var marker = new MarkerWithLabel({
       position: latlng,
       labelContent: label,
       labelAnchor: new google.maps.Point(22, 0),
       labelClass: ".marker-with-label", // the CSS class for the label
       labelStyle: {opacity: 0.75},
       icon: {}
     });
    return marker;
}
