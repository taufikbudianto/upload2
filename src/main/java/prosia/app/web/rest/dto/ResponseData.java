/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author RANDY
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseData extends Response implements Serializable {
    
    @JsonProperty(value = "data")
    private List data;
    
}
