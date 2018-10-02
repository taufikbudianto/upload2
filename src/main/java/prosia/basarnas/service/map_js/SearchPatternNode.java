/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

import org.primefaces.model.TreeNode;
import prosia.basarnas.model.Incident;
import prosia.basarnas.model.IncidentSearchPattern;
import prosia.basarnas.web.controller.map.googleapi.RoutingNode;

/**
 *
 * @author Aris
 */
public class SearchPatternNode extends RoutingNode<IncidentSearchPattern> {
    private Incident incident;
    
    public SearchPatternNode(IncidentSearchPattern pattern, TreeNode defaultTreeModel, Incident incident) {
        //super(pattern, defaultTreeModel);
        //setUserObject(pattern.getName());
        this.incident = incident;
        if(pattern.getType().equals(GSearchPattern.FREE_DEFINE)){
            //setImageIcon(FREE_DEFINE_ICON_32);
        }else if(pattern.getType().equals(GSearchPattern.SECTOR)){
            //setImageIcon(SECTOR_ICON_32);
        }else if(pattern.getType().equals(GSearchPattern.SQUARE)){
            //setImageIcon(SQUARE_ICON_32);
        }else if(pattern.getType().equals(GSearchPattern.PARAREL)){
            //setImageIcon(PARAREL_ICON_32);
        }else if(pattern.getType().equals(GSearchPattern.TSR)){
            //setImageIcon(TSR_ICON_32);
        }else if(pattern.getType().equals(GSearchPattern.TSN)){
           // setImageIcon(TSN_ICON_32);
        }else if(pattern.getType().equals(GSearchPattern.TRAPEZIUM)){
            //setImageIcon(PARAREL_ICON_32);
        }
    }

    @Override
    protected void remove() {
        
    }
}
