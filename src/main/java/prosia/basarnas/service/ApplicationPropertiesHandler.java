package prosia.basarnas.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationPropertiesHandler {

    public static final String DATE_HOUR_DIFFERENCE_KEY = "date.hour.difference";
    public static final String SMS_EMAIL_GW_EXPIRED_HOUR_KEY = "sms.email.gateway.expired.hour";
    public static final String CURRENT_SITE_KEY = "current.site";
    public static final String REPORT_GENERATE_FOLDER = "report.generate.folder";
    public static final String UPLOAD_FOLDER_DOC_ROOT_KEY = "upload.folder.doc.root";
    public static final String UPLOAD_FOLDER_DOC_GENERAL_KEY = "upload.folder.doc.general";
    public static final String UPLOAD_FOLDER_DOC_PERSONNEL_PHOTO_KEY = "upload.folder.doc.personnel.photo";
    public static final String UPLOAD_FOLDER_DOC_VOLUNTEER_PHOTO_KEY = "upload.folder.doc.volunteer.photo";
    public static final String UPLOAD_FOLDER_DOC_USER_PHOTO_KEY = "upload.folder.doc.user.photo";
    public static final String UPLOAD_FOLDER_DOC_LOG_DATA_KEY = "upload.folder.doc.log.data";
    public static final String UPLOAD_FOLDER_DOC_INCIDENT_LOG_DATA_KEY = "upload.folder.doc.incident.log.data";
    public static final String UPLOAD_FOLDER_DOC_LOG_RADIOGRAM_KEY = "upload.folder.doc.log.radiogram";
    //Real Path
    public static final String UPLOAD_FOLDER_POTENCY_SYMBOL_KEY = "upload.folder.potency.symbol";
    public static final String UPLOAD_FOLDER_ASSET_TYPE_SYMBOL_KEY = "upload.folder.assettype.symbol";
    public static final String WEB_FOLDER_PLACEMARK_SYMBOL_KEY = "web.folder.placemark.symbol";
    //URL Path
    public static final String UPLOAD_FOLDER_PLACEMARK_SYMBOL_KEY = "upload.folder.placemark.symbol";

    public static final String UPLOAD_FOLDER_POTENCY_ABSOLUTE_SYMBOL_KEY = "upload.folder.absolute.potency.symbol";
    public static final String UPLOAD_FOLDER_ASSET_TYPE_ABSOLUTE_SYMBOL_KEY = "upload.folder.absolute.assettype.symbol";
    public static final String UPLOAD_FOLDER_EMAIL_ATTACHMENT_KEY = "upload.folder.email.attachment";

    public static final String MAP_ONLINE_KEY = "map.online.url";
    public static final String MAP_FUSION_OFFLINE_KEY = "map.offline.url";
    public static final String MAP_PORTABLE_OFFLINE_KEY = "map.portable.url";
    public static final String EARTH_ONLINE_KEY = "earth.online.url";
    public static final String EARTH_FUSION_OFFLINE_KEY = "earth.offline.url";
    public static final String EARTH_PORTABLE_OFFLINE_KEY = "earth.portable.url";

    public static final String GOOGLE_MAP_FUSION_SERVER_DEFS_KEY = "google.map.fusion.server.def.url";
    public static final String GOOGLE_EARTH_FUSION_SERVER_DEFS_KEY = "google.earth.fusion.server.def.url";
    public static final String GOOGLE_MAP_PORTABLE_SERVER_DEFS_KEY = "google.map.portable.server.def.url";
    public static final String GOOGLE_EARTH_PORTABLE_SERVER_DEFS_KEY = "google.earth.portable.server.def.url";

    public static final String UTI_WEATHER_BMKG_GET_NEAREST_LIMIT = "uti.weather.bmkg.getnearest.limit";
    public static final String INCIDENT_GET_NEAREST_KANSAR_AMOUNT = "incident.getnearest.kansar.amount";

    //Fecth BMKG IMAGE SCHEDULER
    public static final String FECTH_IMAGE_BMKG_SCHEDULER_MORNING_HOUR = "fecth.image.bmkg.scheduler.morning_hour";
    public static final String FECTH_IMAGE_BMKG_SCHEDULER_NOON_HOUR = "fecth.image.bmkg.scheduler.noon_hour";
    public static final String FECTH_IMAGE_BMKG_SCHEDULER_PROCESSING_TIME = "fecth.image.bmkg.scheduler.processing_time";
    public static final String DEFAULT_SEARCH_PATTERN_TRACK_SPACING = "default.searchpattern.trackspacing";

    private static Properties prop;
    private static final Logger logger = Logger.getLogger(
            ApplicationPropertiesHandler.class);

    static {
        InputStream is = null;

        try {
            logger.info("Loading file application.properties...");

//            is = ApplicationPropertiesHandler.class.getClassLoader().
//                    getResourceAsStream("prosia/config/application.properties");
            is = ApplicationPropertiesHandler.class.getClassLoader().
                    getResourceAsStream("properties.properties");
//            is = new FileInputStream("/cfg/application.properties");
            prop = new Properties();
            prop.load(is);

            logger.info("Successfully loading file application.properties...");

            logger.debug("Current Site = " + getProperty(CURRENT_SITE_KEY));
        } catch (IOException e) {
            logger.error("Unable to read file application.properties...", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ieo) {
                // do nothing
            }
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
