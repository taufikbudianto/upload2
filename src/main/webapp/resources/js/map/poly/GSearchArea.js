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
    this.l.push(this.l[0]);
    this.polygon = new GPolygon(this.l,"#FF0000", 3, 1,"#000000",0.1,{
        clickable: false
    });
    //this.polygon = new GPolygon(this.l);
    map.addOverlay(this.polygon);
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
    this.polygon.hide();
    for(i in this.arrayTooltpMarker){
        this.arrayTooltpMarker[i].hide();
    }
}

GSearchArea.prototype.show = function(){
    if(this.visible) return;
    this.visible = true;
    this.polygon.show();
    for(i in this.arrayTooltpMarker){
        this.arrayTooltpMarker[i].show();
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
        map.addOverlay(marker);
    }
}





GSearchArea.prototype.createLabelPoint = function(latlng, label){
    var icon = new GIcon();    
    icon.image = TRANSPARANT_ICON;
    icon.iconSize = new GSize(10, 10);
    icon.iconAnchor = new GPoint(8, 8);
    var opts = {
        "icon": icon, 
        "clickable": false, 
        "labelOffset": new GSize(-6, -17), 
        "labelText" : label,
        "labelClass" : 'searcharea_point'
    };
    return new LabeledMarker(latlng, opts);
}