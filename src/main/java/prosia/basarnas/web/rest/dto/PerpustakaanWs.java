/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Taufik
 */
@Getter
@Setter
@ToString
public class PerpustakaanWs implements Serializable {

    
    @JsonProperty(value = "lib_id")
    private Integer libId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "created_by")
    private String createdBy;
    @JsonProperty(value = "created_date")
    private String createdDate;
    @JsonProperty(value = "lib_attachment")
    private String libAttachment;
    @JsonProperty(value = "book_content")
    private String bookContent;
    private String url_image_lib;
    private String downloadfile;

}
