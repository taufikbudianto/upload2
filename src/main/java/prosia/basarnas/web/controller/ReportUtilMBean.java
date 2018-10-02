package prosia.basarnas.web.controller;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.web.servlets.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import prosia.basarnas.constant.UIConstant;
import prosia.basarnas.model.DriftCalcWorksheet1;
import prosia.basarnas.model.DriftCalcWorksheet2;
import prosia.basarnas.model.DriftCalcWorksheet3;
import prosia.basarnas.model.DriftCalcWorksheet8;
import prosia.basarnas.repo.DriftCalcWorksheet2Repo;
import prosia.basarnas.web.util.DecimalUtil;
import prosia.basarnas.service.limitJRViewerFormats;
import prosia.basarnas.web.util.LatitudeLongitude;
import prosia.basarnas.web.util.Tanggal;

public class ReportUtilMBean {

    //private static final String JASPER_WORKSHEET1 = "/prosia/ui/report/drift/worksheet1.jasper";
    private static final String JASPER_WORKSHEET1 = "/report/report_jasper/worksheet1.jrxml";
    private static final String JASPER_WORKSHEET2 = "/report/report_jasper/worksheet2.jasper";
    private static final String JASPER_WORKSHEET3 = "/report/report_jasper/worksheet3.jasper";
    private static final String JASPER_WORKSHEET8 = "/report/report_jasper/worksheet8.jasper";
    private static final String BASARNAS_LOGO = "/prosia/common/image/basarnas (copy).gif";
    private static ReportUtilMBean instance = null;

    static void generateWorksheet2Report(DriftCalcWorksheet2 dwc1, DriftCalcWorksheet2 dwc2, DriftCalcWorksheet2 dwc3, DriftCalcWorksheet2 dwc4, String fileOut) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private final double DEFAULT_KM = 0.53961;

    @Autowired
    private DriftCalcWorksheet2Repo driftCalcWorksheet2Repo;

    ReportUtilMBean() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        throw new CloneNotSupportedException();
    }

    public static ReportUtilMBean getInstance() {
        if (instance == null) {
            instance = new ReportUtilMBean();
        }

        return instance;
    }

    //<editor-fold defaultstate="collapsed" desc="generateWorksheet1Report">
    /**
     * Fungsi untuk menuliskan data Drift Calculation Worksheet 1 dalam bentuk
     * pdf
     *
     * @param dcw Object DriftCalcWorksheet1 yang akan dituliskan dalam pdf
     * @param fileOut String path file pdf akan disimpan
     */
    public HashMap<String, Object> generateWorksheet1Report(DriftCalcWorksheet1 dcw, String fileOut, String user) {
        String time = "";
        String dir = System.getProperty("user.dir").replace("\\", "\\\\");
        dir = dir + "\\\\src\\\\main\\\\webapp\\\\report\\\\report_jasper\\\\worksheet1.jasper";
        InputStream is = getClass().getResourceAsStream(dir);
//        File file = new File(dir);
        HashMap<String, Object> params = new HashMap<String, Object>();
        //@Time formatting
        SimpleDateTimeZoneFormatMBean sdf = new SimpleDateTimeZoneFormatMBean();
        sdf.applyPattern(UIConstant.UI_DATE_TIME_ZONE_FORMAT);
        if (dcw.getIncident().getStartdate() != null && dcw.getIncident().getStartdate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dcw.getIncident().getStartdate());
            cal.setTimeZone(TimeZone.getTimeZone(dcw.getIncident().getStartdatetimezone()));
            time = sdf.format(cal);
        }
        //Calculation
        Double distanceRightDe = dcw.getDriftRightDistance() * dcw.getDriftErrorPercentage() / 100;
        Double distanceLeftDe = dcw.getDriftLeftDistance() * dcw.getDriftErrorPercentage() / 100;
        Double leewayWindSpeedBeforeModifier = dcw.getIndexLeeway().getMultiplier() * dcw.getWindCurrentSpeed();
        Double searchRadiusBeforeRounded = dcw.getTotalProbableError() * dcw.getSafetyFactor();
        //Insert parameters
        params.put("SEARCH_TARGET", dcw.getIncidentDescription() + " ");
        params.put("LKP", LatitudeLongitude.latitideFormatted(dcw.getIncidentLatitude()) + " , " + LatitudeLongitude.longitudeFormatted(dcw.getIncidentLongitude()) + " ");
        params.put("TIME", time + " ");
        params.put("HOUR_OF_DRIFT", dcw.getDriftHours().toString() + " ");
        params.put("SEA_CURRENT_VECTOR", dcw.getSeaCurrentAngle().toString() + " ");
        params.put("SEA_CURRENT_KNOTS", dcw.getSeaCurrentSpeed().toString() + " ");
        params.put("SEA_CURRENT_DISTANCE", dcw.getSeaCurrentDistance().toString() + " ");
        Double currentAngle = dcw.getWindCurrentAngle();
        Double reciprocalCurrentAngle = dcw.getWindCurrentAngle();
        if (reciprocalCurrentAngle >= 180) {
            reciprocalCurrentAngle = reciprocalCurrentAngle - 180;
        } else {
            reciprocalCurrentAngle = reciprocalCurrentAngle + 180;
        }
        params.put("WIND_VECTOR", reciprocalCurrentAngle.toString() + " ");
        params.put("WIND_KNOTS", dcw.getWindCurrentSpeed().toString() + " ");
        params.put("RECIPROCAL_WIND_VECTOR", dcw.getWindCurrentAngle().toString() + " ");
        params.put("RECIPROCAL_WIND_KNOTS", dcw.getWindCurrentSpeed().toString() + " ");
        params.put("RECIPROCAL_WIND_VECTOR_30", dcw.getWindCurrentAngle().toString() + " ");
        params.put("RECIPROCAL_WIND_KNOTS_30", dcw.getWindCurrentSpeed().toString() + " ");
        params.put("CURRENT_WIND_VECTOR", (dcw.getWindCurrentAngle()).toString() + " ");
        params.put("CURRENT_WIND_KNOTS", dcw.getWindCurrentSpeedForCalcDist().toString() + " ");
        params.put("CURRENT_WIND_DISTANCE", dcw.getWindCurrentDistance().toString() + " ");
        params.put("LEEWAY_ANGLES", dcw.getLeewayDivergenceAngle().toString() + " ");
        params.put("LEEWAY_VECTOR_LEFT", dcw.getLeewayLeftAngle().toString() + " ");
        params.put("LEEWAY_VECTOR_RIGHT", dcw.getLeewayRightAngle().toString() + " ");
        params.put("LEEWAY_MULTIPLIER", dcw.getIndexLeeway().getMultiplier().toString() + " ");
        params.put("LEEWAY_SPEED_BEFORE_MODIFIER", leewayWindSpeedBeforeModifier.toString() + " ");
        params.put("LEEWAY_MODIFIER", dcw.getIndexLeeway().getModifier().toString() + " ");
        params.put("LEEWAY_SPEED", dcw.getLeewaySpeed().toString() + " ");
        if (dcw.getUnit() == 0) {
            params.put("LEEWAY_DISTANCE", dcw.getLeewayDistance().toString() + "  nm ");
        } else {
            params.put("LEEWAY_DISTANCE", DecimalUtil.rounding(dcw.getLeewayDistance() / DEFAULT_KM).toString() + "  km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DRIFT_DISTANCE_RIGHT", dcw.getDriftRightDistance().toString() + " nm ");
        } else {
            params.put("DRIFT_DISTANCE_RIGHT", DecimalUtil.rounding(dcw.getDriftRightDistance() / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DRIFT_DISTANCE_RIGHT_DE", distanceRightDe.toString() + " nm ");
        } else {
            params.put("DRIFT_DISTANCE_RIGHT_DE", DecimalUtil.rounding(distanceRightDe / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DRIFT_DISTANCE_LEFT", dcw.getDriftLeftDistance().toString() + " nm ");
        } else {
            params.put("DRIFT_DISTANCE_LEFT", DecimalUtil.rounding(dcw.getDriftLeftDistance() / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DRIFT_DISTANCE_LEFT_DE", distanceLeftDe.toString() + " nm ");
        } else {
            params.put("DRIFT_DISTANCE_LEFT_DE", DecimalUtil.rounding(distanceLeftDe / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DRIFT_DISTANCE_LEFT_RIGHT", dcw.getDriftLeftToRightDistance().toString() + " nm ");
        } else {
            params.put("DRIFT_DISTANCE_LEFT_RIGHT", DecimalUtil.rounding(dcw.getDriftLeftToRightDistance() / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DRIFT_DISTANCE_DE", dcw.getDriftError().toString() + " ");
        } else {
            params.put("DRIFT_DISTANCE_DE", DecimalUtil.rounding(dcw.getDriftError() / DEFAULT_KM).toString() + " ");
        }
        if (dcw.getUnit() == 0) {
            params.put("DISTRESS_CRAFT_ERROR", dcw.getDistressCraftError().toString() + " nm ");
        } else {
            params.put("DISTRESS_CRAFT_ERROR", DecimalUtil.rounding(dcw.getDistressCraftError() / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("SEARCH_CRAFT_ERROR", dcw.getSearchCraftError().toString() + " nm ");
        } else {
            params.put("SEARCH_CRAFT_ERROR", DecimalUtil.rounding(dcw.getSearchCraftError() / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("TOTAL_PROBABLE_ERROR", dcw.getTotalProbableError().toString() + " ");
        } else {
            params.put("TOTAL_PROBABLE_ERROR", DecimalUtil.rounding(dcw.getTotalProbableError() / DEFAULT_KM).toString() + " ");
        }

        params.put("SAFETY_FACTOR", dcw.getSafetyFactor().toString() + " ");
        if (dcw.getUnit() == 0) {
            params.put("SEARCH_RADIUS", DecimalUtil.rounding(searchRadiusBeforeRounded, 4).toString() + " nm ");
        } else {
            params.put("SEARCH_RADIUS", DecimalUtil.rounding(searchRadiusBeforeRounded / DEFAULT_KM, 4).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("SEARCH_RADIUS_ROUNDED", dcw.getSearchRadius().toString() + " nm ");
        } else {
            params.put("SEARCH_RADIUS_ROUNDED", DecimalUtil.rounding(dcw.getSearchRadius() / DEFAULT_KM).toString() + " km ");
        }
        if (dcw.getUnit() == 0) {
            params.put("SEARCH_AREA", dcw.getSearchArea().toString() + " nm² ");
        } else {
            params.put("SEARCH_AREA", DecimalUtil.rounding(dcw.getSearchArea() / DEFAULT_KM).toString() + " km² ");
        }
        params.put("user", user);
        params.put("DATUM", LatitudeLongitude.latitideFormatted(dcw.getDatumLatitude()) + " , " + LatitudeLongitude.latitideFormatted(dcw.getDatumLongitude()) + " ");
        //Generating pdf
        try {
            //           JasperReport jr = (JasperReport) JRLoader.loadObject(is);
            JasperReport jr = JasperCompileManager.compileReport(is);
//            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
//            JasperExportManager.exportReportToPdfFile(jp, fileOut);

            JRViewer jrv = new JRViewer(jp);
            limitJRViewerFormats limit = new limitJRViewerFormats();
            limit.limitJRViewerFormats(jrv);
            final JFrame jf = new JFrame();

            BufferedImage Image = null;
            try {
                Image = ImageIO.read(jf.getClass().getResource(BASARNAS_LOGO));
            } catch (Exception ex) {
            }
            jf.setIconImage(Image);
            jf.getContentPane().add(jrv);
            jf.validate();
            jf.setVisible(true);
            jf.setTitle("Laporan Worksheet 1");
            jf.setSize(new Dimension(850, 600));
            jf.setLocation(300, 100);
            jf.setAlwaysOnTop(true);
            jf.setResizable(false);

            jf.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    jf.setAlwaysOnTop(false);
                }
            });
        } catch (JRException ex) {
        }
        return params;
    }

    //<editor-fold defaultstate="collapsed" desc="generateWorksheet2Report">
    /**
     * Fungsi untuk menuliskan data Drift Calculation Worksheet 2 dalam bentuk
     * pdf
     *
     * @param dcw1 Object DriftCalcWorksheet2 yang akan dituliskan dalam pdf
     * pada kolom pertama
     * @param dcw2 Object DriftCalcWorksheet2 yang akan dituliskan dalam pdf
     * pada kolom kedua
     * @param dcw3 Object DriftCalcWorksheet2 yang akan dituliskan dalam pdf
     * pada kolom ketiga
     * @param dcw4 Object DriftCalcWorksheet2 yang akan dituliskan dalam pdf
     * pada kolom keempat
     * @param fileOut String path file pdf akan disimpan
     */
    public void generateWorksheet2Report(DriftCalcWorksheet2 dcw1, DriftCalcWorksheet2 dcw2, DriftCalcWorksheet2 dcw3, DriftCalcWorksheet2 dcw4) {
        String date = "";
        InputStream is = getClass().getResourceAsStream(JASPER_WORKSHEET2);
//        File file = new File(JASPER_WORKSHEET2);
        HashMap<String, Object> params = new HashMap<String, Object>();
        String compiledBy = "";
        String fatigueFactor = "";
        //Date formatting
        date = Tanggal.dateStringConvert(dcw1.getIncident().getAlertedat());
        //String formatting
        if (!dcw1.getModifiedBy().equalsIgnoreCase("") && dcw1.getModifiedBy() != null) {
            compiledBy = dcw1.getModifiedBy();
        } else {
            if (!dcw1.getCreatedBy().equalsIgnoreCase("") && dcw1.getCreatedBy() != null) {
                compiledBy = dcw1.getCreatedBy();
            }
        }
        if (dcw1.getFatigueCorrectionFactor() == 0) {
            fatigueFactor = "Yes";
        } else {
            fatigueFactor = "No";
        }
        //Insert parameters
        params.put("COMPILED_BY", compiledBy + " ");
        params.put("DATE", date + " ");
        params.put("SEARCH_PLATFORM", dcw1.getSearchPlatform() + " ");
        params.put("SEARCH_PLATFORM_TAS", dcw1.getSearchPlatformTas().toString() + " ");
        params.put("SEARCH_OBJECT", dcw1.getSearchObject() + " ");
        params.put("MET_VISIBILITY", dcw1.getMeteorologicalVisibility().toString() + " ");
        params.put("WIND", dcw1.getWindSpeed().toString() + " ");
        params.put("FATIGUE_FACTOR", fatigueFactor + " ");

        String searchHeight1 = Integer.toString(dcw1.getSearchHeight().intValue());
        //SearchHeight 1
        params.put("SEARCH_HEIGHT_1", searchHeight1.toString() + " ft    ");
        if (dcw1.getUnit() == 0) {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + " NM   ");
        } else {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + " KM   ");
        }

        params.put("WEATHER_CORRECTION_FACTOR_1", dcw1.getWeatherCorrectionFactor().toString() + "   ");
        params.put("SPEED_CORRECTION_FACTOR_1", dcw1.getSpeedCorrectionFactor().toString() + "   ");
        params.put("SWEEP_WIDTH_FACTOR_1", dcw1.getSweepWidthFactor().toString() + "   ");
        params.put("PRACTICAL_TRACK_SPACING_1", dcw1.getPracticalTrackSpacing().toString() + "   ");
        params.put("COVERAGE_FACTOR_1", dcw1.getCoverageFactor().toString() + "   ");
        params.put("POD_1", dcw1.getProbabilityOfDetection().toString() + "   ");
        params.put("SEARCH_AREA_1", dcw1.getSearchArea().toString() + "   ");
        params.put("SEARCH_HOURS_1", dcw1.getSearchhours().toString() + "   ");
        if (dcw2 != null) {
            String searchHeight2 = Integer.toString(dcw2.getSearchHeight().intValue());
            //SearchHeight 2
            params.put("SEARCH_HEIGHT_2", searchHeight2.toString() + " ft    ");
            if (dcw2.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + " NM   ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + " KM   ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_2", dcw2.getWeatherCorrectionFactor().toString() + "   ");
            params.put("SPEED_CORRECTION_FACTOR_2", dcw2.getSpeedCorrectionFactor().toString() + "   ");
            params.put("SWEEP_WIDTH_FACTOR_2", dcw2.getSweepWidthFactor().toString() + "   ");
            params.put("PRACTICAL_TRACK_SPACING_2", dcw2.getPracticalTrackSpacing().toString() + "   ");
            params.put("COVERAGE_FACTOR_2", dcw2.getCoverageFactor().toString() + "   ");
            params.put("POD_2", dcw2.getProbabilityOfDetection().toString() + "   ");
            params.put("SEARCH_AREA_2", dcw2.getSearchArea().toString() + "   ");
            params.put("SEARCH_HOURS_2", dcw2.getSearchhours().toString() + "   ");
        }
        if (dcw3 != null) {
            String searchHeight3 = Integer.toString(dcw3.getSearchHeight().intValue());
            //SearchHeight 3
            params.put("SEARCH_HEIGHT_3", searchHeight3.toString() + " ft    ");
            if (dcw3.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + " NM   ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + " KM   ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_3", dcw3.getWeatherCorrectionFactor().toString() + "   ");
            params.put("SPEED_CORRECTION_FACTOR_3", dcw3.getSpeedCorrectionFactor().toString() + "   ");
            params.put("SWEEP_WIDTH_FACTOR_3", dcw3.getSweepWidthFactor().toString() + "   ");
            params.put("PRACTICAL_TRACK_SPACING_3", dcw3.getPracticalTrackSpacing().toString() + "   ");
            params.put("COVERAGE_FACTOR_3", dcw3.getCoverageFactor().toString() + "   ");
            params.put("POD_3", dcw3.getProbabilityOfDetection().toString() + "   ");
            params.put("SEARCH_AREA_3", dcw3.getSearchArea().toString() + "   ");
            params.put("SEARCH_HOURS_3", dcw3.getSearchhours().toString() + "   ");
        }
        if (dcw4 != null) {
            String searchHeight4 = Integer.toString(dcw4.getSearchHeight().intValue());
            //SearchHeight 4
            params.put("SEARCH_HEIGHT_4", searchHeight4.toString() + " ft    ");
            if (dcw4.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + " NM   ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + " KM   ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_4", dcw4.getWeatherCorrectionFactor().toString() + "   ");
            params.put("SPEED_CORRECTION_FACTOR_4", dcw4.getSpeedCorrectionFactor().toString() + "   ");
            params.put("SWEEP_WIDTH_FACTOR_4", dcw4.getSweepWidthFactor().toString() + "   ");
            params.put("PRACTICAL_TRACK_SPACING_4", dcw4.getPracticalTrackSpacing().toString() + "   ");
            params.put("COVERAGE_FACTOR_4", dcw4.getCoverageFactor().toString() + "   ");
            params.put("POD_4", dcw4.getProbabilityOfDetection().toString() + "   ");
            params.put("SEARCH_AREA_4", dcw4.getSearchArea().toString() + "   ");
            params.put("SEARCH_HOURS_4", dcw4.getSearchhours().toString() + "   ");
        }
        //Generating pdf
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(is);
//            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
//            JasperExportManager.exportReportToPdfFile(jp, fileOut);

            JRViewer jrv = new JRViewer(jp);
            limitJRViewerFormats limit = new limitJRViewerFormats();
            limit.limitJRViewerFormats(jrv);
            final JFrame jf = new JFrame();

            BufferedImage Image = null;
            try {
                Image = ImageIO.read(jf.getClass().getResource(BASARNAS_LOGO));
            } catch (Exception ex) {

            }
            jf.setIconImage(Image);
            jf.getContentPane().add(jrv);
            jf.validate();
            jf.setVisible(true);
            jf.setTitle("Laporan Worksheet 2");
            jf.setSize(new Dimension(850, 600));
            jf.setLocation(300, 100);
            jf.setAlwaysOnTop(true);
            jf.setResizable(false);

            jf.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    jf.setAlwaysOnTop(false);
                }

            });
        } catch (JRException ex) {
        }
    }

    public HashMap<String, Object> returnMapWorksheet2Report(DriftCalcWorksheet2 dcw1, DriftCalcWorksheet2 dcw2, DriftCalcWorksheet2 dcw3, DriftCalcWorksheet2 dcw4, String user) {
        String date = "";
        InputStream is = getClass().getResourceAsStream(JASPER_WORKSHEET2);
//        File file = new File(JASPER_WORKSHEET2);
        HashMap<String, Object> params = new HashMap<String, Object>();
        String compiledBy = "";
        String fatigueFactor = "";
        //Date formatting
        date = Tanggal.dateStringConvert(dcw1.getIncident().getAlertedat());
        //String formatting
        if (dcw1.getModifiedBy() != null && !dcw1.getModifiedBy().equalsIgnoreCase("")) {
            compiledBy = dcw1.getModifiedBy();
        } else {
            if (dcw1.getCreatedBy() != null && !dcw1.getCreatedBy().equalsIgnoreCase("")) {
                compiledBy = dcw1.getCreatedBy();
            }
        }
        if (dcw1.getFatigueCorrectionFactor() == 0) {
            fatigueFactor = "Yes";
        } else {
            fatigueFactor = "No";
        }
        //Insert parameters
        params.put("COMPILED_BY", compiledBy + " ");
        params.put("DATE", date + " ");
        params.put("SEARCH_PLATFORM", dcw1.getSearchPlatform() + " ");
        params.put("SEARCH_PLATFORM_TAS", dcw1.getSearchPlatformTas().toString() + " ");
        params.put("SEARCH_OBJECT", dcw1.getSearchObject() + " ");
        params.put("MET_VISIBILITY", dcw1.getMeteorologicalVisibility().toString() + " ");
        params.put("WIND", dcw1.getWindSpeed().toString() + " ");
        params.put("FATIGUE_FACTOR", fatigueFactor + " ");

        String searchHeight1 = Integer.toString(dcw1.getSearchHeight().intValue());
        //SearchHeight 1
        params.put("SEARCH_HEIGHT_1", searchHeight1.toString() + " ft    ");
        if (dcw1.getUnit() == 0) {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + " NM   ");
        } else {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + " KM   ");
        }

        params.put("WEATHER_CORRECTION_FACTOR_1", dcw1.getWeatherCorrectionFactor().toString() + "   ");
        params.put("SPEED_CORRECTION_FACTOR_1", dcw1.getSpeedCorrectionFactor().toString() + "   ");
        params.put("SWEEP_WIDTH_FACTOR_1", dcw1.getSweepWidthFactor().toString() + "   ");
        params.put("PRACTICAL_TRACK_SPACING_1", dcw1.getPracticalTrackSpacing().toString() + "   ");
        params.put("COVERAGE_FACTOR_1", dcw1.getCoverageFactor().toString() + "   ");
        params.put("POD_1", dcw1.getProbabilityOfDetection().toString() + "   ");
        params.put("SEARCH_AREA_1", dcw1.getSearchArea().toString() + "   ");
        params.put("SEARCH_HOURS_1", dcw1.getSearchhours().toString() + "   ");
        params.put("user", user + " ");
        if (dcw2 != null) {
            String searchHeight2 = Integer.toString(dcw2.getSearchHeight().intValue());
            //SearchHeight 2
            params.put("SEARCH_HEIGHT_2", searchHeight2.toString() + " ft    ");
            if (dcw2.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + " NM   ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + " KM   ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_2", dcw2.getWeatherCorrectionFactor().toString() + "   ");
            params.put("SPEED_CORRECTION_FACTOR_2", dcw2.getSpeedCorrectionFactor().toString() + "   ");
            params.put("SWEEP_WIDTH_FACTOR_2", dcw2.getSweepWidthFactor().toString() + "   ");
            params.put("PRACTICAL_TRACK_SPACING_2", dcw2.getPracticalTrackSpacing().toString() + "   ");
            params.put("COVERAGE_FACTOR_2", dcw2.getCoverageFactor().toString() + "   ");
            params.put("POD_2", dcw2.getProbabilityOfDetection().toString() + "   ");
            params.put("SEARCH_AREA_2", dcw2.getSearchArea().toString() + "   ");
            params.put("SEARCH_HOURS_2", dcw2.getSearchhours().toString() + "   ");
        }
        if (dcw3 != null) {
            String searchHeight3 = Integer.toString(dcw3.getSearchHeight().intValue());
            //SearchHeight 3
            params.put("SEARCH_HEIGHT_3", searchHeight3.toString() + " ft    ");
            if (dcw3.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + " NM   ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + " KM   ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_3", dcw3.getWeatherCorrectionFactor().toString() + "   ");
            params.put("SPEED_CORRECTION_FACTOR_3", dcw3.getSpeedCorrectionFactor().toString() + "   ");
            params.put("SWEEP_WIDTH_FACTOR_3", dcw3.getSweepWidthFactor().toString() + "   ");
            params.put("PRACTICAL_TRACK_SPACING_3", dcw3.getPracticalTrackSpacing().toString() + "   ");
            params.put("COVERAGE_FACTOR_3", dcw3.getCoverageFactor().toString() + "   ");
            params.put("POD_3", dcw3.getProbabilityOfDetection().toString() + "   ");
            params.put("SEARCH_AREA_3", dcw3.getSearchArea().toString() + "   ");
            params.put("SEARCH_HOURS_3", dcw3.getSearchhours().toString() + "   ");
        }
        if (dcw4 != null) {
            String searchHeight4 = Integer.toString(dcw4.getSearchHeight().intValue());
            //SearchHeight 4
            params.put("SEARCH_HEIGHT_4", searchHeight4.toString() + " ft    ");
            if (dcw4.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + " NM   ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + " KM   ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_4", dcw4.getWeatherCorrectionFactor().toString() + "   ");
            params.put("SPEED_CORRECTION_FACTOR_4", dcw4.getSpeedCorrectionFactor().toString() + "   ");
            params.put("SWEEP_WIDTH_FACTOR_4", dcw4.getSweepWidthFactor().toString() + "   ");
            params.put("PRACTICAL_TRACK_SPACING_4", dcw4.getPracticalTrackSpacing().toString() + "   ");
            params.put("COVERAGE_FACTOR_4", dcw4.getCoverageFactor().toString() + "   ");
            params.put("POD_4", dcw4.getProbabilityOfDetection().toString() + "   ");
            params.put("SEARCH_AREA_4", dcw4.getSearchArea().toString() + "   ");
            params.put("SEARCH_HOURS_4", dcw4.getSearchhours().toString() + "   ");
        }
        return params;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generateWorksheet3Report">
    /**
     * Fungsi untuk menuliskan data Drift Calculation Worksheet 3 dalam bentuk
     * pdf
     *
     * @param dcw1 Object DriftCalcWorksheet3 yang akan dituliskan dalam pdf
     * pada kolom pertama
     * @param dcw2 Object DriftCalcWorksheet3 yang akan dituliskan dalam pdf
     * pada kolom kedua
     * @param dcw3 Object DriftCalcWorksheet3 yang akan dituliskan dalam pdf
     * pada kolom ketiga
     * @param dcw4 Object DriftCalcWorksheet3 yang akan dituliskan dalam pdf
     * pada kolom keempat
     * @param fileOut String path file pdf akan disimpan
     */
    public void generateWorksheet3Report(DriftCalcWorksheet3 dcw1, DriftCalcWorksheet3 dcw2, DriftCalcWorksheet3 dcw3, DriftCalcWorksheet3 dcw4, String fileOut) {
        String date = "";
        InputStream is = getClass().getResourceAsStream(JASPER_WORKSHEET3);
//        File file = new File(JASPER_WORKSHEET3);
        HashMap<String, Object> params = new HashMap<String, Object>();
        String compiledBy = "";
        String fatigueFactor = "";
        //Date formatting
        date = Tanggal.dateStringConvert(dcw1.getIncident().getAlertedat());
        //String formatting
        if (!dcw1.getModifiedBy().equalsIgnoreCase("") && dcw1.getModifiedBy() != null) {
            compiledBy = dcw1.getModifiedBy();
        } else {
            if (!dcw1.getCreatedBy().equalsIgnoreCase("") && dcw1.getCreatedBy() != null) {
                compiledBy = dcw1.getCreatedBy();
            }
        }
        if (dcw1.getFatigueCorrectionFactor() == 0) {
            fatigueFactor = "Yes";
        } else {
            fatigueFactor = "No";
        }
        //Insert parameters
        params.put("COMPILED_BY", compiledBy + " ");
        params.put("DATE", date + " ");
        params.put("SEARCH_OBJECT", dcw1.getSearchObject() + " ");
        params.put("MET_VISIBILITY", dcw1.getMeteorologicalVisibility().toString() + " ");
        params.put("VEGETATION", dcw1.getVegetation() + " ");
        params.put("FATIGUE_FACTOR", fatigueFactor + " ");
        String searchHeight1 = Integer.toString(dcw1.getSearchHeight().intValue());
        //Search Height 1
        params.put("SEARCH_HEIGHT_1", searchHeight1 + " ft ");
        if (dcw1.getUnit() == 0) {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  NM  ");
        } else {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  KM  ");
        }

        params.put("TERRAIN_CORRECTION_FACTOR_1", dcw1.getTerrainCorrectionFactor().toString() + " ");
        params.put("FATIGUE_CORRECTION_FACTOR_1", dcw1.getFatigueCorrectionFactor().toString() + " ");
        params.put("SWEEP_WIDTH_FACTOR_1", dcw1.getSweepWidthFactor().toString() + " ");
        params.put("PRACTICAL_TRACK_SPACING_1", dcw1.getPracticalTrackSpacing().toString() + " ");
        params.put("COVERAGE_FACTOR_1", dcw1.getCoverageFactor().toString() + " ");
        params.put("POD_1", dcw1.getProbabilityOfDetection().toString() + " ");
        params.put("SEARCH_AREA_1", dcw1.getSearchArea().toString() + " ");
        params.put("SEARCH_HOURS_1", dcw1.getSearchhours().toString() + " ");
        if (dcw2 != null) {
            String searchHeight2 = Integer.toString(dcw2.getSearchHeight().intValue());
            //Search Height 2
            params.put("SEARCH_HEIGHT_2", searchHeight2 + " ft ");
            if (dcw2.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  NM  ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  KM  ");
            }

            params.put("TERRAIN_CORRECTION_FACTOR_2", dcw2.getTerrainCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_2", dcw2.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_2", dcw2.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_2", dcw2.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_2", dcw2.getCoverageFactor().toString() + " ");
            params.put("POD_2", dcw2.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_2", dcw2.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_2", dcw2.getSearchhours().toString() + " ");
        }
        if (dcw3 != null) {
            String searchHeight3 = Integer.toString(dcw3.getSearchHeight().intValue());
            //Search Height 3
            params.put("SEARCH_HEIGHT_3", searchHeight3 + " ft ");
            if (dcw3.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + "  NM  ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + "  KM  ");
            }

            params.put("TERRAIN_CORRECTION_FACTOR_3", dcw3.getTerrainCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_3", dcw3.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_3", dcw3.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_3", dcw3.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_3", dcw3.getCoverageFactor().toString() + " ");
            params.put("POD_3", dcw3.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_3", dcw3.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_3", dcw3.getSearchhours().toString() + " ");
        }
        if (dcw4 != null) {
            String searchHeight4 = Integer.toString(dcw4.getSearchHeight().intValue());
            //Search Height 4
            params.put("SEARCH_HEIGHT_4", searchHeight4 + " ft ");
            if (dcw4.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + "  NM  ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + "  KM  ");
            }

            params.put("TERRAIN_CORRECTION_FACTOR_4", dcw4.getTerrainCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_4", dcw4.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_4", dcw4.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_4", dcw4.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_4", dcw4.getCoverageFactor().toString() + " ");
            params.put("POD_4", dcw4.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_4", dcw4.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_4", dcw4.getSearchhours().toString() + " ");
        }

        //Generating pdf
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(is);
//            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
//            JasperExportManager.exportReportToPdfFile(jp, fileOut);

            JRViewer jrv = new JRViewer(jp);
            limitJRViewerFormats limit = new limitJRViewerFormats();
            limit.limitJRViewerFormats(jrv);
            final JFrame jf = new JFrame();

            BufferedImage Image = null;
            try {
                Image = ImageIO.read(jf.getClass().getResource(BASARNAS_LOGO));
            } catch (Exception ex) {

            }
            jf.setIconImage(Image);
            jf.getContentPane().add(jrv);
            jf.validate();
            jf.setVisible(true);
            jf.setTitle("Laporan Worksheet 3");
            jf.setSize(new Dimension(850, 600));
            jf.setLocation(300, 100);
            jf.setAlwaysOnTop(true);
            jf.setResizable(false);

            jf.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    jf.setAlwaysOnTop(false);
                }

            });
        } catch (JRException ex) {
        }
    }

    public HashMap<String, Object> returnMapWorksheet3Report(DriftCalcWorksheet3 dcw1, DriftCalcWorksheet3 dcw2, DriftCalcWorksheet3 dcw3, DriftCalcWorksheet3 dcw4, String user) {
        String date = "";
        InputStream is = getClass().getResourceAsStream(JASPER_WORKSHEET3);
//        File file = new File(JASPER_WORKSHEET3);
        HashMap<String, Object> params = new HashMap<String, Object>();

        String compiledBy = "";
        String fatigueFactor = "";
        //Date formatting
        System.out.println("TEST 676 " + dcw1.getIncident().getAlertedat());
        date = Tanggal.dateStringConvert(dcw1.getIncident().getAlertedat() == null
                ? new Date() : dcw1.getIncident().getAlertedat());
        //String formatting
        if (dcw1.getModifiedBy() != null && !dcw1.getModifiedBy().equalsIgnoreCase("")) {
            compiledBy = dcw1.getModifiedBy();
        } else {
            if (dcw1.getCreatedBy() != null && !dcw1.getCreatedBy().equalsIgnoreCase("")) {
                compiledBy = dcw1.getCreatedBy();
            }
        }

        if (dcw1.getFatigueCorrectionFactor() == 0) {
            fatigueFactor = "Yes";
        } else {
            fatigueFactor = "No";
        }
        //Insert parameters
        params.put("COMPILED_BY", compiledBy + " ");
        params.put("DATE", date + " ");
        params.put("SEARCH_OBJECT", dcw1.getSearchObject() + " ");
        params.put("MET_VISIBILITY", dcw1.getMeteorologicalVisibility().toString() + " ");
        params.put("VEGETATION", dcw1.getVegetation() + " ");
        params.put("FATIGUE_FACTOR", fatigueFactor + " ");
        String searchHeight1 = Integer.toString(dcw1.getSearchHeight().intValue());
        //Search Height 1
        params.put("SEARCH_HEIGHT_1", searchHeight1 + " ft ");
        if (dcw1.getUnit() == 0) {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  NM  ");
        } else {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  KM  ");
        }

        params.put("TERRAIN_CORRECTION_FACTOR_1", dcw1.getTerrainCorrectionFactor().toString() + " ");
        params.put("FATIGUE_CORRECTION_FACTOR_1", dcw1.getFatigueCorrectionFactor().toString() + " ");
        params.put("SWEEP_WIDTH_FACTOR_1", dcw1.getSweepWidthFactor().toString() + " ");
        params.put("PRACTICAL_TRACK_SPACING_1", dcw1.getPracticalTrackSpacing().toString() + " ");
        params.put("COVERAGE_FACTOR_1", dcw1.getCoverageFactor().toString() + " ");
        params.put("POD_1", dcw1.getProbabilityOfDetection().toString() + " ");
        params.put("SEARCH_AREA_1", dcw1.getSearchArea().toString() + " ");
        params.put("SEARCH_HOURS_1", dcw1.getSearchhours().toString() + " ");
        params.put("user", user + " ");
        if (dcw2 != null) {
            String searchHeight2 = Integer.toString(dcw2.getSearchHeight().intValue());
            //Search Height 2
            params.put("SEARCH_HEIGHT_2", searchHeight2 + " ft ");
            if (dcw2.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  NM  ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  KM  ");
            }

            params.put("TERRAIN_CORRECTION_FACTOR_2", dcw2.getTerrainCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_2", dcw2.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_2", dcw2.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_2", dcw2.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_2", dcw2.getCoverageFactor().toString() + " ");
            params.put("POD_2", dcw2.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_2", dcw2.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_2", dcw2.getSearchhours().toString() + " ");
        }
        if (dcw3 != null) {
            String searchHeight3 = Integer.toString(dcw3.getSearchHeight().intValue());
            //Search Height 3
            params.put("SEARCH_HEIGHT_3", searchHeight3 + " ft ");
            if (dcw3.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + "  NM  ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_3", dcw3.getUncorrectedSweepWidth().toString() + "  KM  ");
            }

            params.put("TERRAIN_CORRECTION_FACTOR_3", dcw3.getTerrainCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_3", dcw3.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_3", dcw3.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_3", dcw3.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_3", dcw3.getCoverageFactor().toString() + " ");
            params.put("POD_3", dcw3.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_3", dcw3.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_3", dcw3.getSearchhours().toString() + " ");
        }
        if (dcw4 != null) {
            String searchHeight4 = Integer.toString(dcw4.getSearchHeight().intValue());
            //Search Height 4
            params.put("SEARCH_HEIGHT_4", searchHeight4 + " ft ");
            if (dcw4.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + "  NM  ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_4", dcw4.getUncorrectedSweepWidth().toString() + "  KM  ");
            }

            params.put("TERRAIN_CORRECTION_FACTOR_4", dcw4.getTerrainCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_4", dcw4.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_4", dcw4.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_4", dcw4.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_4", dcw4.getCoverageFactor().toString() + " ");
            params.put("POD_4", dcw4.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_4", dcw4.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_4", dcw4.getSearchhours().toString() + " ");
        }
        return params;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="generateWorksheet8Report">
    /**
     * Fungsi untuk menuliskan data Drift Calculation Worksheet 8 dalam bentuk
     * pdf
     *
     * @param dcw1 Object DriftCalcWorksheet8 yang akan dituliskan dalam pdf
     * @param fileOut String path file pdf akan disimpan
     */
    public void generateWorksheet8Report(DriftCalcWorksheet8 dcw1, DriftCalcWorksheet8 dcw2, String fileOut) {
        String date = "";
        InputStream is = getClass().getResourceAsStream(JASPER_WORKSHEET8);
//        File file = new File(JASPER_WORKSHEET8);
        HashMap<String, Object> params = new HashMap<String, Object>();
        String compiledBy = "";
        String fatigueFactor = "";
        String searchHeight1 = "";
        String searchHeight2 = "";
        //Date formatting
        date = Tanggal.dateStringConvert(dcw1.getIncident().getAlertedat());
        //String formatting
        if (!dcw1.getModifiedBy().equalsIgnoreCase("") && dcw1.getModifiedBy() != null) {
            compiledBy = dcw1.getModifiedBy();
        } else {
            if (!dcw1.getCreatedBy().equalsIgnoreCase("") && dcw1.getCreatedBy() != null) {
                compiledBy = dcw1.getCreatedBy();
            }
        }
        if (dcw1.getFatigueCorrectionFactor() == 0) {
            fatigueFactor = "Yes";
        } else {
            fatigueFactor = "No";
        }
        if (dcw1.getSearchHeight().equals(Double.valueOf("0"))) {
            searchHeight1 = "MERSHIP ";
        } else if (dcw1.getSearchHeight().equals(Double.valueOf("8"))) {
            searchHeight1 = "Eye Height 8 ft ";
        } else if (dcw1.getSearchHeight().equals(Double.valueOf("14"))) {
            searchHeight1 = "Eye Height 14 ft ";
        }
        //Insert parameters
        params.put("COMPILED_BY", compiledBy + " ");
        params.put("DATE", date + " ");
        params.put("SEARCH_PLATFORM", dcw1.getSearchPlatform() + " ");
        params.put("SEARCH_OBJECT", dcw1.getSearchObject() + " ");
        params.put("MET_VISIBILITY", dcw1.getMeteorologicalVisibility().toString() + " ");
        params.put("WIND", dcw1.getWindSpeed().toString() + " ");
        params.put("FATIGUE_FACTOR", fatigueFactor + " ");
        params.put("SEARCH_HEIGHT_1", searchHeight1 + " ");
        if (dcw1.getUnit() == 0) {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  NM    ");
        } else {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  KM    ");
        }

        params.put("WEATHER_CORRECTION_FACTOR_1", dcw1.getWeatherCorrectionFactor().toString() + " ");
        params.put("FATIGUE_CORRECTION_FACTOR_1", dcw1.getFatigueCorrectionFactor().toString() + " ");
        params.put("SWEEP_WIDTH_FACTOR_1", dcw1.getSweepWidthFactor().toString() + " ");
        params.put("PRACTICAL_TRACK_SPACING_1", dcw1.getPracticalTrackSpacing().toString() + " ");
        params.put("COVERAGE_FACTOR_1", dcw1.getCoverageFactor().toString() + " ");
        params.put("POD_1", dcw1.getProbabilityOfDetection().toString() + " ");
        params.put("SEARCH_AREA_1", dcw1.getSearchArea().toString() + " ");
        params.put("SEARCH_HOURS_1", dcw1.getSearchhours().toString() + " ");
        if (dcw2 != null) {
            if (dcw2.getSearchHeight().equals(Double.valueOf("0"))) {
                searchHeight2 = "MERSHIP ";
            } else if (dcw2.getSearchHeight().equals(Double.valueOf("8"))) {
                searchHeight2 = "Eye Height 8 ft ";
            } else if (dcw2.getSearchHeight().equals(Double.valueOf("14"))) {
                searchHeight2 = "Eye Height 14 ft ";
            }
            params.put("SEARCH_HEIGHT_2", searchHeight2 + " ");
            if (dcw2.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  NM    ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  KM    ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_2", dcw2.getWeatherCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_2", dcw2.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_2", dcw2.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_2", dcw2.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_2", dcw2.getCoverageFactor().toString() + " ");
            params.put("POD_2", dcw2.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_2", dcw2.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_2", dcw2.getSearchhours().toString() + " ");
        }
        //Generating pdf
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(is);
//            JasperReport jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
//            JasperExportManager.exportReportToPdfFile(jp, fileOut);

            JRViewer jrv = new JRViewer(jp);
            limitJRViewerFormats limit = new limitJRViewerFormats();
            limit.limitJRViewerFormats(jrv);
            final JFrame jf = new JFrame();

            BufferedImage Image = null;
            try {
                Image = ImageIO.read(jf.getClass().getResource(BASARNAS_LOGO));
            } catch (Exception ex) {

            }
            jf.setIconImage(Image);
            jf.getContentPane().add(jrv);
            jf.validate();
            jf.setVisible(true);
            jf.setTitle("Laporan Worksheet 2");
            jf.setSize(new Dimension(850, 600));
            jf.setLocation(300, 100);
            jf.setAlwaysOnTop(true);
            jf.setResizable(false);

            jf.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    jf.setAlwaysOnTop(false);
                }

            });
        } catch (JRException ex) {
        }
    }
    //</editor-fold>

    public HashMap<String, Object> returnMapWorksheet8Report(DriftCalcWorksheet8 dcw1, DriftCalcWorksheet8 dcw2, String user) {
        String date = "";
        InputStream is = getClass().getResourceAsStream(JASPER_WORKSHEET8);
//        File file = new File(JASPER_WORKSHEET8);
        HashMap<String, Object> params = new HashMap<String, Object>();
        String compiledBy = "";
        String fatigueFactor = "";
        String searchHeight1 = "";
        String searchHeight2 = "";
        //Date formatting
        date = Tanggal.dateStringConvert(dcw1.getIncident().getAlertedat());
        //String formatting

        if (dcw1.getModifiedBy() != null && !dcw1.getModifiedBy().equalsIgnoreCase("")) {
            compiledBy = dcw1.getModifiedBy();
        } else {
            if (dcw1.getCreatedBy() != null && !dcw1.getCreatedBy().equalsIgnoreCase("")) {
                compiledBy = dcw1.getCreatedBy();
            }
        }
        if (dcw1.getFatigueCorrectionFactor() == 0) {
            fatigueFactor = "Yes";
        } else {
            fatigueFactor = "No";
        }
        if (dcw1.getSearchHeight().equals(Double.valueOf("0"))) {
            searchHeight1 = "MERSHIP ";
        } else if (dcw1.getSearchHeight().equals(Double.valueOf("8"))) {
            searchHeight1 = "Eye Height 8 ft ";
        } else if (dcw1.getSearchHeight().equals(Double.valueOf("14"))) {
            searchHeight1 = "Eye Height 14 ft ";
        }
        //Insert parameters
        params.put("COMPILED_BY", compiledBy + " ");
        params.put("DATE", date + " ");
        params.put("SEARCH_PLATFORM", dcw1.getSearchPlatform() + " ");
        params.put("SEARCH_OBJECT", dcw1.getSearchObject() + " ");
        params.put("MET_VISIBILITY", dcw1.getMeteorologicalVisibility().toString() + " ");
        params.put("WIND", dcw1.getWindSpeed().toString() + " ");
        params.put("FATIGUE_FACTOR", fatigueFactor + " ");
        params.put("SEARCH_HEIGHT_1", searchHeight1 + " ");
        if (dcw1.getUnit() == 0) {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  NM    ");
        } else {
            params.put("UNCORRECTED_SWEEP_WIDTH_1", dcw1.getUncorrectedSweepWidth().toString() + "  KM    ");
        }

        params.put("WEATHER_CORRECTION_FACTOR_1", dcw1.getWeatherCorrectionFactor().toString() + " ");
        params.put("FATIGUE_CORRECTION_FACTOR_1", dcw1.getFatigueCorrectionFactor().toString() + " ");
        params.put("SWEEP_WIDTH_FACTOR_1", dcw1.getSweepWidthFactor().toString() + " ");
        params.put("PRACTICAL_TRACK_SPACING_1", dcw1.getPracticalTrackSpacing().toString() + " ");
        params.put("COVERAGE_FACTOR_1", dcw1.getCoverageFactor().toString() + " ");
        params.put("POD_1", dcw1.getProbabilityOfDetection().toString() + " ");
        params.put("SEARCH_AREA_1", dcw1.getSearchArea().toString() + " ");
        params.put("SEARCH_HOURS_1", dcw1.getSearchhours().toString() + " ");
        params.put("user", user + " ");
        if (dcw2 != null) {
            if (dcw2.getSearchHeight().equals(Double.valueOf("0"))) {
                searchHeight2 = "MERSHIP ";
            } else if (dcw2.getSearchHeight().equals(Double.valueOf("8"))) {
                searchHeight2 = "Eye Height 8 ft ";
            } else if (dcw2.getSearchHeight().equals(Double.valueOf("14"))) {
                searchHeight2 = "Eye Height 14 ft ";
            }
            params.put("SEARCH_HEIGHT_2", searchHeight2 + " ");
            if (dcw2.getUnit() == 0) {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  NM    ");
            } else {
                params.put("UNCORRECTED_SWEEP_WIDTH_2", dcw2.getUncorrectedSweepWidth().toString() + "  KM    ");
            }

            params.put("WEATHER_CORRECTION_FACTOR_2", dcw2.getWeatherCorrectionFactor().toString() + " ");
            params.put("FATIGUE_CORRECTION_FACTOR_2", dcw2.getFatigueCorrectionFactor().toString() + " ");
            params.put("SWEEP_WIDTH_FACTOR_2", dcw2.getSweepWidthFactor().toString() + " ");
            params.put("PRACTICAL_TRACK_SPACING_2", dcw2.getPracticalTrackSpacing().toString() + " ");
            params.put("COVERAGE_FACTOR_2", dcw2.getCoverageFactor().toString() + " ");
            params.put("POD_2", dcw2.getProbabilityOfDetection().toString() + " ");
            params.put("SEARCH_AREA_2", dcw2.getSearchArea().toString() + " ");
            params.put("SEARCH_HOURS_2", dcw2.getSearchhours().toString() + " ");
            params.put("user", user + " ");
        }
        return params;
    }

    public void generateWorksheet2Report(DriftCalcWorksheet2 dcw, String fileOut) {
        generateWorksheet2Report(dcw, dcw, dcw, dcw, fileOut);
    }

    public void generateWorksheet3Report(DriftCalcWorksheet3 dcw, String fileOut) {
        generateWorksheet3Report(dcw, dcw, dcw, dcw, fileOut);
    }

    public void generateWorksheet8Report(DriftCalcWorksheet8 dcw, String fileOut) {
        generateWorksheet8Report(dcw, dcw, fileOut);
    }

//    public static void main(String[] args) {
//        String fileOut1 = "/home/fredy/Desktop/report/worksheet1.pdf";
//        String fileOut2 = "/home/fredy/Desktop/report/worksheet2.pdf";
//        String fileOut3 = "/home/fredy/Desktop/report/worksheet3.pdf";
//        String fileOut8 = "/home/fredy/Desktop/report/worksheet8.pdf";
//        ReportUtilMBean me = new ReportUtilMBean();
    //DriftCalcWorksheet1 dw1 = driftCalcWorksheet2Repo.findByworksheetID("JKT-1109-0005");
//        DriftCalcWorksheet2 dw2 = driftCalcWorksheet2Repo.findByworksheetID("JKT-1109-0005");
//        DriftCalcWorksheet3 dw3 = DriftCalcWorksheet3ES.findDriftCalcWorksheet3ById("JKT-1109-0001");
//        DriftCalcWorksheet8 dw8 = DriftCalcWorksheet8ES.findDriftCalcWorksheet8ById("JKT-1109-0001");
    //      me.generateWorksheet1Report(dw1, fileOut1);
//        me.generateWorksheet2Report(dw2, fileOut2);
//        me.generateWorksheet3Report(dw3, fileOut3);
//        me.generateWorksheet8Report(dw8, fileOut8);
}
