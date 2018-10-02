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
    this.pivot = pivot;
    this.draw();
}


GSearchArea.prototype.draw = function(){
    //agar vertex yang baru ditambah bisa diupdate tambahkan posisi awal vertex pada Array
    this.l.push(this.l[0]);
    this.polygon = ge.createPlacemark('');

    // Create the polygon.
    var polygon = ge.createPolygon('');
    polygon.setAltitudeMode(ge.ALTITUDE_CLAMP_TO_GROUND);
    polygon.setTessellate(true);
    this.polygon.setGeometry(polygon);

    // Add points for the outer shape.
    var outer = ge.createLinearRing('');
    for(i in this.l){
        outer.getCoordinates().pushLatLngAlt(this.l[i].lat(), this.l[i].lng(), 700);
    }
    polygon.setOuterBoundary(outer);
    //Create a style and set width and color of line
    this.polygon.setStyleSelector(ge.createStyle(''));
    var lineStyle = this.polygon.getStyleSelector().getLineStyle();
    lineStyle.setWidth(30);
    lineStyle.getColor().set('ff0000ff');
    var polyColor =
    this.polygon.getStyleSelector().getPolyStyle().getColor();
    polyColor.setA(10);
    polyColor.setB(0);
    polyColor.setG(0);
    polyColor.setR(0);
    // Add the placemark to Earth.
    map.addOverlay(this.polygon);
    this.l.splice(this.l.length - 1, this.l.length);
    this.initTooltipMarker();
}




GSearchArea.prototype.remove = function(){
    map.removeOverlay(this.polygon);
    this.polygon = null;
    if(this.arrayTooltpMarker){
        for(i in this.arrayTooltpMarker){
            map.removeOverlay(this.arrayTooltpMarker[i]);
        }
        this.arrayTooltpMarker = null;
    }
}

GSearchArea.prototype.hide = function(){
    if(!this.visible) return;
    this.visible = false;
    this.polygon.setVisibility(false);
    for(i in this.arrayTooltpMarker){
        this.arrayTooltpMarker[i].setVisibility(false);
    }
}

GSearchArea.prototype.show = function(){
    if(this.visible) return;
    this.visible = true;
    this.polygon.setVisibility(true);
    for(i in this.arrayTooltpMarker){
        this.arrayTooltpMarker[i].setVisibility(true);
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
    var index = "A";
    if(this.type == "TRAPEZ"){
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
            label = "Point " + index;
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
            this.arrayTooltpMarker.push(marker);
            map.addOverlay(marker);
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
            label = "Point A";
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        } else if(arc > 0 && arc <= 90){
            label = "Point B";
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        } else if(arc > 90 && arc <= 180){
            label = "Point C";
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        } else if(arc > 180 && arc <= 270){
            label = "Point D";
            marker = createMarker(label, this.l[i], TRANSPARANT_ICON, 0.3);
        }
        this.arrayTooltpMarker.push(marker);
        measureIntegrationMarker(marker);
        map.addOverlay(marker);
    }
}