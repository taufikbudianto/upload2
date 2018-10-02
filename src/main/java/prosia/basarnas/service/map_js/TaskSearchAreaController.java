/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import lombok.Data;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import prosia.basarnas.constant.CommonConstant;
import prosia.basarnas.model.IncidentAsset;
import prosia.basarnas.web.controller.map.googleapi.GLatLng;
import prosia.basarnas.web.controller.map.googleapi.MapController;
import prosia.basarnas.web.util.CalculationInterface;
import prosia.basarnas.web.util.DriftCalcWorksheet1Result;
import prosia.basarnas.web.model.map.GTaskSearchArea;

/**
 *
 * @author Aris
 */
@Data
public class TaskSearchAreaController implements Disposable {

    private Map<Integer, GTaskSearchArea> mapTaskSearchArea = new HashMap<Integer, GTaskSearchArea>();
    private MapController mapController;
    //private TaskSearchAreaNavigator navigator;

    private ActionListener actList;
    private MouseListener mouseListener;
    private final static Logger logger = Logger.getLogger(TaskSearchAreaController.class);
    private GTaskSearchArea taskSearchArea;
    
    /*
    public TaskSearchAreaController(TaskSearchAreaNavigator navigator, MapController mapController) {
        this.mapController = mapController;
        this.navigator = navigator;
        initEvent();
    }
     */
    public TaskSearchAreaController(MapController mapController) {
        this.mapController = mapController;
        initEvent();
    }

    private void initEvent() {
        actList = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                if(e.getSource().equals(navigator.getBtnClear())){
                    clearActionPerformed(e);
                }else if(e.getSource().equals(navigator.getBtnRemoveLast())){
                    removeLastActionPerformed(e);
                }
                 */
            }
        };
        mouseListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                listOnClick(e);
            }
        };
        /*
        this.navigator.getBtnClear().addActionListener(actList);
        this.navigator.getBtnRemoveLast().addActionListener(actList);
        this.navigator.getList().addMouseListener(mouseListener);
         */
    }

    /**
     * from Worksheet2Controller and Worksheet8Controller
     *
     * interface methode ke Worksheet2Controller dan Workshet8Controller untuk
     * membuat task area
     *
     * @param incidentAsset sru yang diassign
     * @param luas area yang bisa dicover oleh sru
     * @param s trackspacing
     * @param type dari radius 1 atau 1/2
     * @param name nama dari task area
     */
    public void createNewAndAddToNavigator(IncidentAsset incidentAsset, double luas, double s, 
            int radiusType, String name, String driftTaskAreaID, prosia.basarnas.web.util.TaskSearchAreaController taskSearchAreaControllerUtil, 
            DriftCalcWorksheet1Result worksheetResult ) {
        if (radiusType == 0) {
            CalculationInterface ci = taskSearchAreaControllerUtil.insertHalfRadius(luas);
            if (ci == null) {
                addMessage("Informasi", "Tidak dapat menampilkan Task Search Area. Luas Task Search Area melebihi kapasitas Search Area");
            } else {
                createGTaskByCI(ci, incidentAsset, s, name, radiusType, driftTaskAreaID, worksheetResult);
            }
            System.out.println("---createNewAndAddToNavigator 96---" + ci);
        } else {
            CalculationInterface ci = taskSearchAreaControllerUtil.insertOneRadius(luas);
            System.out.println("---createNewAndAddToNavigator 99---" + ci);
            if (ci == null) {
                addMessage("Informasi", "Tidak dapat menampilkan Task Search Area. Luas Task Search Area melebihi kapasitas Search Area");
            } else {
                createGTaskByCI(ci, incidentAsset, s, name, radiusType, driftTaskAreaID, worksheetResult);
            }
        }

    }

    /**
     * from createNewAndAddToNavigator
     *
     * @param ci hasil calculasi dari task search area controller untuk
     * mendapatkan nilai tinggi dari task search area yang akan di tambah ke map
     * @param sru yang diassign untuk task search area tsb
     * @param s track spacing yang diasumsikan untuk task search area tsb
     * @param name nama untuk sitask search area
     * @param type radius apakah 1 atau 1/2
     */
    private void createGTaskByCI(CalculationInterface ci, IncidentAsset sru, Double s, String name, int radiusType, String driftTaskAreaID, DriftCalcWorksheet1Result worksheetResult) {

        String parrentID = worksheetResult.getParrentID();
        String childID = GPoly.generateParrentID();
        Double latPivot = ci.getUnrotateStart().getLatNumb() - (ci.getHeight() / 2) * CommonConstant.ONE_NM_TO_DEG;
        Double lngPivot = ci.getUnrotateStart().getLngNumb() + (ci.getWidth() / 2) * CommonConstant.ONE_NM_TO_DEG;
        GLatLng unrotatePivot = new GLatLng(latPivot, lngPivot);
        System.out.println("---parrentID---" + parrentID);
        System.out.println("---childID---" + childID);
        System.out.println("---latPivot---" + latPivot);
        System.out.println("---lngPivot---" + lngPivot);
        System.out.println("---unrotatePivot---" + unrotatePivot);
        GTaskSearchArea taskSearchArea = new GTaskSearchArea(null, ci.getUnrotateStart(), unrotatePivot, sru, ci.getWidth(), ci.getHeight(), s, radiusType, name, parrentID, childID, driftTaskAreaID);
        add(taskSearchArea);

    }

    /**
     * from createGTaskByCI
     *
     * @param taskSearchArea Object baru GTaskSearchArea yang akan di tambahkan
     * ke list navigator
     *
     */
    public void add(GTaskSearchArea taskSearchAreaParam) {
        taskSearchAreaParam.setID(sequenceID());
        mapTaskSearchArea.put(taskSearchAreaParam.getID(), taskSearchAreaParam);
        
        //navigator.addItemToList(taskSearchArea);
        //mapController.getForm().getLayerControlWarpper().toFront(LayerControlWarpper.TASK_AREA_NAVIGATOR_NAME);
    }

    /**
     * methode ini digunakan untuk menambahkan object task search area yang
     * berasal dari trapezium search area hapus Object task area yang ada pada
     * Navigator task area jika ia memiliki TrapeziumTaskAreaID yang sama. lalu
     * baru dipasang ke Task Area Navigator
     *
     * @param newTask task area yang baru yang akan divalidasi apakah sudah
     * existing jika iya Objebct lama akan dihapus lalu instance yang ditampung
     * oleh parameter ini yang akan ditambahkan ke Navigator
     */
    public void removeExistingAndAddNew(GTaskSearchArea newTask) {
        /*
        for (int i = 0; i < navigator.getListModel().getSize(); ++i) {
            try {
                GTaskSearchArea existTask = (GTaskSearchArea) navigator.getListModel().getElementAt(i);
            if (existTask.getTrapeziumTaskAreaID().equals(newTask.getTrapeziumTaskAreaID())) {
                mapTaskSearchArea.remove(existTask.getID());
                mapController.getForm().exec(existTask.removeScript());
                navigator.getListModel().remove(i);
                break;
            }
            } catch (Exception e) {
                logger.info("task search area controller removeExistingAndAddNew()", e);
            }
        }
        add(newTask);
         */
    }

    /**
     * remove TaskSearchArea berdasarkan TrapeziumTaskAreaID-nya
     *
     * @param ids Collection TrapeziumTaskAreaID dari task search area yang akan
     * dihapus.
     */
    public void removeByTrapeziumTaskAreaID(String... ids) {
        /*
        int listCount = navigator.getListModel().getSize();
        for (int i = listCount - 1; i > -1; --i) {
            System.out.println(i);
            GTaskSearchArea existTask = (GTaskSearchArea) navigator.getListModel().getElementAt(i);
            for (String id : ids) {
                if (existTask.getTrapeziumTaskAreaID().equals(id)) {
                    mapTaskSearchArea.remove(existTask.getID());
                    mapController.getForm().exec(existTask.removeScript());
                    navigator.getListModel().remove(i);
                    break;
                }
            }
        }
         */
    }

    public void removeLast() {
        //remove 
        /*
        int lastIndex = navigator.getListModel().getSize() - 1;
        GTaskSearchArea removedTaskSearchArea = (GTaskSearchArea) navigator.getListModel().getElementAt(lastIndex);
        if (removedTaskSearchArea.getState() == GTaskSearchArea.InstantiateType.CREATE_NEW) {
            try {
                mapController.getCurrentDriftCalculation().getTaskSearchAreaController().removeLast();
                mapTaskSearchArea.remove(removedTaskSearchArea.getID());
                mapController.getForm().exec(removedTaskSearchArea.removeScript());
                navigator.getListModel().remove(lastIndex);
            } catch (Exception e) {
                //jika Intance Drift calculation belum dibuat maka akan menghasilkan error null pointer exeptions
                //proses langsung dikeluarkan
                return;
            }
        } else {
            mapTaskSearchArea.remove(removedTaskSearchArea.getID());
            mapController.getForm().exec(removedTaskSearchArea.removeScript());
            navigator.getListModel().remove(lastIndex);
        }
         */
    }

    public void clearActionPerformed(ActionEvent e) {
        if (isEmpty()) {
            //JOptionPane.showMessageDialog(mapController.getForm(), "Tidak terdapat Task Search Area pada list !", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        /*
        int clearComfirmation = JOptionPane.showConfirmDialog(mapController.getForm(), "Apakah anda yakin akan menghapus seluruh Task Search Area dari peta ?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (clearComfirmation == JOptionPane.YES_OPTION) {
            clearAllElement();
        }
         */
    }

    public void removeLastActionPerformed(ActionEvent e) {
        /*
        if (!navigator.getListModel().isEmpty() && JOptionPane.showConfirmDialog(Main.mapForm, "Apakah anda yakin akan menghapus Task Search Area yang terakhir ? ", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            removeLast();
        }
         */
    }

    public void clearAllElement() {
        /*
        navigator.clearItemsFromList();
        mapTaskSearchArea.clear();
        try {
            mapController.getCurrentDriftCalculation().getTaskSearchAreaController().clear();
        } catch (Exception e) {
        }
         */
    }

    public boolean isEmpty() {
        return false;
        //return navigator.getListModel().isEmpty();
    }

    /**
     * check apakah navigator task search area memiliki Object GTaskSearchArea
     * yang bertype CreateNew
     *
     * @see AutoCreateSearchPatternController createRelativeTaskSearchArea
     */
    public boolean isEmptyCreateNewType() {
        for (GTaskSearchArea taskArea : mapTaskSearchArea.values()) {
            if (taskArea.getState() == GTaskSearchArea.InstantiateType.CREATE_NEW) {
                return false;
            }
        }
        return true;
    }

    /**
     * check apakah navigator task search area memiliki Object GTaskSearchArea
     * yang bertype TrapeziumComeForm dan LOAD_FROM_DATABASE
     *
     * @see AutoCreateSearchPatternController createRelativeTaskSearchArea
     */
    public boolean isEmptyTrapeziumComeAndLoadFormDatabaseFrom() {
        for (GTaskSearchArea taskArea : mapTaskSearchArea.values()) {
            if (taskArea.getState() == GTaskSearchArea.InstantiateType.TRAPEZIUM_COME_FROM || taskArea.getState() == GTaskSearchArea.InstantiateType.VIEW_FORM_DATABASE) {
                return false;
            }
        }
        return true;
    }

    public GTaskSearchArea getSelectedObject() {
        return null;
        //return navigator.getSelectedObject();
    }

    private int sequenceID() {
        int id = mapTaskSearchArea.size() + 1;
        return id;
    }

    private void listOnClick(MouseEvent mouseEvent) {
        /*
        if (mouseEvent.getClickCount() == 2) {
            int index = navigator.getList().locationToIndex(mouseEvent.getPoint());
            if (index >= 0) {
                Object o = navigator.getList().getModel().getElementAt(index);
                GTaskSearchArea taskSearchArea = (GTaskSearchArea) o;
            }
        }
         */
    }

    public Map<Integer, GTaskSearchArea> getMapTaskSearchArea() {
        return mapTaskSearchArea;
    }

    /**
     * Methode ini akan mengembalikan List kumpulan Task Area yang ada pada
     * TaskArea navigator dengan type tertentu yang ditentukan oleh
     * <param>type</param>
     *
     * @param type GTaskSearchArea.InstantiateType
     * @return List<GTaskSearchArea> yang terdapat pada Task Search Area
     * navigator dengan type tertentu berdasarkan parameter
     */
    public List<GTaskSearchArea> getListByType(GTaskSearchArea.InstantiateType type) {
        List<GTaskSearchArea> result = new ArrayList<GTaskSearchArea>();
        for (GTaskSearchArea gTaskSearchArea : mapTaskSearchArea.values()) {
            if (gTaskSearchArea.getState() == type) {
                result.add(gTaskSearchArea);
            }
        }
        return result;
    }

    /**
     * Methode ini akan mengembalikan List kumpulan Task Area yang ada pada
     * TaskArea navigator dengan type tertentu yang ditentukan oleh
     * <param>type1, tyep2</param> sama dengan methode
     * <code>getListByType(GTaskSearchArea.InstantiateType type)</code> namun
     * methode ini dengan 2 parameter type
     *
     * @param type1 GTaskSearchArea.InstantiateType
     * @param type2 GTaskSearchArea.InstantiateType
     * @return List<GTaskSearchArea> yang terdapat pada Task Search Area
     * navigator dengan type tertentu berdasarkan parameter
     */
    public List<GTaskSearchArea> getListByTypes(GTaskSearchArea.InstantiateType type1, GTaskSearchArea.InstantiateType type2) {
        List<GTaskSearchArea> result = new ArrayList<GTaskSearchArea>();
        for (GTaskSearchArea gTaskSearchArea : mapTaskSearchArea.values()) {
            if (gTaskSearchArea.getState() == type1 || gTaskSearchArea.getState() == type2) {
                result.add(gTaskSearchArea);
            }
        }
        return result;
    }

    public List<GTaskSearchArea> getList() {
        List<GTaskSearchArea> result = new ArrayList<GTaskSearchArea>();
        for (GTaskSearchArea gTaskSearchArea : mapTaskSearchArea.values()) {
            result.add(gTaskSearchArea);
        }
        return result;
    }

    @Override
    public void dumpResources() throws Exception {
        /*
        this.navigator.getBtnClear().removeActionListener(actList);
        this.navigator.getBtnRemoveLast().removeActionListener(actList);
        this.navigator.getList().removeMouseListener(mouseListener);
        mapTaskSearchArea.clear();
        mapTaskSearchArea = null;
        mapController = null;
        navigator = null;
        actList = null;
        mouseListener = null;
         */
    }

    public void addMessage(String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
//        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void warnMessage(String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
//        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
