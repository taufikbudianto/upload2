/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentLog;
import prosia.basarnas.model.IncidentPOB;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstPosSar;
import prosia.basarnas.model.ResAsset;
import prosia.basarnas.model.ResPotency;
import prosia.basarnas.model.UtiBeaconComposite;
import prosia.basarnas.model.UtiBeaconElemental;
import prosia.basarnas.model.UtiSighting;
import prosia.basarnas.repo.IncidentLogRepo;
import prosia.basarnas.repo.IncidentRepo;
import prosia.basarnas.repo.IncidentTabPOBRepo;
import prosia.basarnas.repo.MstKantorSarRepo;
import prosia.basarnas.repo.MstPosSarRepo;
import prosia.basarnas.repo.ResAssetRepo;
import prosia.basarnas.repo.ResPotencyRepo;
import prosia.basarnas.repo.SightingAndHearingRepo;
import prosia.basarnas.repo.UtiBeaconCompositeRepo;
import prosia.basarnas.repo.UtiBeaconElementalsRepo;
import prosia.basarnas.web.controller.MapMBean;
import prosia.basarnas.web.model.Document;

/**
 *
 * @author Taufik AB
 */
@Component
@Scope("view")
public @Data
class MappingMBean extends AbstractManagedBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private IncidentRepo incidentRepo;
    @Autowired
    private MstKantorSarRepo mstKantorSarRepo;
    @Autowired
    private MstPosSarRepo mstPosSarRepo;
    @Autowired
    private MapMBean map;
    @Autowired
    private ResAssetRepo asetRepo;
    @Autowired
    private ResPotencyRepo potensiRepo;
    @Autowired
    private IncidentTabPOBRepo pobRepo;
    @Autowired
    private UtiBeaconCompositeRepo compositeRepo;
    @Autowired
    private UtiBeaconElementalsRepo elementalRepo;
    @Autowired
    private IncidentLogRepo logRepo;
    @Autowired
    private SightingAndHearingRepo sightingRepo;

    private String centerGeoMap;
    private Integer zoomGeoMap;

    private List<Incident> listIncident;
    private List<MstKantorSar> listKansar;
    private List<MstPosSar> listPossar;
    private List<ResAsset> listAset;
    private List<ResPotency> listPotensi;
    private List<IncidentPOB> listPob;
    private List<UtiBeaconComposite> listComposite;
    private List<UtiBeaconElemental> listElemental;
    private List<IncidentLog> listLog;
    private List<UtiSighting> listSighting;

    private TreeNode root;
    private TreeNode selectedTreeRouting;
    private MapModel mapModel = new DefaultMapModel();

    private Incident incident;
    private MstKantorSar kantorsar;
    private MstPosSar possar;
    private ResAsset aset;
    private ResPotency potensi;
    private IncidentPOB pob;
    private UtiBeaconComposite composite;
    private UtiBeaconElemental elemental;
    private IncidentLog incLog;
    private UtiSighting sighting;
    
    public MappingMBean() {
        listIncident = new ArrayList<>();
        listKansar = new ArrayList<>();
        listPossar = new ArrayList<>();
        listAset = new ArrayList<>();
        incident = new Incident();
        possar = new MstPosSar();
        kantorsar = new MstKantorSar();
        aset = new ResAsset();
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        root = new DefaultTreeNode(new Document("Files", null, "files"), null);
        setData(true, false);

    }

    public void setData(Boolean all, Boolean clear) {
        try {
            TreeNode object = new DefaultTreeNode("folder", new Document("Geography Object", null, "root"), root);

            //Kansar
            TreeNode kansar = new DefaultTreeNode("kansar", new Document("Kantor SAR", null, "kansar"), object);
            if (all) {
                listKansar = mstKantorSarRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listKansar = new ArrayList<>();
            }
            for (MstKantorSar mstKansar : listKansar) {
                TreeNode kansarAll = new DefaultTreeNode("kansardetail", new Document(mstKansar.getKantorsarname(), mstKansar, "kansar"), kansar);
            }

            TreeNode possar = new DefaultTreeNode("possar", new Document("Pos SAR", null, "possar"), object);
            if (all) {
                listPossar = mstPosSarRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listPossar = new ArrayList<>();
            }
            for (MstPosSar mstPossar : listPossar) {
                TreeNode PossarAll = new DefaultTreeNode("possardetail", new Document(mstPossar.getPossarname(), mstPossar, "possar"), possar);
            }
            TreeNode distress = new DefaultTreeNode("distress", new Document("Beacon Distress", null, "distress"), object);
            TreeNode distressComposite = new DefaultTreeNode("composite", new Document("Beacon Composite", null, "composite"), distress);
            if (all) {
                listComposite = compositeRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listComposite = new ArrayList<>();
            }
            for (UtiBeaconComposite composite : listComposite) {
                TreeNode CompositeAll = new DefaultTreeNode("compositedetail", new Document(composite.getBeaconid(), composite, "composite"), distressComposite);
            }
            TreeNode distressElemental = new DefaultTreeNode("elemental", new Document("Beacon Elemental", null, "elemental"), distress);
            if (all) {
                listElemental = elementalRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listElemental = new ArrayList<>();
            }
            for (UtiBeaconElemental elemental : listElemental) {
                TreeNode ElementalAll = new DefaultTreeNode("elementaldetail", new Document(elemental.getBeaconID(), elemental, "elemental"), distressElemental);
            }
            TreeNode sh = new DefaultTreeNode("sh", new Document("Sighting &  Hearing", null, "sh"), object);
             if (all) {
                listSighting = sightingRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listSighting = new ArrayList<>();
            }
            for (UtiSighting sight : listSighting) {
                TreeNode SightingAll = new DefaultTreeNode("shdetail", new Document(sight.getObserverName(), sight, "sh"), sh);
            }
            //Insiden
            TreeNode insiden = new DefaultTreeNode(new Document("Insiden", null, "incident"), object);
            listIncident = incidentRepo.findAllIncidentOpenLatLongNotNull();
            if (!listIncident.isEmpty()) {
                for (Incident incident : listIncident) {
                    TreeNode detailInsiden = new DefaultTreeNode("incident", new Document(incident.getIncidentnumber(), incident, "incident"), insiden);
                    // setLatLong(new Document("", incident, ""), "incident");
                }
            }
            TreeNode asset = new DefaultTreeNode("asset", new Document("Asset", null, "asset"), object);
            if (all) {
                listAset = asetRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listAset = new ArrayList<>();
            }
            for (ResAsset aset : listAset) {
                TreeNode AssetAll = new DefaultTreeNode("assetdetail", new Document(aset.getName(), aset, "asset"), asset);
            }
            TreeNode potensi = new DefaultTreeNode("potensi", new Document("Potensi", null, "potensi"), object);
            if (all) {
                listPotensi = potensiRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listPotensi = new ArrayList<>();
            }
            for (ResPotency potensy : listPotensi) {
                TreeNode PotensiAll = new DefaultTreeNode("potensidetail", new Document(potensy.getPotencyname(), potensy, "potensi"), potensi);
            }
            TreeNode log = new DefaultTreeNode("log", new Document("Log", null, "log"), object);
            if (all) {
                listLog = logRepo.findAllByLatLongNotNull();
            }
            if (clear) {
                listLog = new ArrayList<>();
            }
            for (IncidentLog logInc : listLog) {
                TreeNode LogAll = new DefaultTreeNode("logdetail", new Document(logInc.getLogID(), logInc, "log"), log);
            }
            TreeNode pob = new DefaultTreeNode("pob", new Document("Pob", null, "pob"), object);
            if (all) {
                listPob = pobRepo.findAllByLatLongNotNull();
//                listPob = pobRepo.findAll();
            }
            if (clear) {
                listPob = new ArrayList<>();
            }
            for (IncidentPOB incPob : listPob) {
                TreeNode PobAll = new DefaultTreeNode("pobdetail", new Document(incPob.getName(), incPob, "pob"), pob);
            }
        } catch (NullPointerException e) {
            log.info(e.getMessage());
        }
    }

    public void displayAll() {
        setData(true, false);
    }

    public void showTreeRoutingProperties() {
        try {
            Document d = (Document) selectedTreeRouting.getData();
            if (d.getType().equals("kansar")) {
                kantorsar = (MstKantorSar) d.getObjClass();
                setLatLong(d, "kansar");
            }
            if (d.getType().equals("possar")) {
                possar = (MstPosSar) d.getObjClass();
                setLatLong(d, "possar");
            }
            if (d.getType().equals("asset")) {
                aset = (ResAsset) d.getObjClass();
                setLatLong(d, "asset");
            }
            if (d.getType().equals("potensi")) {
                potensi = (ResPotency) d.getObjClass();
                setLatLong(d, "potensi");
            }
            if (d.getType().equals("elemental")) {
                elemental = (UtiBeaconElemental) d.getObjClass();
                setLatLong(d, "elemental");
            }
            if (d.getType().equals("composite")) {
                composite = (UtiBeaconComposite) d.getObjClass();
                setLatLong(d, "composite");
            }
            if (d.getType().equals("pob")) {
                pob = (IncidentPOB) d.getObjClass();
                setLatLong(d, "pob");
            }
            if (d.getType().equals("log")) {
                incLog = (IncidentLog) d.getObjClass();
                setLatLong(d, "log");
            }
             if (d.getType().equals("sh")) {
                sighting = (UtiSighting) d.getObjClass();
                setLatLong(d, "sh");
            }
            if (d.getType().equals("incident")) {
                incident = (Incident) d.getObjClass();
                setLatLong(d, "incident");
                centerGeoMap = incident.getLatitude() + "," + incident.getLongitude();
                map = new MapMBean();
                map.setCenterGeoMap(centerGeoMap);
                map.setZoomGeoMap(7);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private void setLatLong(Document d, String param) {
        String lat = "0";
        String longitude = "0";
        Incident inc;
        MstKantorSar mstKansar;
        MstPosSar mstPossar;
        ResAsset aset;
        String iconPath = "";
        if (param.equals("incident")) {
            inc = (Incident) d.getObjClass();
            lat = inc.getLatitude();
            longitude = inc.getLongitude();
            switch (inc.getIncidenttype()) {
                case 1:
                    iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/insiden-ply-red.png");
                    break;
                case 2:
                    iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/insiden-pnb-red.png");
                    break;
                case 3:
                    iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/insiden-musibah-red.png");
                    break;
                case 4:
                    iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/insiden-bencana-red.png");
                    break;
                default:
                    iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/incident_arrow.png");
                    break;
            }
        }
        if (param.equals("possar")) {
            mstPossar = (MstPosSar) d.getObjClass();
            lat = mstPossar.getLatitude();
            longitude = mstPossar.getLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/basarnas.png");
        }
        if (param.equals("kansar")) {
            mstKansar = (MstKantorSar) d.getObjClass();
            lat = mstKansar.getLatitude();
            longitude = mstKansar.getLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/pos_sar.png");

        }
        if (param.equals("asset")) {
            aset = (ResAsset) d.getObjClass();
            lat = aset.getLatitude();
            longitude = aset.getLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/asset.png");

        }
        if (param.equals("potensi")) {
            potensi = (ResPotency) d.getObjClass();
            lat = potensi.getLatitude();
            longitude = potensi.getLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/potency.png");

        }
        if (param.equals("composite")) {
            composite = (UtiBeaconComposite) d.getObjClass();
            lat = Double.valueOf(composite.getLatitude()).toString();
            longitude = Double.valueOf(composite.getLongitude()).toString();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/beacon_composite.png");

        }
        if (param.equals("elemental")) {
            elemental = (UtiBeaconElemental) d.getObjClass();
            lat = Double.valueOf(elemental.getLatitude()).toString();
            longitude = Double.valueOf(elemental.getLongitude()).toString();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/beacon_gps.png");

        }
        if (param.equals("pob")) {
            pob = (IncidentPOB) d.getObjClass();
            lat = pob.getLatitude();
            longitude = pob.getLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/pob.png");

        }
        if (param.equals("log")) {
            incLog = (IncidentLog) d.getObjClass();
            lat = incLog.getLatitude();
            longitude = incLog.getLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/log.png");

        }
        
        if (param.equals("sh")) {
            sighting = (UtiSighting) d.getObjClass();
            lat = sighting.getObserverLatitude();
            longitude = sighting.getObserverLongitude();
            iconPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/resources/basarnas/images/sarcoreapp/sighting.png");

        }
        centerGeoMap = lat + "," + longitude;
        map.setCenterGeoMap(centerGeoMap);
        map.setZoomGeoMap(7);
        LatLng coord = new LatLng(Double.valueOf(lat), Double.valueOf(longitude));
        mapModel.addOverlay(new Marker(coord, "Incident Number : " + incident.getIncidentnumber(), "", iconPath));
        map.setMapModel(mapModel);
        //RequestContext.getCurrentInstance().execute("PF('idnGmap').geocode('Jakarta')");
        // mapModel.addOverlay(new Marker(coord, "Incident Number : " + incident.getIncidentnumber()));
    }
}
