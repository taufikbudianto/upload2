/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.map.LatLng;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.web.model.map.GenerateValueRequestBean;

/**
 *
 * @author Ismail
 */
public class SubSearchAreaCollection implements java.io.Serializable {

    private CommonUtil commonUtil = new CommonUtil();
    private LatLng pivot;
    private double radius;
    private double tilt;

    public SubSearchAreaCollection(LatLng pivot, double radius, double tilt) {
        this.pivot = pivot;
        this.radius = radius;
        this.tilt = tilt;
    }

    private List<LatLng> SubSearchAreaCollection(GenerateValueRequestBean requestBean) {
        List<LatLng> listLatLng = new ArrayList<>();
        listLatLng.add(requestBean.getStartTaskSearchArea());
        listLatLng.add(new LatLng(listLatLng.get(0).getLat(), listLatLng.get(0).getLng()
                + (CommonConstant.ONE_NM_TO_DEG * requestBean.getWidthTaskSearchArea())));
        listLatLng.add(new LatLng(listLatLng.get(1).getLat()
                - (CommonConstant.ONE_NM_TO_DEG * requestBean.getHeightTaskSearchArea()), listLatLng.get(1).getLng()));
        listLatLng.add(new LatLng(listLatLng.get(2).getLat(), listLatLng.get(2).getLng()));
        listLatLng.add(requestBean.getStartTaskSearchArea());
        LatLng pivot = commonUtil.rotationLatLng(this.pivot,
                requestBean.getPivotTaskSearchArea(), tilt);
        listLatLng = commonUtil.rotationLatLngs(listLatLng, pivot, tilt);
        return listLatLng;
    }
}
