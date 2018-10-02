package prosia.basarnas.service;

import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.JRViewer;


public class limitJRViewerFormats {
    public JRViewer limitJRViewerFormats(JRViewer viewer)
      {
        // Entfernen aller Exportformate
        JRSaveContributor[] save = viewer.getSaveContributors();
        for (int i=0; i<save.length; i++) 
        {
          viewer.removeSaveContributor(save[i]);
        }

        // Allow these SaveContributor
        final String[] DEFAULT_CONTRIBUTORS = {
        /*"net.sf.jasperreports.view.save.JRPrintSaveContributor",*/
        "net.sf.jasperreports.view.save.JRPdfSaveContributor",
        "net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor",
        "net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor"
        };

        for(int i = 0; i < DEFAULT_CONTRIBUTORS.length; i++) 
        {
          try 
          {
            Class saveContribClass = JRClassLoader.loadClassForName(DEFAULT_CONTRIBUTORS[i]);
            ResourceBundle jrViewerResBundel = ResourceBundle.getBundle("net/sf/jasperreports/view/viewer", viewer.getLocale() );
            Constructor constructor = saveContribClass.getConstructor(new Class[]{Locale.class, ResourceBundle.class});
            JRSaveContributor saveContrib = (JRSaveContributor)constructor.newInstance(new Object[]{viewer.getLocale(), jrViewerResBundel });
            viewer.addSaveContributor(saveContrib);
          }
          catch (Exception e) {}
        }
        viewer.setZoomRatio((float) 0.7);
        return viewer;
      }
}
