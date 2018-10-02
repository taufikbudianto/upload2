/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.util.HashMap;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author Aris
 */
public class TrapeziumTaskAreaUtil {
    private TrapeziumSearchAreaUtil searchAreaUtil;
    private HashMap<UUID, TaskArea> taskAreaHashMap = new HashMap<UUID, TaskArea>();
    private Double trapeziumDiffIncrementInNm;
    private static double[] arrtrapeziumDiffIncrementInNm = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private LatLng currentStartPoint;
    private static LatLng[] arrcurrentStartPoint = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private LatLng currentEndPoint;
    private Double currentTaskAreaLengthInNm;
    private Double totalTaskAreaLengthInNm;
    private static double[] arrtotalTaskAreaLengthInNm = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private Double luasTaskArea;
    private TaskArea taskArea;
    private TaskArea taskAreaSplit1;
    private TaskArea taskAreaSplit2;
    private int ukuranSize = 0;

    public TrapeziumTaskAreaUtil(){
        
    }    
    public TrapeziumTaskAreaUtil(TrapeziumSearchAreaUtil trapeziumSearchAreaUtil) {
        searchAreaUtil = trapeziumSearchAreaUtil;
        trapeziumDiffIncrementInNm = Math.abs(searchAreaUtil.getBigL() - searchAreaUtil.getSmallL()) * 60 / searchAreaUtil.width();
        currentStartPoint = searchAreaUtil.smallSection();
        totalTaskAreaLengthInNm = Double.valueOf(0);
    }

    public TrapeziumTaskAreaUtil(TrapeziumSearchAreaUtil trapeziumSearchAreaUtil, int Size, boolean isInsert) {
        searchAreaUtil = trapeziumSearchAreaUtil;
        int i = Size;
        arrtrapeziumDiffIncrementInNm[i]= Math.abs(searchAreaUtil.arrGetBigL()[i] - searchAreaUtil.arrGetSmallL()[i]) * 60 / searchAreaUtil.arrWidth()[i];
        arrcurrentStartPoint[i] = searchAreaUtil.arrSmallSection()[i];
        arrtotalTaskAreaLengthInNm[i] = Double.valueOf(0);

    }
    public class TaskArea {

        private UUID taskAreaID = UUID.randomUUID();
        private LatLng startPoint;
        private LatLng endPoint;
        private LatLng smallCoord1;
        private LatLng smallCoord2;
        private LatLng bigCoord1;
        private LatLng bigCoord2;
        private Double taskAreaLength;
        private Double taskAreaZone;
        
        //<editor-fold defaultstate="collapsed" desc="Getter-Setter">

        public LatLng getBigCoord1() {
            return bigCoord1;
        }

        public void setBigCoord1(LatLng bigCoord1) {
            this.bigCoord1 = bigCoord1;
        }

        public LatLng getBigCoord2() {
            return bigCoord2;
        }

        public void setBigCoord2(LatLng bigCoord2) {
            this.bigCoord2 = bigCoord2;
        }

        public LatLng getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(LatLng endPoint) {
            this.endPoint = endPoint;
        }

        public LatLng getSmallCoord1() {
            return smallCoord1;
        }

        public void setSmallCoord1(LatLng smallCoord1) {
            this.smallCoord1 = smallCoord1;
        }

        public LatLng getSmallCoord2() {
            return smallCoord2;
        }

        public void setSmallCoord2(LatLng smallCoord2) {
            this.smallCoord2 = smallCoord2;
        }

        public LatLng getStartPoint() {
            return startPoint;
        }

        public void setStartPoint(LatLng startPoint) {
            this.startPoint = startPoint;
        }

        public UUID getTaskAreaID() {
            return taskAreaID;
        }

        public void setTaskAreaID(UUID taskAreaID) {
            this.taskAreaID = taskAreaID;
        }

        public Double getTaskAreaLength() {
            return taskAreaLength;
        }

        public void setTaskAreaLength(Double taskAreaLength) {
            this.taskAreaLength = taskAreaLength;
        }

        public Double getTaskAreaZone() {
            return taskAreaZone;
        }

        public void setTaskAreaZone(Double taskAreaZone) {
            this.taskAreaZone = taskAreaZone;
        }
        //</editor-fold>
    }

    public UUID addTaskArea(Double taskAreaLengthInNm, int ukuran) {
        ukuranSize = ukuran;
        currentTaskAreaLengthInNm = taskAreaLengthInNm;
        
            if (currentTaskAreaLengthInNm + arrtotalTaskAreaLengthInNm[ukuranSize] > searchAreaUtil.arrWidth()[ukuranSize]) {
                addMessage("Informasi", "Panjang task area tidak boleh melebihi panjang search area");
                return null;
            } else {
                taskArea = new TaskArea();
                taskAreaHashMap = new HashMap<>();
                    
                    LatLng point = new LatLng(arrcurrentStartPoint[ukuranSize].getLat(), arrcurrentStartPoint[ukuranSize].getLng() + taskAreaLengthInNm / 60);
                    currentEndPoint = MapCalculation.rotation(arrcurrentStartPoint[ukuranSize], point, searchAreaUtil.arrBigRotationDegree());
                    Double taskAreaL = searchAreaUtil.arrGetSmallL()[ukuranSize] + (arrtotalTaskAreaLengthInNm[ukuranSize] + currentTaskAreaLengthInNm) * arrtrapeziumDiffIncrementInNm[ukuranSize] / 60;
                    LatLng smallPoint = new LatLng(arrcurrentStartPoint[ukuranSize].getLat(), arrcurrentStartPoint[ukuranSize].getLng() + taskAreaL);
                    LatLng bigPoint = new LatLng(currentEndPoint.getLat(), currentEndPoint.getLng() + taskAreaL);
                    taskArea.startPoint = arrcurrentStartPoint[ukuranSize];
                    taskArea.endPoint = currentEndPoint;
                    taskArea.taskAreaLength = currentTaskAreaLengthInNm;
                    taskArea.smallCoord1 = MapCalculation.rotation(arrcurrentStartPoint[ukuranSize], smallPoint, searchAreaUtil.arrSmallRotationDegree() + 90);
                    taskArea.smallCoord2 = MapCalculation.rotation(arrcurrentStartPoint[ukuranSize], smallPoint, searchAreaUtil.arrSmallRotationDegree() - 90);
                    taskArea.bigCoord1 = MapCalculation.rotation(currentEndPoint, bigPoint, searchAreaUtil.arrBigRotationDegree() - 90);
                    taskArea.bigCoord2 = MapCalculation.rotation(currentEndPoint, bigPoint, searchAreaUtil.arrBigRotationDegree() + 90);
                    luasTaskArea = currentTaskAreaLengthInNm * taskAreaL * 2 * 60;
                    taskArea.taskAreaZone = luasTaskArea;
                    taskAreaHashMap.put(taskArea.taskAreaID, taskArea);
                    
                }
            
        return taskArea.taskAreaID;
    }
    
    public void splitTaskArea(TaskArea taskArea) {
        if (taskArea != null) {
            taskAreaHashMap.remove(taskArea.taskAreaID);
            taskAreaSplit1 = new TaskArea();
            taskAreaSplit1.startPoint = taskArea.startPoint;
            taskAreaSplit1.endPoint = taskArea.endPoint;
            taskAreaSplit1.taskAreaLength = taskArea.taskAreaLength;
            taskAreaSplit1.taskAreaZone = taskArea.taskAreaZone / 2;
            taskAreaSplit1.smallCoord1 = taskArea.smallCoord1;
            taskAreaSplit1.smallCoord2 = taskArea.startPoint;
            taskAreaSplit1.bigCoord1 = taskArea.bigCoord1;
            taskAreaSplit1.bigCoord2 = taskArea.endPoint;
            taskAreaHashMap.put(taskAreaSplit1.taskAreaID, taskAreaSplit1);
            taskAreaSplit2 = new TaskArea();
            taskAreaSplit2.startPoint = taskArea.startPoint;
            taskAreaSplit2.endPoint = taskArea.endPoint;
            taskAreaSplit2.taskAreaLength = taskArea.taskAreaLength;
            taskAreaSplit2.taskAreaZone = taskArea.taskAreaZone / 2;
            taskAreaSplit2.smallCoord1 = taskArea.startPoint;
            taskAreaSplit2.smallCoord2 = taskArea.smallCoord2;
            taskAreaSplit2.bigCoord1 = taskArea.endPoint;
            taskAreaSplit2.bigCoord2 = taskArea.bigCoord2;
            taskAreaHashMap.put(taskAreaSplit2.taskAreaID, taskAreaSplit2);
            luasTaskArea = luasTaskArea / 2;
        }
    }

    public void setNextTaskArea() {
        arrcurrentStartPoint[ukuranSize] = currentEndPoint;
        arrtotalTaskAreaLengthInNm[ukuranSize] =arrtotalTaskAreaLengthInNm[ukuranSize] + currentTaskAreaLengthInNm;
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void warnMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public HashMap<UUID, TaskArea> getTaskAreaHashMap() {
        return taskAreaHashMap;
    }

    public void setTaskAreaHashMap(HashMap<UUID, TaskArea> taskAreaHashMap) {
        this.taskAreaHashMap = taskAreaHashMap;
    }

    public Double getLuasTaskArea() {
        return luasTaskArea;
    }

    public void setLuasTaskArea(Double luasTaskArea) {
        this.luasTaskArea = luasTaskArea;
    }

    public LatLng getCurrentStartPoint() {
//        return currentStartPoint;
        return arrcurrentStartPoint[ukuranSize];
    }

    public void setCurrentStartPoint(LatLng currentStartPoint) {
        this.currentStartPoint = currentStartPoint;
    }

    public Double getTotalTaskAreaLengthInNm() {
//        return totalTaskAreaLengthInNm;
        return arrtotalTaskAreaLengthInNm[ukuranSize];
    }

    public void setTotalTaskAreaLengthInNm(Double totalTaskAreaLengthInNm) {
        this.totalTaskAreaLengthInNm = totalTaskAreaLengthInNm;
    }
    
    public void clearMemory(){
        try{
            for(int i=0 ; i < 20; i++){
                arrtrapeziumDiffIncrementInNm[i] = 0;
                arrcurrentStartPoint[i] = null;
                arrtotalTaskAreaLengthInNm[i] = 0;
            }
            
        }catch(Exception ex){
            
        }
    }
}
