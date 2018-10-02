/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller.map.googleapi;

import lombok.Data;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Aris
 */
public abstract class RoutingNode<A> {
    private A object;
    private TreeNode defaultTreeModel;
    
    public RoutingNode() {
    
    }
    public RoutingNode(A object, TreeNode defaultTreeModel) {
        this.object = object;
        this.defaultTreeModel = defaultTreeModel;
    }
    
    protected abstract void remove();
    
    public A getObject() {
        return object;
    }

    public void setObject(A object) {
        this.object = object;
    }

    public TreeNode getDefaultTreeModel() {
        return defaultTreeModel;
    }

    public void setDefaultTreeModel(TreeNode defaultTreeModel) {
        this.defaultTreeModel = defaultTreeModel;
    }
}
