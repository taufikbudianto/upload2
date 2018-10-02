/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import prosia.basarnas.constant.ProsiaConstant;
import prosia.basarnas.model.Layer;
import prosia.basarnas.repo.LayerRepo;
import prosia.basarnas.web.model.Document;

/**
 *
 * @author Irfan Rofie
 */
@Component
@Scope("view")
@Data
public class LayerMBean extends AbstractManagedBean implements InitializingBean {

    private Layer layer;
    private TreeNode treeLayer;
    private TreeNode selectedTreeLayer;
    private Double valueOpacity;

    @Autowired
    private LayerRepo layerRepo;

    public LayerMBean() {
        initLayer();
    }

    private void initLayer() {
        layer = new Layer();
        valueOpacity = 0.0;
    }

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createTreeLayer();
    }

    public void saveLayer() {
        if (StringUtils.isBlank(layer.getLayerId())) {
            layer.setLayerId(createLayerId());
            layer.setStatus(false);
        }
        layer.setOpacity(String.valueOf(valueOpacity));
        layerRepo.save(layer);
        createTreeLayer();
        initLayer();
        addPopUpMessage(FacesMessage.SEVERITY_INFO,
                "Sukses", "Layer berhasil disimpan");
        RequestContext.getCurrentInstance().update("idPanelMapSearch");
        RequestContext.getCurrentInstance()
                .execute("PF('showDialogLayer').hide()");
    }

    public void deleteLayer() {
        if (layer != null) {
            layer.setStatus(true);
            layerRepo.save(layer);
            createTreeLayer();
            addPopUpMessage(FacesMessage.SEVERITY_INFO,
                    "Sukses", "Layer berhasil dihapus");
            RequestContext.getCurrentInstance().update("idPanelMapSearch");
        }
    }

    public void createTreeLayer() {
        treeLayer = new CheckboxTreeNode();
        for (Layer lyr : layerRepo.findByStatusFalse()) {
            TreeNode node = new CheckboxTreeNode(new Document(lyr.getName(),
                    false, lyr, null), treeLayer);
        }
    }

    public void onContextMenu(NodeSelectEvent event) {
        Document doc = (Document) event.getTreeNode().getData();
        layer = (Layer) doc.getObjClass();
        /*if (event.getTreeNode().isSelected()) {
            event.getTreeNode().setSelected(false);
        }*/
    }

    public void onNodeSelect(NodeSelectEvent event) {
        Document doc = (Document) event.getTreeNode().getData();
        Layer layer = (Layer) doc.getObjClass();
        if (doc.getSelected()) {
            RequestContext.getCurrentInstance().execute(
                    "kmlLayerStart('" + layer.getPath() + "')");
        } else {
            RequestContext.getCurrentInstance().execute(
                    "kmlLayerStop()");
        }

    }

    private String createLayerId() {
        String kansarId = getCurrentUser().getResPersonnel().getOfficeSar().getKantorsarid();
        log.debug("kansarId : {}", kansarId);
        String nextval = "";
        Date date = new Date();
        String fromDate = new SimpleDateFormat("yy-MM-dd").format(date);
        String[] splitDate = fromDate.split("-");
        List<Layer> ias = layerRepo.findByLayerIdContaining(kansarId);
        String id = null;
        if (ias.isEmpty()) {
            id = kansarId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
        } else {
            String maxId = layerRepo.findByMaxId(kansarId);
            String[] splitId = maxId.split("-");
            if (maxId.contains(splitDate[0] + splitDate[1])) {
                int next = Integer.valueOf(splitId[2]) + 1;
                int length = String.valueOf(next).length();
                switch (length) {
                    case 1:
                        nextval = ProsiaConstant.SEQUENCE_000 + next;
                        break;
                    case 2:
                        nextval = ProsiaConstant.SEQUENCE_00 + next;
                        break;
                    case 3:
                        nextval = ProsiaConstant.SEQUENCE_0 + next;
                        break;
                    case 4:
                        nextval = "" + next;
                        break;
                    default:
                        break;
                }
                id = kansarId + "-" + splitDate[0] + splitDate[1] + "-" + nextval;
            } else if (Integer.parseInt(splitId[1]) < Integer.parseInt(splitDate[0] + splitDate[1])) {
                id = kansarId + "-" + splitDate[0] + splitDate[1] + ProsiaConstant.SEQUENCE_0001;
            }
        }
        return id;
    }

    public void showLayerDialog() {
        Boolean isNew = (Boolean) getRequestParam("isNew");
        if (StringUtils.isNotBlank(layer.getLayerId()) && isNew == null) {
            valueOpacity = Double.valueOf(layer.getOpacity());
        } else {
            initLayer();
        }
        RequestContext.getCurrentInstance().reset("idDialogLayer");
        RequestContext.getCurrentInstance().update("idDialogLayer");
        RequestContext.getCurrentInstance().execute("PF('showDialogLayer').show()");
    }
}
