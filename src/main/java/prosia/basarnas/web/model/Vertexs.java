/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Ismail
 */
@Data
public class Vertexs implements Serializable {

    private String sequence;
    private String latitude;
    private String longitude;
}
