/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

/**
 *
 * @author Aris
 */
public interface CommonConstant {
    public static final String NAN_STRING = "nan";
    public static final String EMPTY_STRING = "";
    public static final String ONE_SPACE_STRING = " ";
    public static final String DMS_DEGREE_SYMBOL = "°";
    public static final String DMS_MINUTE_SECOND_SYMBOL = "'";    
    public static final String DMS_PATTERN = "ddd°mm'ss-pp";
    public static final String DMS_DATABASE_PATTERN = "ddd-mm-ss-pp";
    public static final String NULL_STRING = "null";
    public static final String ZERO_STRING = "0";
    public static final String PREFIX_METHOD_SET = "set";
    public static final String PREFIX_METHOD_GET = "get";
    public static final String UPLOAD_FILE_NAME_KEY = "FILE_NAME";
    public static final String UPLOAD_FILE_CATEGORY_KEY = "FILE_CATEGORY";
    public static final String UPLOAD_FILE_INCIDENT_ID_KEY = "INCIDENT_ID";
    public static final String UPLOAD_FILE_KEY = "BIN";
    public static final String UPLOAD_FILE_SUCCESS = "SUCCESS";
    public static final String UPLOAD_FILE_FAILED = "FAILED";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_ZONE_PATTERN = "yyyyMMddHHmmx";
    public static final String DOWNLOAD_FILE_NAME_KEY = "FILE_NAME";
    public static final String DOWNLOAD_FILE_CATEGORY_KEY = "FILE_CATEGORY";
    public static final String DOWNLOAD_FILE_INCIDENT_ID_KEY = "INCIDENT_ID";
    public static final String DOWNLOAD_FILE_HEADER_CONTENT_TYPE_KEY = "Content-Type";
    public static final String DOWNLOAD_FILE_HEADER_CONTENT_LENGTH_KEY = "Content-Length";
    public static final String DOWNLOAD_FILE_HEADER_CONTENT_DISPOSITION_KEY = "Content-Disposition";
    public static final String DOWNLOAD_FILE_HEADER_CONTENT_DISPOSITION_FILENAME_KEY = "filename";
    public static final String DOWNLOAD_FILE_STATUS_KEY = "Download-Status";
    public static final String DOWNLOAD_FILE_SUCCESS = "SUCCESS";
    public static final String DOWNLOAD_FILE_FAILED = "FAILED";
    public static final String CONTENT_TYPE_PLAIN_TEXT = "text/plain";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final double ONE_NM_TO_DEG = 0.016666666666666666666666666666667;
    public static final int DEFAULT_DECIMAL_PRECISION = 4;
}
