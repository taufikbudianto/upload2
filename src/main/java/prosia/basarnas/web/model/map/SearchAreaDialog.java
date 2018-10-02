/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.model.map;

import java.io.Serializable;
import lombok.Data;
import prosia.basarnas.constant.StatusConstant;

/**
 *
 * @author Ismail
 */
@Data
public class SearchAreaDialog implements Serializable {

    private String shape = StatusConstant.SEARCH_AREA_SQUARE_SHAPE;
    private String satuan;
    private Double searchNilai;
    private Double searchHeading;

}
