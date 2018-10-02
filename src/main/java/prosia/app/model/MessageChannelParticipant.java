/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
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
@Table(name = "psa_msg_channel_participant")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"messageChannelParticipantPK"})
@ToString(exclude = {"messageChannelParticipantPK"})
@NoArgsConstructor
public class MessageChannelParticipant implements Serializable {
    
    public enum ParticipantRole {
        CONSUMER,
        COLLABORATOR,
        MANAGER
    }
    
    @EmbeddedId
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MessageChannelParticipantPK messageChannelParticipantPK;
    
    @Column(name = "participant_role", length = 10)
    @Enumerated(EnumType.STRING)
    private ParticipantRole participantRole;
    
    @JoinColumn(name = "channel_id", referencedColumnName = "channel_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MessageChannel channel;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public MessageChannelParticipant(MessageChannel channel, User user) {
        this.messageChannelParticipantPK = new MessageChannelParticipantPK(channel.getChannelId(), user.getUserId());
        this.channel = channel;
        this.user = user;
    }

    public MessageChannelParticipant(ParticipantRole participantRole, MessageChannel channel, User user) {
        this.messageChannelParticipantPK = new MessageChannelParticipantPK(channel.getChannelId(), user.getUserId());
        this.participantRole = participantRole;
        this.channel = channel;
        this.user = user;
    }
    
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
class MessageChannelParticipantPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "channel_id")
    private int channelId;
    
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;

    public MessageChannelParticipantPK() {
    }

    public MessageChannelParticipantPK(int channelId, int userId) {
        this.channelId = channelId;
        this.userId = userId;
    }
    
}
