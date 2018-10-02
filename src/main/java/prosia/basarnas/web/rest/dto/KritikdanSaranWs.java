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
 * @author PROSIA
 */
@Data
public class KritikdanSaranWs implements Serializable {
    @JsonProperty(value = "kritik_dan_saranId")
    private String kritikdansaranId;
    @JsonProperty(value = "personnelId")
    private String personnelId;
    @JsonProperty(value = "kritik_dan_saran")
    private String kritikdansaran;
}
