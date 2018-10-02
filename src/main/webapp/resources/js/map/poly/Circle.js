/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function Circle(pivot, radius){
    if(!pivot || !radius) return;
    this.pivot = pivot;
    this.radius = radius;
    this.radius = this.radius * 1.825;
    this.draw();
}

Circle.prototype.draw = function(){
    // Degrees to radians 
    var d2r = Math.PI / 180;
    // Radians to degrees
    var r2d = 180 / Math.PI;
    // Earth radius is 3,963 miles => 6399.592 Km
    var cLat = (this.radius / 6378.8) * r2d;
    var cLng = cLat / Math.cos(this.pivot.lat() * d2r);
 
    // Store points in array 
    var points = [];
 
    // Calculate the points
    // Work around 360 points on circle
    //for (var i=0; i < 360; i++) {
    for (var i=-1; i < 41; i++) {
        //var theta = Math.PI * (i/16);
        var theta = Math.PI * (i/(41/2));
        // Calculate next X point
        var circleX = this.pivot.lng() + (cLng * Math.cos(theta));            
        // Calculate next Y point 
        var circleY = this.pivot.lat() + (cLat * Math.sin(theta));
        // Add point to array 
        points.push(new GPoint(circleX, circleY));
    //points.push(new GLatLng(circleX, circleY));	
    }
		
    this.poly = new GPolygon(points,"#FF0000", 3, 1,"#000000",0.1,{
        clickable: false
    });
    map.addOverlay(this.poly);    
}
