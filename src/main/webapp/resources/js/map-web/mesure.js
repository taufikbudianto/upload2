/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global PF, MeasureTool */

var map, measureTool;
function measurenew() {
    var map = PF('idnGmap').getMap();
    measureTool = new MeasureTool(map, {
        showSegmentLength: true,
        tooltip: true,
        unit: MeasureTool.UnitTypeId.METRIC // metric or imperial
    });
}
function measureStop() {
    var map = PF('idnGmap').getMap();
    measureTool = new MeasureTool(map, {
        contextMenu: false
    });
}
