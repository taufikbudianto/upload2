/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.constant;

import javax.swing.ImageIcon;

public class UIConstant {
    
    public static final String UI_APPS_TITLE = "SARCORE";
    public static final String UI_DATE_FORMAT = "dd/MM/yyyy";
    public static final String UI_TIME_FORMAT = "HH:mm";
    public static final String UI_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String UI_DATE_TIME_ZONE_FORMAT = "yyyyMMddHHmmx";
    public static final String ROW_DIRECTION_UP = "-";
    public static final String ROW_DIRECTION_DOWN = "+";
    public static final String ROW_DIRECTION_LAST = "LAST";
    public static final String ROW_DIRECTION_FIRST = "FIRST";
    public static final int SCREEN_STATE_ADD = 0;
    public static final int SCREEN_STATE_EDIT = 1;
    public static final int SCREEN_STATE_SEARCH_SUCCESS = 2;
    public static final int SCREEN_STATE_ADD_SUCCESS = 3;
    public static final int SCREEN_STATE_EDIT_SUCCESS = 4;
    public static final int SCREEN_STATE_DELETE_SUCCESS = 5;
    public static final int SCREEN_STATE_INITIAL = 6;
    public static final int SCREEN_STATE_ADD_DETAIL = 7;
    public static final int SCREEN_STATE_EDIT_DETAIL = 8;
    public static final int SCREEN_STATE_VIEW = 9;
    public static final String IMAGE_URL_PACKAGE = "/prosia/common/image/";
    public static final String COMBO_BOX_LABEL_ALL = "Semua";
    public static final String COMBO_BOX_LABEL_CHOOSE_ONE = "Pilih Salah Satu";
    public static final ImageIcon BASARNAS_ICON = new ImageIcon(UIConstant.class.getResource(IMAGE_URL_PACKAGE + "basarnas.gif"));
    public static final ImageIcon MAP_ICON = new ImageIcon(UIConstant.class.getResource(IMAGE_URL_PACKAGE + "map.png"));
}
