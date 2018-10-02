/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Irfan Rofie
 */
@Component
@Scope("view")
@Data
public class ScreenshotMBean {

    private String mapMode;
    @Value("${uploadFolder}")
    private String direktory;

    public ScreenshotMBean() {
    }

    public void showDialog() {
        RequestContext.getCurrentInstance().reset("idDialogScreenshot");
        RequestContext.getCurrentInstance().update("idDialogScreenshot");
        RequestContext.getCurrentInstance().execute("PF('showDialogScreenshot').show()");
    }

    public void screenshotMap() throws FileNotFoundException, JAXBException {
        if (mapMode.equals("img")) {
            RequestContext.getCurrentInstance().execute("saveAsImage()");
            RequestContext.getCurrentInstance().execute("PF('eventDialog').show()");
        } else {
            Kml kml = new Kml();
            Document doc = kml.createAndSetDocument().withName("JAK Example").withOpen(Boolean.TRUE);
            Folder folder = doc.createAndAddFolder();
            folder.withName("Continents with Earth's surface").withOpen(Boolean.TRUE);
            // create Placemark elements
            createPlacemarkWithChart(doc, folder, 93.24607775062842, 47.49808862281773, "Asia", 30);
            createPlacemarkWithChart(doc, folder, 19.44601806124206, 10.13133611111111, "Africa", 20);
            createPlacemarkWithChart(doc, folder, -103.5286299241638, 41.26035225962401, "North America", 17);
            createPlacemarkWithChart(doc, folder, -59.96161780270248, -13.27347674076888, "South America", 12);
            createPlacemarkWithChart(doc, folder, 14.45531426360271, 47.26208181151567, "Europe", 7);
            createPlacemarkWithChart(doc, folder, 135.0555272486322, -26.23824399654937, "Australia", 6);

            // print and save
            //kml.marshal(new File("advancedexample1.kml"));
            Marshaller marshaller = JAXBContext.newInstance(new Class[]{Kml.class}).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
                @Override
                public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
                    return namespaceUri.matches("http://www.w3.org/\\d{4}/Atom") ? "atom"
                            : (namespaceUri.matches("urn:oasis:names:tc:ciq:xsdschema:xAL:.*?") ? "xal"
                            : (namespaceUri.matches("http://www.google.com/kml/ext/.*?") ? "gx"
                            : (namespaceUri.matches("http://www.opengis.net/kml/.*?") ? ""
                            : (null))));
                }
            });
            String strFile = direktory + "kml"
                    + File.separator + "sarcore_kml.kml";
            marshaller.marshal(kml, new File(strFile));
            RequestContext.getCurrentInstance().execute("window.open('/kml/sarcore_kml.kml','_blank')");
        }

    }

    private static void createPlacemarkWithChart(Document document, Folder folder, double longitude, double latitude,
            String continentName, int coveredLandmass) {

        int remainingLand = 100 - coveredLandmass;
        Icon icon = new Icon()
                .withHref("http://chart.apis.google.com/chart?chs=380x200&chd=t:" + coveredLandmass + "," + remainingLand + "&cht=p&chf=bg,s,ffffff00");
        Style style = document.createAndAddStyle();
        style.withId("style_" + continentName) // set the stylename to use this style from the placemark
                .createAndSetIconStyle().withScale(5.0).withIcon(icon); // set size and icon
        style.createAndSetLabelStyle().withColor("ff43b3ff").withScale(5.0); // set color and size of the continent name

        Placemark placemark = folder.createAndAddPlacemark();
        // use the style for each continent
        placemark.withName(continentName)
                .withStyleUrl("#style_" + continentName)
                // 3D chart imgae
                .withDescription(
                        "<![CDATA[<img src=\"http://chart.apis.google.com/chart?chs=430x200&chd=t:" + coveredLandmass + "," + remainingLand + "&cht=p3&chl=" + continentName + "|remaining&chtt=Earth's surface\" />")
                // coordinates and distance (zoom level) of the viewer
                .createAndSetLookAt().withLongitude(longitude).withLatitude(latitude).withAltitude(0).withRange(12000000);

        placemark.createAndSetPoint().addToCoordinates(longitude, latitude); // set coordinates
    }
}
