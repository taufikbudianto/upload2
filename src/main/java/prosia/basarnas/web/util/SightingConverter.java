/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.util;

/**
 *
 * @author PROSIA
 */
public class SightingConverter {

    public static String getStatusString(Integer status) {
        if (null == status) {
            return "";
        } else {
            switch (status) {
                case 1:
                    return "Belum Diproses";
                case 2:
                    return "Sedang Diproses";
                case 3:
                    return "Selesai Diproses";
                default:
                    return "";
            }
        }
    }

    public static String getStringEntryType(Boolean isCrm) {
        if (isCrm) {
            return "CRM";
        } else {
            return "Sar Core";
        }
    }

    public static String objectTypeToString(Integer objectType) {
        if (null == objectType) {
            return "";
        } else {
            switch (objectType) {
                case 1:
                    return "Kapal";
                case 2:
                    return "Orang";
                case 3:
                    return "Polusi";
                case 4:
                    return "Pesawat";
                case 5:
                    return "Sinyal";
                case 6:
                    return "Kendaraan";
                case 7:
                    return "Lain-lain";
                default:
                    return "";
            }
        }
    }
}
