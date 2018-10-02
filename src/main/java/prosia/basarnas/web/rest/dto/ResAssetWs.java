/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Taufik
 */
@Data
public class ResAssetWs implements Serializable {
   
    @JsonProperty(value = "asset_name")
    private String assetName;
    @JsonProperty(value = "sar_office")
    private String sarOffice;
    @JsonProperty(value = "latitude")
    private String latitude;
    @JsonProperty(value = "longitude")
    private String longitude;
    @JsonProperty(value = "picture_list")
    private List<String> picture;
    @JsonProperty(value = "contact_list")
    private List data;
}
