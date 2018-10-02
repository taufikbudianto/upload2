/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.constant;

/**
 *
 * @author Aris
 */
public class StatusConstant {
    public static final String ADM_ROLE_ID = "NAS_08_001";
    
    // Personnel Status
    public static final int PERSONNEL_STATUS_READY = 0;
    public static final int PERSONNEL_STATUS_IN_FIELD = 1;
    public static final int PERSONNEL_STATUS_RESIGN = 2;
    // Status Asset Available
    public static final int ASSET_STATUS_AVAILABLE = 0;
    public static final int ASSET_STATUS_UNAVAILABLE = 1;
//    // Potency Level
//    public static final int POTENCY_LEVEL_NATIONAL = 0;
//    public static final int POTENCY_LEVEL_OFFICE_SAR = 1;
//    public static final int POTENCY_LEVEL_POST_SAR = 2;
    // Potency Type
    public static final int POTENCY_TYPE_LAND = 0;
    public static final int POTENCY_TYPE_AIR = 1;
    public static final int POTENCY_TYPE_SEA = 2;
    // Personnel Type
    public static final int PERSONNEL_TYPE_PERSONNEL = 0;
    public static final int PERSONNEL_TYPE_VOLUNTEER = 1;
    // Gender
    public static final String GENDER_MALE = "M";
    public static final String GENDER_FEMALE = "F";
    // Employment Type
    public static final int EMPLOYMENT_TYPE_PNS = 0;
    public static final int EMPLOYMENT_TYPE_CPNS = 1;
    // Marital Status
    public static final int MARITAL_STATUS_MARRIED = 0;
    public static final int MARITAL_STATUS_SINGLE = 1;
    // Position Level
    public static final int POSITION_CATEGORY_STRUCTURAL = 0;
    public static final int POSITION_CATEGORY_FUNCTIONAL = 1;
    public static final int POSITION_CATEGORY_OPERATIONAL = 2;
    public static final int POSITION_CATEGORY_READY = 3;
    // Rescuer
    public static final String RESCUER_MERCHANT = "Merchant";
    public static final String RESCUER_HELICOPTER = "Helicopter";
    public static final String RESCUER_FIXED_WING = "Fixed Wing";
    public static final String RESCUER_VESSEL = "Vessel";
    // Rescuer Air Type (Worksheet 2)    
    public static final String RESCUER_AIR_IN_QUERY = "'" + StatusConstant.RESCUER_FIXED_WING + "', '" + StatusConstant.RESCUER_HELICOPTER + "'";
    // Rescuer Sea Type (Worksheet 8)
    public static final String RESCUER_SEA_IN_QUERY = "'" + StatusConstant.RESCUER_VESSEL + "', '" + StatusConstant.RESCUER_MERCHANT + "'";
    // Search Object
    public static final String SEARCH_OBJECT_PERSON_IN_WATER = "Person in Water";
    public static final String SEARCH_OBJECT_PERSON_IN_WATER_WITH_FLOAT_EQUIP = "Person in Water (with float equip)";
    public static final String SEARCH_OBJECT_BOAT_LESS_THEN_5M = "Boat <5m (17ft)";
    public static final String SEARCH_OBJECT_BOAT_LESS_THEN_7M = "Boat <7m (23ft)";
    public static final String SEARCH_OBJECT_RAFT_1_PERSON = "Raft 1 Person";
    public static final String SEARCH_OBJECT_RAFT_10_PERSON = "Raft 10 Person";
    public static final String SEARCH_OBJECT_RAFT_15_PERSON = "Raft 15 Person";
    public static final String SEARCH_OBJECT_RAFT_20_PERSON = "Raft 20 Person";
    public static final String SEARCH_OBJECT_RAFT_25_PERSON = "Raft 25 Person";
    public static final String SEARCH_OBJECT_RAFT_4_PERSON = "Raft 4 Person";
    public static final String SEARCH_OBJECT_RAFT_6_PERSON = "Raft 6 Person";
    public static final String SEARCH_OBJECT_RAFT_8_PERSON = "Raft 8 Person";
    // Search Object Over Land
    public static final String SEARCH_OBJECT_OVER_LAND_PERSON = "Person";
    public static final String SEARCH_OBJECT_OVER_LAND_VEHICLE = "Vehicle";
    public static final String SEARCH_OBJECT_OVER_LAND_AIRCRAFT_LESS_THEN_5700 = "Aircraft < 5700kg";
    public static final String SEARCH_OBJECT_OVER_LAND_AIRCRAFT_OVER_5700 = "Aircraft > 5700kg";
    // Search Object Over Land Vegetation
    public static final String VEGETATION_LESS_THEN_15_PCTG = "<15%";
    public static final String VEGETATION_15_60_PCTG = "15-60%";
    public static final String VEGETATION_60_85_PCTG = "60-85%";
    public static final String VEGETATION_OVER_85_PCTG = ">85%";
    // Search Repeat
    public static final String SEARCH_REPEAT_FIRST = "First";
    public static final String SEARCH_REPEAT_SECOND = "Second";
    public static final String SEARCH_REPEAT_THIRD = "Third";
    public static final String SEARCH_REPEAT_FOURTH = "Fourth";
    public static final String SEARCH_REPEAT_FIFTH = "Fifth";
    // SMS/Email Message Type
    public static final int SMS_EMAIL_MESG_TYPE_UTILITIES = 0;
    public static final int SMS_EMAIL_MESG_TYPE_INCIDENT_PERSONNEL = 1;
    public static final int SMS_EMAIL_MESG_TYPE_INCIDENT_SRU = 2;
    public static final int SMS_EMAIL_MESG_TYPE_INCIDENT_EQUIPMENTS = 3;
    public static final int SMS_EMAIL_MESG_TYPE_INCIDENT_POTENCY = 4;
    public static final int SMS_EMAIL_MESG_TYPE_RADIOGRAM = 5;
    // Asset Type Category
    public static final int ASSET_TYPE_CATEGORY_MEDIUM = 0; // Sarana
    public static final int ASSET_TYPE_CATEGORY_INFRASTRUCTURE = 1; // Prasarana
    public static final int ASSET_TYPE_CATEGORY_EQUIPMENTS = 2; // Peralatan
    public static final int ASSET_TYPE_CATEGORY_COMPLETENESS = 3; // Kelengkapan
    // SRU / Not SRU in query
    public static final String ASSET_TYPE_IS_SRU_IN_QUERY = String.valueOf(ASSET_TYPE_CATEGORY_MEDIUM);
    public static final String ASSET_TYPE_IS_NOT_SRU_IN_QUERY = ASSET_TYPE_CATEGORY_INFRASTRUCTURE + ", " + ASSET_TYPE_CATEGORY_EQUIPMENTS + ", " + ASSET_TYPE_CATEGORY_COMPLETENESS;
    // Country ID
    public static final String COUNTRY_ID_INDONESIA = "ID";
    // Asset Type Category
    public static final int SKILL_DASAR = 0;
    public static final int SKILL_LANJUTAN = 1;
    public static final int SKILL_SPESIALIS = 2;
    public static final int SKILL_MANAJERIAL = 3;
    
    public static final String LAYER_KML = "K";
    public static final String LAYER_ESRI = "E";

    //Map Mode
    public static final String MAP_ONLINE_MODE = "Online Mode (Koneksi Internet)";
    public static final String MAP_OFFLINE_FUSION_MODE = "Offline Mode (Koneksi Basarnas Pusat)";
    public static final String MAP_OFFLINE_PORTABLE_MODE = "Offline Mode (Koneksi Portable)";
    
    // Person Title
    public static final String PERSON_TITLE_BPK = "Bpk.";
    public static final String PERSON_TITLE_IBU = "Ibu.";
    public static final String PERSON_TITLE_DR = "Dr.";
    public static final String PERSON_TITLE_DRS = "Drs.";
    public static final String PERSON_TITLE_PROF = "Prof.";
    
    // Asset Type Matra
    public static final int ASSET_TYPE_MATRA_LAND = 0;
    public static final int ASSET_TYPE_MATRA_AIR = 1;
    public static final int ASSET_TYPE_MATRA_SEA = 2;    
    
    //TaskSearchArea
    public static final int TASK_SEARCH_AREA_HALF_RADIUS = 0;
    public static final int TASK_SEARCH_AREA_ONE_RADIUS = 1;
    
    public static final String SEARCH_AREA_SQUARE_SHAPE = "Square";
    public static final String SEARCH_AREA_CIRCLE_SHAPE = "Circle";
    public static final String SEARCH_AREA_MONTECARLO = "MONTECARLO";
}
