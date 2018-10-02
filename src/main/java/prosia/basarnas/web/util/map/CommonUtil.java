/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;

/**
 *
 * @author Ismail
 */
public class CommonUtil implements Serializable {

    public List<LatLng> rotationLatLngs(List<LatLng> latlngs, LatLng poros, double degree) {
        List<LatLng> rotatLatLng = new ArrayList<>();
        for (LatLng ll : latlngs) {
            LatLng gll = rotationLatLng(poros, ll, degree);
            rotatLatLng.add(gll);
        }
        return rotatLatLng;
    }

    public LatLng rotationLatLng(LatLng poros, LatLng pi, double degree) {
        degree = otherwise(degree);
        double x_ = poros.getLng(), y_ = poros.getLat(),
                x = pi.getLng(), y = pi.getLat(),
                xGenerate, yGenerate,
                cos, sin;

        if (((toDeg(toRad(degree))) / 90) % 2 == 1) {
            cos = 0;
        } else {
            cos = Math.cos(toRad(degree));
        }
        sin = Math.sin(toRad(degree));

        xGenerate = (x * cos) - (y * sin) + (x_) - (x_ * cos) + (y_ * sin);
        yGenerate = (x * sin) + (y * cos) + (y_) - (x_ * sin) - (y_ * cos);
        return new LatLng(yGenerate, xGenerate);
    }

    //MATH
    private double otherwise(double value) {
        if (value > 0) {
            return value - (Math.abs(value) * 2);
        } else {
            return value + (Math.abs(value) * 2);
        }
    }

    private double toRad(double degree) {
        return degree * (Math.PI / 180);
    }

    public double toDeg(double radian) {
        return radian * (180 / Math.PI);
    }

    public LatLng getPivotPoint(List<LatLng> points) {
        double topLat = points.get(0).getLat();
        double bottomLat = topLat;
        double rightLng = points.get(0).getLng();
        double leftLng = rightLng;
        for (int i = 1; i < points.size(); ++i) {
            double z = points.get(i).getLat();
            if (z > topLat) {
                topLat = z;
            } else if (z < bottomLat) {
                bottomLat = z;
            }
            z = points.get(i).getLng();
            if (z > rightLng) {
                rightLng = z;
            } else if (z < leftLng) {
                leftLng = z;
            }
        }
        double lat = (topLat - bottomLat) / 2 + bottomLat;
        double lng = (rightLng - leftLng) / 2 + leftLng;
        return new LatLng(lat, lng);
    }

    public double getAngle(LatLng center, LatLng po, LatLng p1) {

        LatLng dpo = new LatLng(po.getLng() - center.getLng(), po.getLat() - center.getLat());

        LatLng dp1 = new LatLng(p1.getLng() - center.getLng(), p1.getLat() - center.getLat());

        double angleRad = Math.atan2(dpo.getLat(), dpo.getLng()) - Math.atan2(dp1.getLat(), dp1.getLng());
        double angleDeg = angleRad * 180 / Math.PI;
        return -angleRad;
    }
}
