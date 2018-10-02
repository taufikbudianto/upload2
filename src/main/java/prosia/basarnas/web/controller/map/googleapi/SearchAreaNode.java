/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.TreeNode;
import prosia.basarnas.model.SearchArea;
import prosia.basarnas.service.map_js.SearchPatternNode;
import prosia.basarnas.service.map_js.SearchPatternNodeContainer;

/**
 *
 * @author Aris
 */
public abstract class SearchAreaNode extends RoutingNode<SearchArea> implements SearchPatternNodeContainer{
    
    private List<TaskSearchAreaNode> taskSearchAreaNodes = new ArrayList<TaskSearchAreaNode>();
    /**
     * this properties indicating whether or not search area in view on map condition
     * @see SearchAreaPopup
     */
    private boolean shownToMap;
    private GSearchArea gSearchArea;
    private TreeNode treeNode;
    
    public SearchAreaNode(){
        
    }

    public SearchAreaNode(SearchArea searchArea, TreeNode defaultTreeModel) {
        super(searchArea, defaultTreeModel);
        //setUserObject(searchArea.getSearchAreaID() + " - "+ searchArea.getName());
        //setImageIcon(NavigatorIconCollection.SEARCH_AREA_ICON_32);
        this.shownToMap= false;
    }
   
    @Override
    public void addSearchPatternNode(SearchPatternNode searchPatternNode){
        //getDefaultTreeModel().insertNodeInto(searchPatternNode, this, getChildCount());
    }
    
    public void addTaskSearchAreaNode(TaskSearchAreaNode taskSearchAreaNode){
        //getDefaultTreeModel().insertNodeInto(taskSearchAreaNode, this, getChildCount());
        this.taskSearchAreaNodes.add(taskSearchAreaNode);
    }
    
    public void remove(){
        getDefaultTreeModel().clearParent();
        removeFromRoutingControllerCollection();
        //Main.mapForm.exec(this.getGSearchArea().removeScript());
        this.getGSearchArea().removeScript();
        this.setShownToMap(false);
    }
    
    
    protected abstract void removeFromRoutingControllerCollection();

    public List<TaskSearchAreaNode> getTaskSearchAreaNodes() {
        return taskSearchAreaNodes;
    }

    public void setTaskSearchAreaNodes(List<TaskSearchAreaNode> taskSearchAreaNodes) {
        this.taskSearchAreaNodes = taskSearchAreaNodes;
    }
    
    /**
     * this method will return object GSearchArea from SearchArea whose belong to this object(SearchAreaNode)
     * the process are validating whether the return value is already exist or not if not return value will be create. 
     *
     * @return GSearchArea
     * @see SearchAreaPopup
     */
    
    public GSearchArea getGSearchArea(){
        if(gSearchArea == null){
            return gSearchArea = GSearchArea.searchAreaToGSearchArea(this.getObject());
        }else{
            return gSearchArea; 
        }
    }

    public boolean isShownToMap() {
        return shownToMap;
    }

    public void setShownToMap(boolean shownToMap) {
        this.shownToMap = shownToMap;
    }
}
