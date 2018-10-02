/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_message")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"messageId"})
@ToString
@NoArgsConstructor
public class Message extends AbstractAuditingEntity implements Serializable {
    
    public enum MessageType {
        DOCUMENT,
        IMAGE,
        LOCATION,
        TEXT
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "message_id")
    private Integer messageId;
    
    @JoinColumn(name = "channel_id", referencedColumnName = "channel_id")
    @ManyToOne(optional = false)
    private MessageChannel channel;
    
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @ManyToOne
    private User author;
    
    @Column(name = "message_type", length = 10)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    
    @Lob
    @Column(name = "text_payload")
    private String textPayload;
    
    @Column(name = "binary_payload")
    private byte[] binaryPayload;
    
    @Column(name = "mark_as_read")
    private Boolean markAsRead;

    public Message(MessageChannel channel, User author, MessageType messageType) {
        this.channel = channel;
        this.author = author;
        this.messageType = messageType;
    }
    
}
