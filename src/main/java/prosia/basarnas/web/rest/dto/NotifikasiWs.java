/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
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
public class NotifikasiWs implements Serializable {

    @JsonProperty(value = "notification_id")
    private Integer notifId;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "created_date")
    private String dateCreated;
    @JsonProperty(value = "created_by")
    private String createdBy;
    @JsonProperty(value = "flag_comment")
    private Boolean flagComment;//flag_comment
    @JsonProperty(value = "comment_list")
    private List<NotifikasiTanggapanWs> commentList;
}
