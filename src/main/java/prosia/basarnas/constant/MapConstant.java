/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.constant;

import prosia.basarnas.web.controller.map.googleapi.GLatLng;

/**
 *
 * @author Aris
 */
public class MapConstant {
    public static final String SEARCH_AREA_MODE = "'SEARCH_AREA'";
    public static final String REMOVE_CURRENT_SEARCH_AREA = "'removeCurrentSearchArea()'";
    public static final String FREE_DEFINE_SEARCH_PATTERN_MODE = "'FREE_DEFINE_PATTERN'";
    public static final String MEASURE_MODE = "'MEASURE'";
    public static final String PLACEMARK_MODE = "'PLACEMARK'";
    public static final String CHECKING_SEARCH_AREA = "checkCurrentSearchArea()";
    public static final String LATITUDE_MAP_CLICKED = "LAT";
    public static final String LONGITUDE_MAP_CLICKED = "LNG";
    public static final String RESET_MAP_OVERLAY = "resetMapOverlay()";
    public static final String REINIT = "reinit()";
    public static final String CUSTOM_SEARCH_AREA = "'CUSTOM_SEARCH_AREA'";    
    
    
    //Measure
    public static final String REMOVE_MEASURE = "removeMeasure()";
    
    
    //State Auto Generate Search Pattern Value, Criteria must valid with DriftCalculation
    public static final String AUTO_GENERATE_STATE = "AUTO_GENERATE_STATE";
    public static final String AUTO_GENERATE_TRUE_STATE = "1";
    public static final String AUTO_GENERATE_FALSE_STATE = "0";
    
    //STRING MAP TYPE
    private static final String G_SATELLITE_MAP ="G_SATELLITE_MAP"; 
    private static final String G_HYBRID_MAP ="G_HYBRID_MAP"; 
    private static final String G_PHYSICAL_MAP ="G_PHYSICAL_MAP"; 
    private static final String G_SATELLITE_3D_MAP ="G_SATELLITE_3D_MAP"; 
    private static final String G_NORMAL_MAP = "G_NORMAL_MAP";
    
    
    //DRIFTCALCULATION CONDITION
    public static final String DRIFT_CALCULATION_ACTIVE = "1";
    public static final String DRIFT_CALCULATION_IS_NULL = "0";
    public static final String CHECK_DRIFT_CALCULATION = "checkingCurrentDriftCalculation()";
    
    /*
    //LAYER MAP TYPE 
    private static final LayerSupport LAYER_SUPPORT_ONLINE_MAP = new LayerSupport(true, true, false); 
    
    //MAP TYPE
    public static final MapType NORMAL_MAP_TYPE = new MapType("Map", G_NORMAL_MAP, false, false, NavigatorIconCollection.MAP_NORMAL_ICON_24, LAYER_SUPPORT_ONLINE_MAP);
    public static final MapType SATELLITE_MAP_WITHOUT_LABEL_TYPE = new MapType("Satellite without label", G_SATELLITE_MAP, false, false, NavigatorIconCollection.SATELLITE_ICON_24, LAYER_SUPPORT_ONLINE_MAP);
    public static final MapType SATELLITE_MAP_WITH_LABEL_TYPE = new MapType("Satellite", G_HYBRID_MAP, false, false, NavigatorIconCollection.SATELLITE_ICON_24, LAYER_SUPPORT_ONLINE_MAP);
    public static final MapType TERRAIN_MAP_TYPE = new MapType("Terrain", G_PHYSICAL_MAP, false, false, NavigatorIconCollection.TERRAIN_ICON_24, LAYER_SUPPORT_ONLINE_MAP);
    public static final MapType SATELLITE_3D_MAP_TYPE = new MapType("Earth", G_SATELLITE_3D_MAP, true, false, NavigatorIconCollection.EARTH_ICON_24, new LayerSupport(false, true, false));    
    public static final MapType FUSION_MAP = new MapType("Fusion Map", "FUSION_MAP", false, true, NavigatorIconCollection.SATELLITE_ICON_24, new LayerSupport(true, false, true));
    public static final MapType GEE = new MapType("GEE", "GEE", true, true, NavigatorIconCollection.EARTH_ICON_24, new LayerSupport(false, true, true));
    public static final MapType PORTABLE_MAP = new MapType("Portable Map", "FUSION_MAP", false, true, NavigatorIconCollection.SATELLITE_ICON_24, new LayerSupport(true, false, true));
    public static final MapType PORTABLE_GLOBE = new MapType("Portable Globe", "GEE", true, true, NavigatorIconCollection.EARTH_ICON_24, new LayerSupport(false, true, true));
    */
    
    
    
    
    public static final String EARTH_LAYER_BORDERS = "LAYER_BORDERS";
    public static final String EARTH_LAYER_BUILDINGS = "LAYER_BUILDINGS";
    public static final String EARTH_LAYER_BUILDINGS_LOW_RESOLUTION = "LAYER_BUILDINGS_LOW_RESOLUTION";
    public static final String EARTH_LAYER_ROADS = "LAYER_ROADS";
    public static final String EARTH_LAYER_TERRAIN = "LAYER_TERRAIN";
    public static final String EARTH_LAYER_TREES = "LAYER_TREES";
    
    //CHECKING NAVIGATION
    public static final String IS_FINISH_LOADING = "IS_FINISH_LOADING";
    
    //CLUSTER MARKER
    public static final String CLUSTER_MANAGER_REFRESH = "clusterManager.refresh()";
    public static final String CLUSTER_MANAGER_REFRESH_CAUSE_ONE_REMOVE = "clusterManager.refreshCauseRemoveOne()";
    
    //HASH VARIABLE
//    public static final String HASH_INCIDENT = "INCIDENT";
//    public static final String HASH_POB = "POB";
//    public static final String HASH_ASSET = "ASSET";
//    public static final String HASH_POTENCY = "POTENCY";
//    public static final String HASH_INCIDENT_ASSET = "INC_ASSET";
//    public static final String HASH_INCIDENT_POTENCY = "INC_POTENCY";
//    public static final String HASH_COMPOSITE = "COMPOSITE";
//    public static final String HASH_ELEMENTAL = "ELEMENTAL";
//    public static final String HASH_POSSAR = "POSSAR";
//    public static final String HASH_KANSAR = "KANSAR";
    
    //POSITION
    public static GLatLng INDONESIAN_POSITION = new GLatLng("-4.214943141390638", "123.046875");
    //DEFAULT ZOOM 
    public static int DEFAULT_MAP_ZOOM = 4;
    
    //PARAMTER URL
    public static final String MAP_TYPE_URL_PARAMTER = "maptype";
    /*
    public static String removesOverlayInSpesifiedHash(String hashVariable, List<SubTreeNode> nodes){
       String iDs = "[";
       for(SubTreeNode node : nodes){
           iDs += "'" + node.getObject().getID() +"',";
       }       
       iDs = iDs.substring(0, iDs.length() -1) + "]";
       return "removesOverlayInSpesifiedHash("+ hashVariable +""+ iDs +")";
    }
    */
    /**
     * @return String function <code>setMode()</code> yang ada pada JavaScript Map
     */
    public static String setMode(String mode){        
        if(mode == null){
            return "setMODE('')";
        }
        System.out.println("setMODE("+ mode +")");
        return "setMODE("+ mode +")";
    }
    
    /**
     * String function <code>setEarthLayer()</code> yang ada pada JavaScript Earth
     */
    public static String setEarthLayer(String layer, boolean kondisi){
        return "setEarthLayer("+ layer +","+ kondisi +")";
    }
    
    
    /**
     * menyatakan apakah valid atau tidak nilai yang di passing kedalam methode ini
     * cocok atau tidak untuk melakukan membuatan Search Pattern
     */ 
    public static boolean isNull(String lama, String baru){
        
        if(baru == null || lama == null || baru.equalsIgnoreCase(lama) || baru.equalsIgnoreCase("null")
                || baru.equalsIgnoreCase("nan") || baru.equalsIgnoreCase("undefined")
                || baru.equalsIgnoreCase("") ){
            return true;
        }
        return false;
    }
    
    
    public static boolean isNull(String value){
        
        if(value == null || value.equals(CommonConstant.EMPTY_STRING) 
                || value.equalsIgnoreCase(CommonConstant.NULL_STRING)
                || value.equalsIgnoreCase(CommonConstant.NAN_STRING)){
            return true;
        }
        return false;
    }
    
    
    public static String setCenter(GLatLng center, int zoomLevel){
        return "map.setCenter("+ center.toJavaScript() +","+ zoomLevel +")";
    }
    
    public static String setCenter(GLatLng center){
        return "map.setCenter(" + center.toJavaScript() +")";
    }

    public static String setMapType(String mapType){
        return "map.setMapType("+ mapType +")";
    }
    
    public static String removeOverlay(String overlayScript){
        return "map.removeOverlay("+ overlayScript +")";
    }
    
    public static String geocoderSearch(String address){
        return "getGeoCoderLatLng('"+ address +"')";
    }
    
    public static String driftCalculationScript(){
        return "DriftCalculation(lkp, datum, leftLeeway, rightLeeway, radius, angle)";
    }
    
    public static String getCenter(){
        return "map.getCenter()";
    }
    
    public static String getZoom(){
        return "map.getZoom()";
    }
    
    public static String setTitleDocument(String newTitle){
        return "changeTitle('"+ newTitle +"')";
    }
    
    
    public static String createConcurrent(String arrayJSON){
        return "createConcurrent("+ arrayJSON +")";
    }
}
