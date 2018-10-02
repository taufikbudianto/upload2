// Subclassed VML/SVG Polyline
//
// Bill Chadwick May 2007
//
// Free for any use
//
// Adds 
//  click, mouseover and mouseout events
//  tooltip
//  dot or dash styling
//  dynamic setting of colour, opacity, weight and dash style  

var BDCCPolylineId = 0;//counter for unique DOM Ids

// Constructor params exactly as GPolyline then a tooltip and dash which must be one of "dot" or "dash" or "solid"

function BDCCPolyline(points, color, weight, opacity, tooltip, dash) {	
    
    this.tooltip = tooltip;
    this.dash = (dash != null) ? dash : "solid";
    this.oColor = color;
    this.weight = weight;
    this.oOpacity = ((opacity >= 0.005)&&(opacity <= 0.015))?0.02:opacity;//don't use our special value
    this.lineStyle = 'single';
    
    //make a unique id for this polyline 
    BDCCPolylineId += 1;
    var uid = BDCCPolylineId.toString(16); //initial color so we can find dom node
    while (uid.length < 6)
        uid = "0" + uid;
    this.uid = "#" + uid;
    this.domid = "BdccPolyline" + BDCCPolylineId.toString();//unique domId

    this.usesVml = (navigator.userAgent.indexOf("MSIE") != -1);
    
    //call super class constructor with temp colour and transperency for nearly invisible
    GPolyline.call(this,points,this.uid,weight,0.01,{"clickable":false}); 
}
BDCCPolyline.prototype = new GPolyline(new Array(new GLatLng(0,0)));//subclass from GPolyline

// According to the GMap docs, GPolyline implements the GOverlay interface
// That is it implements the functions initialize, remove, copy and redraw
// Here we add to GPolyline's own implementation of these functions
//

BDCCPolyline.prototype.initialize = function(map) {
    GPolyline.prototype.initialize.call(this,map); //super class
    //Initialise cant be used to cache the SVG path node or its parent svg node as both are recreated in redraw
    //For VML the shape node is recreated in redraw and all shapes have a common parent       
    this.map = map;       
}

BDCCPolyline.prototype.remove = function() {
    GPolyline.prototype.remove.call(this); //super class    
}

BDCCPolyline.prototype.copy = function(map) {
    return new BDCCPolyline(this.points,this.oColor,this.weight,this.oOpacity,this.tooltip,this.dash);
}

BDCCPolyline.prototype.redraw = function(force) {

   GPolyline.prototype.redraw.call(this,force); //super class
   
   //update later when the API's rendering has finished
   var credraw = GEvent.callback(this,this.delayedRedraw);
   
   if(this.dredraw)
       window.clearTimeout(this.dredraw); 
          
   this.dredraw = window.setTimeout(function(force){credraw(true);},100); //the true here is vital
}

BDCCPolyline.prototype.delayedRedraw = function(force) {
   
   
   var dom = null;
   var i;
   var prnt;

   //You could omit parent node checking if you only have one map per document
   //Doing so will give a performance improvement	  	    
   this.map.getPane(G_MAP_MAP_PANE).parentNode.id = "Cntnr" + this.domid;
    
   if(this.usesVml){
      
        try{        
            var shps = document.getElementsByTagName("shape"); 
	  	        
	  	    //find the one with our UID string as its colour + our 1% opacity
	  	    //and on the correct map       	
        	for(i=0;i<shps.length;i++){
            	dom = shps[i];
                if ((dom.stroke.color == this.uid) && 
                    (dom.stroke.opacity > 0.005) &&
                    (dom.stroke.opacity < 0.015)){
                    prnt = dom.parentNode;
                	while(prnt != null){
                        if (prnt.id == "Cntnr" + this.domid){
                            i = shps.length;
                            break;
                        }
                        else{
                            prnt = prnt.parentNode; 
                        }
                    }
                    if(i < shps.length)
                        dom = null;
                }
                else
                    dom = null;                                                	
        	}

            if(dom != null){
                //we found our VML node
                if(this.tooltip != null){
                    dom.style.cursor = "help";//to show mouseover 
                    dom.title = this.tooltip;
                }
                dom.id = this.domid;//assign unique DOM id so we can modify attributes later   
            }
        }
        catch (ex)
        {
		    if(BDCCPolylineId == 1)
			    alert("The designer of this Google Maps web page has attempted to use VML graphics without including the necessary header material.");
        }
   }
   else{
        var shps = document.getElementsByTagName("path"); 
	    i = shps.length-1;
	    
	    
  	    //find the one with our UID string as its colour + our 1% opacity
  	    //and on the correct map       	
    	for(i=0;i<shps.length;i++){
        	dom = shps[i];
            if ((dom.getAttribute("stroke") == this.uid) && 
                (dom.getAttribute("stroke-opacity") >= 0.005) &&
                (dom.getAttribute("stroke-opacity") <= 0.015)){
                prnt = dom.parentNode;
            	while(prnt != null){
                    if (prnt.id == "Cntnr" + this.domid){
                        i = shps.length;
                        break;
                    }
                    else{
                        prnt = prnt.parentNode; 
                    }
                }
                if(i < shps.length)
                    dom = null;
            }
            else
                dom = null;                                                	
    	}	    
		

        if(dom != null){
            //we found our SVG node

            if(this.tooltip != null){
                dom.style.cursor = "help";//to show mouseover 
                dom.setAttribute("title",this.tooltip);
            }
            dom.setAttribute("id",this.domid);//assign unique DOM id so we can modify attributes later
            dom.setAttribute("pointer-events","stroke");//only click on the line, not its bounding rectangle
        }
   }
   
   if(this.dredraw)
       window.clearTimeout(this.dredraw); 

   if(dom != null){
   
      //set up the appearance of our polyline
       this.setColor(this.oColor);
       this.setDash(this.dash);
       this.setOpacity(this.oOpacity);
       this.setWeight(this.weight);
       this.setLineStyle(this.lineStyle);
       
       //set up event handlers
       var cclick = GEvent.callback(this,this.onClick);
       var cover = GEvent.callback(this,this.onOver);
       var cout = GEvent.callback(this,this.onOut);
   
       GEvent.clearInstanceListeners(dom);//safety 
       GEvent.addDomListener(dom,"click",function(event){cclick();});
       GEvent.addDomListener(dom,"mouseover",function(){cover();});
       GEvent.addDomListener(dom,"mouseout",function(){cout();});
   }
   else {
        //we could not paint because GMaps has not done its drawing yet, try a bit later
        var credraw = GEvent.callback(this,this.delayedRedraw);
        this.dredraw = window.setTimeout(function(force){credraw(true);},100); //the true here is vital
   }
}

//event handlers
BDCCPolyline.prototype.onClick = function(){
    GEvent.trigger(this,"click");
}
BDCCPolyline.prototype.onOver = function(){
    GEvent.trigger(this,"mouseover");
}
BDCCPolyline.prototype.onOut = function(){
    GEvent.trigger(this,"mouseout");
}

//once the shape has been drawn, we can modify it with these setX functions;

BDCCPolyline.prototype.setColor = function(color) {
    this.oColor = color;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.color = this.oColor;
    }
    else{
        dom.setAttribute("stroke",this.oColor);
    }
}
BDCCPolyline.prototype.getColor = function() {
    return this.oColor;
}
BDCCPolyline.prototype.setDash = function(dash) {
    this.dash = dash;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        if(this.dash == "dash")
            dom.stroke.dashstyle = "dash";       
        else if (this.dash == "dot")
            dom.stroke.dashstyle = "dot";    
        else 
            dom.stroke.dashstyle = "";    
    }
    else{
        if(this.dash == "dash")
            dom.setAttribute("stroke-dasharray","10,10");
        else if (this.dash == "dot")
            dom.setAttribute("stroke-dasharray","3,17");
        else
            dom.setAttribute("stroke-dasharray","");
    }
}
BDCCPolyline.prototype.getDash = function() {
    return this.dash;
}
//for VML only, use single, thinthin, thinthick, thickthin, thickbetweenthin
BDCCPolyline.prototype.setLineStyle = function(ls) {
    this.lineStyle = ls;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.linestyle = ls;
    }
}
BDCCPolyline.prototype.getLineStyle = function() {
    return this.lineStyle;
}


BDCCPolyline.prototype.setWeight = function(weight) {
    this.weight = weight;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.weight = this.weight.toString()+"px";   
    }
    else{
        dom.setAttribute("stroke-width",this.weight.toString()+"px");
    }
}
BDCCPolyline.prototype.getWeight = function() {
    return this.weight;
}
BDCCPolyline.prototype.setOpacity = function(opacity) {
    this.oOpacity = ((opacity >= 0.005)&&(opacity <= 0.015))?0.02:opacity;;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.opacity = this.oOpacity;
    }
    else{
        dom.setAttribute("stroke-opacity",this.oOpacity);
    }
}
BDCCPolyline.prototype.getOpacity = function() {
    return this.oOpacity;
}

// Subclassed VML/SVG Polyline
//
// Bill Chadwick May 2007
//
// Free for any use
//
// Adds 
//  click, mouseover and mouseout events
//  tooltip
//  dot or dash styling
//  dynamic setting of colour, opacity, weight and dash style  

var BDCCPolylineId = 0;//counter for unique DOM Ids

// Constructor params exactly as GPolyline then a tooltip and dash which must be one of "dot" or "dash" or "solid"

function BDCCPolyline(points, color, weight, opacity, tooltip, dash) {	
    
    this.tooltip = tooltip;
    this.dash = (dash != null) ? dash : "solid";
    this.oColor = color;
    this.weight = weight;
    this.oOpacity = ((opacity >= 0.005)&&(opacity <= 0.015))?0.02:opacity;//don't use our special value
    this.lineStyle = 'single';
    
    //make a unique id for this polyline 
    BDCCPolylineId += 1;
    var uid = BDCCPolylineId.toString(16); //initial color so we can find dom node
    while (uid.length < 6)
        uid = "0" + uid;
    this.uid = "#" + uid;
    this.domid = "BdccPolyline" + BDCCPolylineId.toString();//unique domId

    this.usesVml = (navigator.userAgent.indexOf("MSIE") != -1);
    
    //call super class constructor with temp colour and transperency for nearly invisible
    GPolyline.call(this,points,this.uid,weight,0.01,{"clickable":false}); 
}
BDCCPolyline.prototype = new GPolyline(new Array(new GLatLng(0,0)));//subclass from GPolyline

// According to the GMap docs, GPolyline implements the GOverlay interface
// That is it implements the functions initialize, remove, copy and redraw
// Here we add to GPolyline's own implementation of these functions
//

BDCCPolyline.prototype.initialize = function(map) {
    GPolyline.prototype.initialize.call(this,map); //super class
    //Initialise cant be used to cache the SVG path node or its parent svg node as both are recreated in redraw
    //For VML the shape node is recreated in redraw and all shapes have a common parent       
    this.map = map;       
}

BDCCPolyline.prototype.remove = function() {
    GPolyline.prototype.remove.call(this); //super class    
}

BDCCPolyline.prototype.copy = function(map) {
    return new BDCCPolyline(this.points,this.oColor,this.weight,this.oOpacity,this.tooltip,this.dash);
}

BDCCPolyline.prototype.redraw = function(force) {

   GPolyline.prototype.redraw.call(this,force); //super class
   
   //update later when the API's rendering has finished
   var credraw = GEvent.callback(this,this.delayedRedraw);
   
   if(this.dredraw)
       window.clearTimeout(this.dredraw); 
          
   this.dredraw = window.setTimeout(function(force){credraw(true);},100); //the true here is vital
}

BDCCPolyline.prototype.delayedRedraw = function(force) {
   
   
   var dom = null;
   var i;
   var prnt;

   //You could omit parent node checking if you only have one map per document
   //Doing so will give a performance improvement	  	    
   this.map.getPane(G_MAP_MAP_PANE).parentNode.id = "Cntnr" + this.domid;
    
   if(this.usesVml){
      
        try{        
            var shps = document.getElementsByTagName("shape"); 
	  	        
	  	    //find the one with our UID string as its colour + our 1% opacity
	  	    //and on the correct map       	
        	for(i=0;i<shps.length;i++){
            	dom = shps[i];
                if ((dom.stroke.color == this.uid) && 
                    (dom.stroke.opacity > 0.005) &&
                    (dom.stroke.opacity < 0.015)){
                    prnt = dom.parentNode;
                	while(prnt != null){
                        if (prnt.id == "Cntnr" + this.domid){
                            i = shps.length;
                            break;
                        }
                        else{
                            prnt = prnt.parentNode; 
                        }
                    }
                    if(i < shps.length)
                        dom = null;
                }
                else
                    dom = null;                                                	
        	}

            if(dom != null){
                //we found our VML node
                if(this.tooltip != null){
                    dom.style.cursor = "help";//to show mouseover 
                    dom.title = this.tooltip;
                }
                dom.id = this.domid;//assign unique DOM id so we can modify attributes later   
            }
        }
        catch (ex)
        {
		    if(BDCCPolylineId == 1)
			    alert("The designer of this Google Maps web page has attempted to use VML graphics without including the necessary header material.");
        }
   }
   else{
        var shps = document.getElementsByTagName("path"); 
	    i = shps.length-1;
	    
	    
  	    //find the one with our UID string as its colour + our 1% opacity
  	    //and on the correct map       	
    	for(i=0;i<shps.length;i++){
        	dom = shps[i];
            if ((dom.getAttribute("stroke") == this.uid) && 
                (dom.getAttribute("stroke-opacity") >= 0.005) &&
                (dom.getAttribute("stroke-opacity") <= 0.015)){
                prnt = dom.parentNode;
            	while(prnt != null){
                    if (prnt.id == "Cntnr" + this.domid){
                        i = shps.length;
                        break;
                    }
                    else{
                        prnt = prnt.parentNode; 
                    }
                }
                if(i < shps.length)
                    dom = null;
            }
            else
                dom = null;                                                	
    	}	    
		

        if(dom != null){
            //we found our SVG node

            if(this.tooltip != null){
                dom.style.cursor = "help";//to show mouseover 
                dom.setAttribute("title",this.tooltip);
            }
            dom.setAttribute("id",this.domid);//assign unique DOM id so we can modify attributes later
            dom.setAttribute("pointer-events","stroke");//only click on the line, not its bounding rectangle
        }
   }
   
   if(this.dredraw)
       window.clearTimeout(this.dredraw); 

   if(dom != null){
   
      //set up the appearance of our polyline
       this.setColor(this.oColor);
       this.setDash(this.dash);
       this.setOpacity(this.oOpacity);
       this.setWeight(this.weight);
       this.setLineStyle(this.lineStyle);
       
       //set up event handlers
       var cclick = GEvent.callback(this,this.onClick);
       var cover = GEvent.callback(this,this.onOver);
       var cout = GEvent.callback(this,this.onOut);
   
       GEvent.clearInstanceListeners(dom);//safety 
       GEvent.addDomListener(dom,"click",function(event){cclick();});
       GEvent.addDomListener(dom,"mouseover",function(){cover();});
       GEvent.addDomListener(dom,"mouseout",function(){cout();});
   }
   else {
        //we could not paint because GMaps has not done its drawing yet, try a bit later
        var credraw = GEvent.callback(this,this.delayedRedraw);
        this.dredraw = window.setTimeout(function(force){credraw(true);},100); //the true here is vital
   }
}

//event handlers
BDCCPolyline.prototype.onClick = function(){
    GEvent.trigger(this,"click");
}
BDCCPolyline.prototype.onOver = function(){
    GEvent.trigger(this,"mouseover");
}
BDCCPolyline.prototype.onOut = function(){
    GEvent.trigger(this,"mouseout");
}

//once the shape has been drawn, we can modify it with these setX functions;

BDCCPolyline.prototype.setColor = function(color) {
    this.oColor = color;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.color = this.oColor;
    }
    else{
        dom.setAttribute("stroke",this.oColor);
    }
}
BDCCPolyline.prototype.getColor = function() {
    return this.oColor;
}


BDCCPolyline.prototype.setDash = function(dash) {
    this.dash = dash;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        if(this.dash == "dash")
            dom.stroke.dashstyle = "dash";       
        else if (this.dash == "dot")
            dom.stroke.dashstyle = "dot";    
        else 
            dom.stroke.dashstyle = "";    
    }
    else{
        if(this.dash == "dash")
            dom.setAttribute("stroke-dasharray","10,10");
        else if (this.dash == "dot")
            dom.setAttribute("stroke-dasharray","3,17");
        else
            dom.setAttribute("stroke-dasharray","");
    }
}


BDCCPolyline.prototype.getDash = function() {
    return this.dash;
}


//for VML only, use single, thinthin, thinthick, thickthin, thickbetweenthin
BDCCPolyline.prototype.setLineStyle = function(ls) {
    this.lineStyle = ls;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.linestyle = ls;
    }
}
BDCCPolyline.prototype.getLineStyle = function() {
    return this.lineStyle;
}


BDCCPolyline.prototype.setWeight = function(weight) {
    this.weight = weight;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.weight = this.weight.toString()+"px";   
    }
    else{
        dom.setAttribute("stroke-width",this.weight.toString()+"px");
    }
}


BDCCPolyline.prototype.getWeight = function() {
    return this.weight;
}


BDCCPolyline.prototype.setOpacity = function(opacity) {
    this.oOpacity = ((opacity >= 0.005)&&(opacity <= 0.015))?0.02:opacity;;
    var dom = document.getElementById(this.domid); 
    if(!dom)
        return;
    if(this.usesVml){
        dom.stroke.opacity = this.oOpacity;
    }
    else{
        dom.setAttribute("stroke-opacity",this.oOpacity);
    }
}
BDCCPolyline.prototype.getOpacity = function() {
    return this.oOpacity;
}

