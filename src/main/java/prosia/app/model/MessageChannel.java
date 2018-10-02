/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "psa_app_message_channel")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"channelId"})
@ToString(exclude = {"participants"})
@NoArgsConstructor
public class MessageChannel extends AbstractAuditingEntity implements Serializable {
    
    public enum ChannelType {
        GROUP_CHAT,
        USER_CHAT,
        USER_NOTIFICATION
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "channel_id")
    private Integer channelId;
    
    @Column(name = "channel_name", length = 50)
    private String channelName;
    
    @Column(name = "channel_type", length = 50)
    @Enumerated(EnumType.STRING)
    private ChannelType channelType;
    
    @Column(name = "archived")
    private Boolean archived;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<MessageChannelParticipant> participants;

    public MessageChannel(String channelName, ChannelType channelType, Boolean archived) {
        this.channelName = channelName;
        this.channelType = channelType;
        this.archived = archived;
    }
    
    /**
     * Add an user as the participant in this channel. As a note, after call this method, 
     * save method must be called from repository class to commit the new participant to database.
     * @param user
     * @param participantRole
     * @throws Exception 
     */
    public void addParticipant(User user, MessageChannelParticipant.ParticipantRole participantRole) 
            throws Exception {
        
        MessageChannelParticipant participant = new MessageChannelParticipant(participantRole, this, user);
        
        if (participants == null) {
            participants = new ArrayList<>();
        } else if (participants.contains(participant)) {
            throw new Exception("Participant is already exists in this Channel.");
        }
        
        participants.add(participant);
    }
    
    /**
     * Remove an user from this participants. As a note, after call this method, 
     * save method must be called from repository class to remove the participant from database.
     * @param user
     * @throws Exception 
     */
    public void removeParticipant(User user) throws Exception {
        if (participants == null) {
            throw new Exception("Participant is empty.");
        }
        
        MessageChannelParticipant participant = new MessageChannelParticipant(this, user);
        
        boolean result = participants.remove(participant);
        
        if (!result) {
            throw new Exception("Participant is not exists in this Channel.");
        }
    }

    public List<MessageChannelParticipant> getParticipants() {
        return Collections.unmodifiableList(participants != null ? participants : new ArrayList<>());
    }
    
}
