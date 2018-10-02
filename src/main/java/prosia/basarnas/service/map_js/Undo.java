/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service.map_js;

/**
 *
 * @author Aris
 */
public abstract class Undo {
    
    protected int undoState;
    
    public abstract void undo();

    
    public int getUndoState() {
        return undoState;
    }

    public void setUndoState(int undoState) {
        this.undoState = undoState;
    }
    
}
