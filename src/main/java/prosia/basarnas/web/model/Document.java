/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ismail
 */
@Getter
@Setter
public class Document implements Serializable, Comparable<Document> {

    private Integer id;
    private String name;
    private Boolean selected;
    private Object objClass;
    private String type;
    private String color;

    public Document(String name, Object objClass, String type) {
        this.name = name;
        this.objClass = objClass;
        this.type = type;
    }

    public Document(Integer id, String name, Object objClass, String type, String color) {
        this.id = id;
        this.name = name;
        this.objClass = objClass;
        this.type = type;
        this.color = color;
    }

    public Document(String name, Boolean selected, Object objClass, String type) {
        this.name = name;
        this.selected = selected;
        this.objClass = objClass;
        this.type = type;
    }

    //Eclipse Generated hashCode and equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((selected == null) ? 0 : selected.hashCode());
        result = prime * result + ((objClass == null) ? 0 : objClass.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Document other = (Document) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (objClass == null) {
            if (other.objClass != null) {
                return false;
            }
        } else if (!objClass.equals(other.objClass)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (selected == null) {
            if (other.selected != null) {
                return false;
            }
        } else if (!selected.equals(other.selected)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (color == null) {
            if (other.color != null) {
                return false;
            }
        } else if (!color.equals(other.color)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Document document) {
        return this.getName().compareTo(document.getName());
    }

}
