/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import org.primefaces.model.map.LatLng;
import prosia.basarnas.web.model.map.GTaskSearchArea;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;

/**
 *
 * @author Ismail
 */
public class Serializable {

    //state dan trackSpaing di define dari luar
    public static GenerateValueRequestBean createGenerateValueRequest(DriftCalcWorksheet1Result worksheet1Result,
            GTaskSearchArea gTaskSearchArea) {
        GenerateValueRequestBean requestBean = new GenerateValueRequestBean();
        requestBean.setParrentId(worksheet1Result.getParrentID());
        if (worksheet1Result != null) {
            requestBean.setShape(worksheet1Result.getShape());
            requestBean.setDrawLeewayLine(worksheet1Result.getDrawLeewayLine());
            LatLng datumPoint = new LatLng(worksheet1Result.getDatumLatLng().getLatNumb(), worksheet1Result.getDatumLatLng().getLngNumb());
            LatLng lkpPoint = new LatLng(worksheet1Result.getLkpLatLng().getLatNumb(), worksheet1Result.getLkpLatLng().getLngNumb());
            LatLng driftLeftPoint = new LatLng(worksheet1Result.getDriftLeftLatLng().getLatNumb(), worksheet1Result.getDriftLeftLatLng().getLngNumb());
            LatLng driftRightPoint = new LatLng(worksheet1Result.getDriftRightLatLng().getLatNumb(), worksheet1Result.getDriftRightLatLng().getLngNumb());
            requestBean.setDatumPoint(datumPoint);
            requestBean.setLkpPoint(lkpPoint);
            requestBean.setDriftLeftPoint(driftLeftPoint);
            requestBean.setDriftRightPoint(driftRightPoint);
            requestBean.setLkpToDatumAngle(worksheet1Result.getLkpToDatumAngle());
            requestBean.setLkpToDatumDistance(worksheet1Result.getLkpToDatumDistance());
            requestBean.setRadiusDrift(worksheet1Result.getRadius());
            requestBean.setTiltDrift(worksheet1Result.getTilt());
            System.out.println("[datum point] :" + datumPoint);
            System.out.println("[datum left leeway] :" + driftLeftPoint);
            System.out.println("[datum rigth leeway] :" + driftRightPoint);
            System.out.println("[datum lkp angle leeway] :" + worksheet1Result.getLkpToDatumAngle());
            System.out.println("[datum lkp distance] :" + worksheet1Result.getLkpToDatumDistance());
            System.out.println("[radius] :" + worksheet1Result.getRadius());
            System.out.println("[tilt] :" + worksheet1Result.getTilt());
        }
        if (gTaskSearchArea != null) {
            System.out.println(gTaskSearchArea);
            
//            System.out.println(gTaskSearchArea.getPivot());
//            LatLng pivotTaskSearchArea = new LatLng(gTaskSearchArea.getPivot().getLatNumb(), gTaskSearchArea.getPivot().getLngNumb());
            LatLng pivotTaskSearchArea = new LatLng(
                    gTaskSearchArea.getUnrotatePivot().getLatNumb(),
                    gTaskSearchArea.getUnrotatePivot().getLngNumb());
            requestBean.setPivotTaskSearchArea(pivotTaskSearchArea);
            LatLng startTaskSearchArea = new LatLng(
                    gTaskSearchArea.getUnrotateStart().getLatNumb(),
                    gTaskSearchArea.getUnrotateStart().getLngNumb());
            requestBean.setStartTaskSearchArea(startTaskSearchArea);
            requestBean.setWidthTaskSearchArea(gTaskSearchArea.getWidth());
            requestBean.setHeightTaskSearchArea(gTaskSearchArea.getHeight());
            requestBean.setTiltTaskSearchArea(gTaskSearchArea.getTilt());
            requestBean.setTrackSpacing(gTaskSearchArea.getTrackSpacing());
            System.out.println("[pivot task search area]" + pivotTaskSearchArea);
            System.out.println("[task searcharea width]" + gTaskSearchArea.getWidth());
            System.out.println("[task search area height]" + gTaskSearchArea.getHeight());
            System.out.println("[task search area tilt]" + gTaskSearchArea.getTilt());
        }
        return requestBean;
    }
}
