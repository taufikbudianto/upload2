/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polygon;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.enumeration.MapCalculationType;
import prosia.basarnas.web.model.map.TaskSearchArea;

/**
 *
 * @author Ismail
 */
public class TaskAreaKMLGenerator implements java.io.Serializable {
    
    private CommonUtil commonUtil = new CommonUtil();
    @Getter
    private Polygon polygonTaskArea;
    
    public TaskAreaKMLGenerator(TaskSearchArea taskSearchArea) {
        polygonTaskArea = new Polygon();
        if (taskSearchArea.getTypeTaskArea().equals(MapCalculationType.DRIFT_CALCULATION)) {
            List<LatLng> lines = SubSearchAreaCollection(taskSearchArea);
            for (LatLng line : lines) {
                polygonTaskArea.getPaths().add(line);
            }
            taskSearchArea.setVertexs(lines);
            polygonTaskArea.getPaths().add(lines.get(0));
        } else {
            polygonTaskArea.setPaths(taskSearchArea.getVertexs());
            polygonTaskArea.getPaths().add(taskSearchArea.getVertexs().get(0));
        }
        polygonTaskArea.setStrokeColor("#00FF00");
        polygonTaskArea.setFillColor("#000000");
        polygonTaskArea.setStrokeOpacity(3);
        polygonTaskArea.setStrokeWeight(3);
        polygonTaskArea.setFillOpacity(0.1);
    }
    
    private List<LatLng> SubSearchAreaCollection(TaskSearchArea taskSearchArea) {
        List<LatLng> listLatLng = new ArrayList<>();
        listLatLng.add(taskSearchArea.getUnrotateStart());
        listLatLng.add(new LatLng(listLatLng.get(0).getLat(), listLatLng.get(0).getLng()
                + (CommonConstant.ONE_NM_TO_DEG * taskSearchArea.getWidth())));
        listLatLng.add(new LatLng(listLatLng.get(1).getLat()
                - (CommonConstant.ONE_NM_TO_DEG * taskSearchArea.getHeight()), listLatLng.get(1).getLng()));
        listLatLng.add(new LatLng(listLatLng.get(2).getLat(), listLatLng.get(0).getLng()));
        listLatLng.add(taskSearchArea.getUnrotateStart());
        LatLng pivot = commonUtil.rotationLatLng(taskSearchArea.getPivotDatumPoint(),
                taskSearchArea.getUnrotatePivot(), taskSearchArea.getTiltDrift());
        listLatLng = commonUtil.rotationLatLngs(listLatLng, taskSearchArea.getPivotDatumPoint(), taskSearchArea.getTiltDrift());
        return listLatLng;
    }
}
