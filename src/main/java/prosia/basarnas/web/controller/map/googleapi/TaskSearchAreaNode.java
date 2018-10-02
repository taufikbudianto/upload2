/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import org.primefaces.model.TreeNode;
import prosia.basarnas.model.IncTaskSearchArea;
import prosia.basarnas.service.map_js.GTaskSearchArea;
import prosia.basarnas.service.map_js.SearchPatternNode;
import prosia.basarnas.service.map_js.SearchPatternNodeContainer;

/**
 *
 * @author Aris
 */
public class TaskSearchAreaNode extends RoutingNode<IncTaskSearchArea> implements SearchPatternNodeContainer {

    private SearchAreaNode searchAreaNode;
    private GTaskSearchArea gTaskSearchArea;

    public TaskSearchAreaNode(SearchAreaNode searchAreaNode, GTaskSearchArea gTaskSearchArea, IncTaskSearchArea taskSearchArea, TreeNode treeModel) {
        //super(taskSearchArea, treeModel);
        this.gTaskSearchArea = gTaskSearchArea;
        //this.setUserObject(getObject().getName());
        //this.setImageIcon(TASK_SEARCH_AREA_ICON_32);
        this.searchAreaNode = searchAreaNode;
    }
    
    public void addSearchPatternNode(SearchPatternNode searchPatternNode){
        //getDefaultTreeModel().insertNodeInto(searchPatternNode, this, getChildCount());
    }

   
    @Override
    protected void remove() {
        //getDefaultTreeModel().removeNodeFromParent(this);
        //this.searchAreaNode.getTaskSearchAreaNodes().remove(this);
        getDefaultTreeModel().clearParent();
        this.searchAreaNode.getTaskSearchAreaNodes().remove(this);
    }

    public SearchAreaNode getSearchAreaNode() {
        return searchAreaNode;
    }

    public void setSearchAreaNode(SearchAreaNode searchAreaNode) {
        this.searchAreaNode = searchAreaNode;
    }

    public GTaskSearchArea getgTaskSearchArea() {
        return gTaskSearchArea;
    }

    public void setgTaskSearchArea(GTaskSearchArea gTaskSearchArea) {
        this.gTaskSearchArea = gTaskSearchArea;
    }
}
