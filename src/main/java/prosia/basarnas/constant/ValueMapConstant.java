/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.constant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aris
 */
public class ValueMapConstant {
    public static final Map<String, Integer> POSITION_CATEGORY = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> POTENCY_TYPE = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> PERSONNEL_TYPE = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> EMPLOYMENT_TYPE = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> PERSONNEL_STATUS = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> ASSET_STATUS = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> MARITAL_STATUS = new LinkedHashMap<String, Integer>();
    public static final Map<String, String> RESCUER = new LinkedHashMap<String, String>();
    public static final Map<String, String> RESCUER_AIR_TYPE = new LinkedHashMap<String, String>();
    public static final Map<String, String> RESCUER_SEA_TYPE = new LinkedHashMap<String, String>();
    public static final Map<String, Double[]> RESCUER_EYE_HEIGHT_ARR = new LinkedHashMap<String, Double[]>();
    public static final Map<String, List<LabelValue>> RESCUER_VISIBILITY_LIST = new LinkedHashMap<String, List<LabelValue>>();
    public static final Map<String, List<LabelValue>> RESCUER_TAS_LIST = new LinkedHashMap<String, List<LabelValue>>();
    public static final List<String> SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1 = new ArrayList<String>();
    public static final List<String> SEARCH_REPEAT = new ArrayList<String>();
    public static final List<String> SEARCH_OBJ_OVER_LAND = new ArrayList<String>();
    public static final List<LabelValue> SEARCH_OBJ_OVER_LAND_VISIBILITY = new ArrayList<LabelValue>();
    public static final List<LabelValue> SEARCH_OBJ_OVER_LAND_VEGETATION = new ArrayList<LabelValue>();
    public static final Map<String, Double[]> SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR = new LinkedHashMap<String, Double[]>();
    public static final Double[] SAFETY_FACTOR_ARR = new Double[]{1.1, 1.6, 2.0, 2.3, 2.6};
    public static final Double[] DISTRESS_CRAFT_ERROR_ARR = new Double[]{0.1, 0.5, 1.0, 2.0, 4.0, 5.0, 10.0, 15.0};
    public static final Double[] SEARCH_CRAFT_ERROR_ARR = new Double[]{0.1, 0.5, 1.0, 2.0, 4.0, 5.0, 10.0, 15.0};
    public static final Map<String, Integer> ASSET_TYPE_CATEGORY = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> SKILL_CATEGORY = new LinkedHashMap<String, Integer>();
    public static final Map<String, Integer> SMS_EMAIL_MESG_TYPE = new LinkedHashMap<String, Integer>();
    public static final List<String> PERSON_TITLE = new ArrayList<String>();
    public static final Map<String, Integer> ASSET_TYPE_MATRA = new LinkedHashMap<String, Integer>();

    static {
//        // Potency Level
//        POTENCY_LEVEL.put("Nasional", StatusConstant.POTENCY_LEVEL_NATIONAL);
//        POTENCY_LEVEL.put("Kantor SAR", StatusConstant.POTENCY_LEVEL_OFFICE_SAR);
//        POTENCY_LEVEL.put("Pos SAR", StatusConstant.POTENCY_LEVEL_POST_SAR);

        // Position Category
        POSITION_CATEGORY.put("Struktural", StatusConstant.POSITION_CATEGORY_STRUCTURAL);
        POSITION_CATEGORY.put("Fungsional", StatusConstant.POSITION_CATEGORY_FUNCTIONAL);
        POSITION_CATEGORY.put("Operasional", StatusConstant.POSITION_CATEGORY_OPERATIONAL);
        POSITION_CATEGORY.put("Siaga", StatusConstant.POSITION_CATEGORY_READY);
        
        // Potency Type
        POTENCY_TYPE.put("Darat", StatusConstant.POTENCY_TYPE_LAND);
        POTENCY_TYPE.put("Udara", StatusConstant.POTENCY_TYPE_AIR);
        POTENCY_TYPE.put("Laut", StatusConstant.POTENCY_TYPE_SEA);

        // Personnel Type
        PERSONNEL_TYPE.put("Personil", StatusConstant.PERSONNEL_TYPE_PERSONNEL);
        PERSONNEL_TYPE.put("Potency", StatusConstant.PERSONNEL_TYPE_VOLUNTEER);

        // Employment Type
        EMPLOYMENT_TYPE.put("PNS", StatusConstant.EMPLOYMENT_TYPE_PNS);
        EMPLOYMENT_TYPE.put("CPNS", StatusConstant.EMPLOYMENT_TYPE_CPNS);

        // Personnel Status
        PERSONNEL_STATUS.put("Ready", StatusConstant.PERSONNEL_STATUS_READY);
        PERSONNEL_STATUS.put("In-Field",
                StatusConstant.PERSONNEL_STATUS_IN_FIELD);
        PERSONNEL_STATUS.put("Resign", StatusConstant.PERSONNEL_STATUS_RESIGN);

        // Asset Status
        ASSET_STATUS.put("Tidak Beroperasi", StatusConstant.ASSET_STATUS_AVAILABLE);
        ASSET_STATUS.put("Beroperasi",
                StatusConstant.ASSET_STATUS_UNAVAILABLE);

        // Marital Status
        MARITAL_STATUS.put("Menikah", StatusConstant.MARITAL_STATUS_MARRIED);
        MARITAL_STATUS.put("Belum Menikah", StatusConstant.MARITAL_STATUS_SINGLE);

        String rescuerVesselLabel = "Vessels and small boats";
        String rescuerMerchantLabel = "Merchant Ships";
        String rescuerFixedWingLabel = "Fixed Wing Aircraft";
        String rescuerHelicopterLabel = "Helicopter";
        // Rescuer
        RESCUER.put(rescuerVesselLabel, StatusConstant.RESCUER_VESSEL);
        RESCUER.put(rescuerMerchantLabel, StatusConstant.RESCUER_MERCHANT);
        RESCUER.put(rescuerFixedWingLabel, StatusConstant.RESCUER_FIXED_WING);
        RESCUER.put(rescuerHelicopterLabel, StatusConstant.RESCUER_HELICOPTER);

        // Rescuer Air Type (Worksheet 2)
        RESCUER_AIR_TYPE.put(rescuerFixedWingLabel,
                StatusConstant.RESCUER_FIXED_WING);
        RESCUER_AIR_TYPE.put(rescuerHelicopterLabel,
                StatusConstant.RESCUER_HELICOPTER);

        // Rescuer Sea Type (Worksheet 8)
        RESCUER_SEA_TYPE.put(rescuerVesselLabel,
                StatusConstant.RESCUER_VESSEL);
        RESCUER_SEA_TYPE.put(rescuerMerchantLabel,
                StatusConstant.RESCUER_MERCHANT);

        // Rescuer Eye Height
        RESCUER_EYE_HEIGHT_ARR.put(StatusConstant.RESCUER_VESSEL,
                new Double[]{8.0, 14.0});
        RESCUER_EYE_HEIGHT_ARR.put(StatusConstant.RESCUER_MERCHANT,
                new Double[]{0.0});
        RESCUER_EYE_HEIGHT_ARR.put(StatusConstant.RESCUER_FIXED_WING,
                new Double[]{500.0, 1000.0, 1500.0, 2000.0});
        RESCUER_EYE_HEIGHT_ARR.put(StatusConstant.RESCUER_HELICOPTER,
                new Double[]{500.0, 1000.0, 1500.0, 2000.0});

        // Rescuer Visibility
        List labelValueList = new ArrayList<LabelValue>();
        labelValueList.add(new LabelValue<Double>("2.0", 2.0));
        labelValueList.add(new LabelValue<Double>("5.0", 5.0));
        labelValueList.add(new LabelValue<Double>("10.0", 10.0));
        labelValueList.add(new LabelValue<Double>("15.0", 15.0));
        labelValueList.add(new LabelValue<Double>("20.0", 20.0));
        labelValueList.add(new LabelValue<Double>("25.0", 25.0));
        RESCUER_VISIBILITY_LIST.put(StatusConstant.RESCUER_VESSEL,
                labelValueList);

        labelValueList = new ArrayList<LabelValue>();
        labelValueList.add(new LabelValue<Double>("5.0", 5.0));
        labelValueList.add(new LabelValue<Double>("10.0", 10.0));
        labelValueList.add(new LabelValue<Double>("20.0", 20.0));
        labelValueList.add(new LabelValue<Double>("30.0", 30.0));
        labelValueList.add(new LabelValue<Double>("40.0", 40.0));
        RESCUER_VISIBILITY_LIST.put(StatusConstant.RESCUER_MERCHANT,
                labelValueList);

        labelValueList = new ArrayList<LabelValue>();
        labelValueList.add(new LabelValue<Double>("2.0", 2.0));
        labelValueList.add(new LabelValue<Double>("5.0", 5.0));
        labelValueList.add(new LabelValue<Double>("10.0", 10.0));
        labelValueList.add(new LabelValue<Double>("20.0", 20.0));
        labelValueList.add(new LabelValue<Double>("30.0", 30.0));
        labelValueList.add(new LabelValue<Double>(">40.0", 40.0));
        RESCUER_VISIBILITY_LIST.put(StatusConstant.RESCUER_FIXED_WING,
                labelValueList);

        labelValueList = new ArrayList<LabelValue>();
        labelValueList.add(new LabelValue<Double>("2.0", 2.0));
        labelValueList.add(new LabelValue<Double>("5.0", 5.0));
        labelValueList.add(new LabelValue<Double>("10.0", 10.0));
        labelValueList.add(new LabelValue<Double>("20.0", 20.0));
        labelValueList.add(new LabelValue<Double>("30.0", 30.0));
        labelValueList.add(new LabelValue<Double>(">40.0", 40.0));
        RESCUER_VISIBILITY_LIST.put(StatusConstant.RESCUER_HELICOPTER,
                labelValueList);

        // Rescuer TAS
        labelValueList = new ArrayList<LabelValue>();
        labelValueList.add(new LabelValue<Double>("<150.0", 150.0));
        labelValueList.add(new LabelValue<Double>("180.0", 180.0));
        labelValueList.add(new LabelValue<Double>("210.0", 210.0));
        RESCUER_TAS_LIST.put(StatusConstant.RESCUER_FIXED_WING,
                labelValueList);

        labelValueList = new ArrayList<LabelValue>();
        labelValueList.add(new LabelValue<Double>("<60.0", 60.0));
        labelValueList.add(new LabelValue<Double>("90.0", 90.0));
        labelValueList.add(new LabelValue<Double>("120.0", 120.0));
        labelValueList.add(new LabelValue<Double>("140.0", 140.0));
        RESCUER_TAS_LIST.put(StatusConstant.RESCUER_HELICOPTER,
                labelValueList);

        // Search Object Weather Correction Factor Type 1        
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_PERSON_IN_WATER);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_PERSON_IN_WATER_WITH_FLOAT_EQUIP);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_BOAT_LESS_THEN_5M);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_BOAT_LESS_THEN_7M);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_1_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_10_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_15_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_20_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_25_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_4_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_6_PERSON);
        SEARCH_OBJ_WEAT_CORRECT_FAC_TYPE_1.add(
                StatusConstant.SEARCH_OBJECT_RAFT_8_PERSON);

        // Search Object Over Land (Worksheet 3)
        SEARCH_OBJ_OVER_LAND.add(StatusConstant.SEARCH_OBJECT_OVER_LAND_PERSON);
        SEARCH_OBJ_OVER_LAND.add(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_VEHICLE);
        SEARCH_OBJ_OVER_LAND.add(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_AIRCRAFT_LESS_THEN_5700);
        SEARCH_OBJ_OVER_LAND.add(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_AIRCRAFT_OVER_5700);

        // Search Object Over Land Eye Height
        SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR.put(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_PERSON,
                new Double[]{500.0, 1000.0});
        SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR.put(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_VEHICLE,
                new Double[]{500.0, 1000.0, 1500.0, 2000.0});
        SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR.put(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_AIRCRAFT_LESS_THEN_5700,
                new Double[]{500.0, 1000.0, 1500.0, 2000.0});
        SEARCH_OBJ_OVER_LAND_EYE_HEIGHT_ARR.put(
                StatusConstant.SEARCH_OBJECT_OVER_LAND_AIRCRAFT_OVER_5700,
                new Double[]{500.0, 1000.0, 1500.0, 2000.0});

        // Search Object Over Land Visibility
        SEARCH_OBJ_OVER_LAND_VISIBILITY.add(new LabelValue<Double>("5.0", 5.0));
        SEARCH_OBJ_OVER_LAND_VISIBILITY.add(new LabelValue<Double>("10.0", 10.0));
        SEARCH_OBJ_OVER_LAND_VISIBILITY.add(new LabelValue<Double>("20.0", 20.0));
        SEARCH_OBJ_OVER_LAND_VISIBILITY.add(new LabelValue<Double>("30.0", 30.0));
        SEARCH_OBJ_OVER_LAND_VISIBILITY.add(new LabelValue<Double>("40.0", 40.0));

        // Search Object Over Land Vegetation
        SEARCH_OBJ_OVER_LAND_VEGETATION.add(new LabelValue<String>(
                StatusConstant.VEGETATION_LESS_THEN_15_PCTG,
                StatusConstant.VEGETATION_LESS_THEN_15_PCTG));
        SEARCH_OBJ_OVER_LAND_VEGETATION.add(new LabelValue<String>(
                StatusConstant.VEGETATION_15_60_PCTG,
                StatusConstant.VEGETATION_15_60_PCTG));
        SEARCH_OBJ_OVER_LAND_VEGETATION.add(new LabelValue<String>(
                StatusConstant.VEGETATION_60_85_PCTG,
                StatusConstant.VEGETATION_60_85_PCTG));
        SEARCH_OBJ_OVER_LAND_VEGETATION.add(new LabelValue<String>(
                StatusConstant.VEGETATION_OVER_85_PCTG,
                StatusConstant.VEGETATION_OVER_85_PCTG));

        // Search Repeat
        SEARCH_REPEAT.add(StatusConstant.SEARCH_REPEAT_FIRST);
        SEARCH_REPEAT.add(StatusConstant.SEARCH_REPEAT_SECOND);
        SEARCH_REPEAT.add(StatusConstant.SEARCH_REPEAT_THIRD);
        SEARCH_REPEAT.add(StatusConstant.SEARCH_REPEAT_FOURTH);
        SEARCH_REPEAT.add(StatusConstant.SEARCH_REPEAT_FIFTH);

        // Asset Type Category        
        ASSET_TYPE_CATEGORY.put("Sarana",
                StatusConstant.ASSET_TYPE_CATEGORY_MEDIUM);
        ASSET_TYPE_CATEGORY.put("Prasarana",
                StatusConstant.ASSET_TYPE_CATEGORY_INFRASTRUCTURE);
        ASSET_TYPE_CATEGORY.put("Peralatan",
                StatusConstant.ASSET_TYPE_CATEGORY_EQUIPMENTS);
        ASSET_TYPE_CATEGORY.put("Kelengkapan",
                StatusConstant.ASSET_TYPE_CATEGORY_COMPLETENESS);

        // Skill type category
        SKILL_CATEGORY.put("Kualifikasi Dasar", StatusConstant.SKILL_DASAR);
        SKILL_CATEGORY.put("Kualifikasi Lanjutan", StatusConstant.SKILL_LANJUTAN);
        SKILL_CATEGORY.put("Kualifikasi Spesialis",
                StatusConstant.SKILL_SPESIALIS);
        SKILL_CATEGORY.put("Kualifikasi Manajerial",
                StatusConstant.SKILL_MANAJERIAL);
                
        // SMS Email Message Type
        SMS_EMAIL_MESG_TYPE.put("Utilities",
                StatusConstant.SMS_EMAIL_MESG_TYPE_UTILITIES);
        SMS_EMAIL_MESG_TYPE.put("Insiden Personil",
                StatusConstant.SMS_EMAIL_MESG_TYPE_INCIDENT_PERSONNEL);
        SMS_EMAIL_MESG_TYPE.put("Insiden SRU",
                StatusConstant.SMS_EMAIL_MESG_TYPE_INCIDENT_SRU);
        SMS_EMAIL_MESG_TYPE.put("Insiden Peralatan",
                StatusConstant.SMS_EMAIL_MESG_TYPE_INCIDENT_EQUIPMENTS);
        SMS_EMAIL_MESG_TYPE.put("Insiden Potensi",
                StatusConstant.SMS_EMAIL_MESG_TYPE_INCIDENT_POTENCY);
        SMS_EMAIL_MESG_TYPE.put("Radiogram",
                StatusConstant.SMS_EMAIL_MESG_TYPE_RADIOGRAM);
        
        // Search Repeat
        PERSON_TITLE.add(StatusConstant.PERSON_TITLE_BPK);
        PERSON_TITLE.add(StatusConstant.PERSON_TITLE_IBU);
        PERSON_TITLE.add(StatusConstant.PERSON_TITLE_DR);
        PERSON_TITLE.add(StatusConstant.PERSON_TITLE_DRS);
        PERSON_TITLE.add(StatusConstant.PERSON_TITLE_PROF);
        
        // Asset Type Matra
        ASSET_TYPE_MATRA.put("Darat", StatusConstant.ASSET_TYPE_MATRA_LAND);
        ASSET_TYPE_MATRA.put("Udara", StatusConstant.ASSET_TYPE_MATRA_AIR);
        ASSET_TYPE_MATRA.put("Laut", StatusConstant.ASSET_TYPE_MATRA_SEA);        
    }

    public static String getKeyFromValue(Map<String, Integer> map, Integer value) {
        Iterator it = map.keySet().iterator();
        String key = null;
        String result = null;
        while (it.hasNext()) {
            key = (String) it.next();
            if (map.get(key).equals(value)) {
                result = key;
                break;
            }
        }

        return result;
    }
    
    public static String getKeyFromValue(Map<String, String> map, String value) {
        Iterator it = map.keySet().iterator();
        String key = null;
        String result = null;
        while (it.hasNext()) {
            key = (String) it.next();
            if (map.get(key).equals(value)) {
                result = key;
                break;
            }
        }

        return result;
    }
}
