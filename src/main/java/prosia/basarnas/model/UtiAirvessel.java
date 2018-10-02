/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author PROSIA
 */
@Entity
@Table(name = "UTI_AIRVESSEL")
public @Data
class UtiAirvessel extends AbstractAuditingEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "BEACONID")
    private String beaconId;
    @Column(name = "BEACONTYPE")
    private String beaconType;
    @Column(name = "REGISTRATIONDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    @Column(name = "REGISTRATIONTYPE")
    private Integer registrationType;
    @Column(name = "OLDBEACONID")
    private String oldBeaconId;
    @ManyToOne
    @JoinColumn(name = "MANUFACTURER")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private Manufacturer manufacturer;
    @ManyToOne
    @JoinColumn(name = "BEACONMODEL")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 25)
    private BeaconModel beaconModel;
    @Column(name = "BATTERYEXPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date batteryExpDate;
    @Column(name = "APPROVALNO")
    private String approvalNo;
    @Column(name = "OWNERNAME")
    private String ownerName;
    //@Lob
    @Column(name = "OWNERADDRESS", length = 100000)
    private String ownerAddress;
    @Column(name = "OWNEREMAIL")
    private String ownerEmail;
    @Column(name = "OWNERPHONE")
    private String ownerPhone;
    @Column(name = "OWNERCELLPHONE")
    private String ownerCellPhone;
    @Column(name = "OWNERCOMPANYNAME")
    private String ownerCompanyName;
    @Column(name = "OWNERCOMPANYADDRESS")
    private String ownerCompanyAddress;
    @Column(name = "OWNERCOMPANYEMAIL")
    private String ownerCompanyEmail;
    @Column(name = "OWNERCOMPANYPHONE")
    private String ownerCompanyPhone;
    @Column(name = "OWNECOMPANYFAX")
    private String owneCompanyFax;
    @Column(name = "REGISTRATIONNO")
    private String registrationNo;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CATEGORY")
    private Integer category;
    @Column(name = "SAILTYPE")
    private Integer sailType;
    @Column(name = "OTHERSAILTYPE")
    private String otherSailType;
    @Column(name = "MACHINETYPE")
    private Integer machineType;
    @Column(name = "OTHERMACHINETYPE")
    private String otherMachineType;
    @Column(name = "CALLSIGN")
    private String callSign;
    @Column(name = "MADEIN")
    private String madeIn;
    @Column(name = "MODEL")
    private String model;
    @Column(name = "ALENGTH")
    private String alength;
    @Column(name = "A_WIDTH")
    private String aWidth;
    @Column(name = "A_WEIGHT")
    private String aWeight;
    @Column(name = "COLOR")
    private String color;
    @Column(name = "PASSENGER")
    private Integer passenger;
    @Column(name = "COMEQUIPMENT")
    private String comequipment;
    @Column(name = "OTHERCOMEQUIPMENT")
    private String otherComequipment;
    @Column(name = "HOMEBASE")
    private String homebase;
    //@Lob
    @Column(name = "ADDITIONALDETAIL", length = 65535)
    private String additionalDetail;
    @Column(name = "INMARSATVOICE")
    private String inmarsatVoice;
    @Column(name = "INMARSATFAX")
    private String inmarsatFax;
    @Column(name = "INMARSATTELEX")
    private String inmarsatTelex;
    @Column(name = "OTHERTELPNO")
    private String otherTelpNo;
    @Column(name = "MMSI")
    private String mmsi;
    @Column(name = "A_USAGE")
    private Integer aUsage;
    @Column(name = "DETAILUSAGE")
    private Integer detailUsage;
    @Column(name = "OTHERDETAILUSAGE")
    private String otherDetailUsage;
    @Column(name = "USAGETYPE")
    private Integer usageType;
    @Column(name = "OTHERUSAGETYPE")
    private String otherUsageType;
    @Lob
    @Column(name = "ADDITIONALUSAGE", length = 65535)
    private String additionalUsage;
    @Column(name = "URGENTCONTACTNAME1")
    private String urgentContactName1;
    @Column(name = "URGENTCONTACTHOMEPHONE1")
    private String urgentContactHomePhone1;
    @Column(name = "URGENTCONTACTOFFICEPHONE1")
    private String urgentContactOfficePhone1;
    @Column(name = "URGENTCONTACTCELLPHONE1")
    private String urgentContactCellPhone1;
    @Column(name = "URGENTCONTACTNAME2")
    private String urgentContactName2;
    @Column(name = "URGENTCONTACTHOMEPHONE2")
    private String urgentContactHomePhone2;
    @Column(name = "URGENTCONTACTOFFICEPHONE2")
    private String urgentContactOfficePhone2;
    @Column(name = "URGENTCONTACTCELLPHONE2")
    private String urgentContactCellPhone2;
    @Column(name = "CREATEBY")
    private String createBy;
    @Column(name = "DATECREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "MODIFIEDBY")
    private String modifiedBy;
    @Column(name = "LASTMODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "USERSITEID")
    private String usersiteId;
    @Column(name = "A_LENGTH")
    private String aLength;
    @Column(name = "CREATEDBY")
    private String createdBy;
    @Column(name = "OWNERCOMPANYFAX")
    private String ownerCompanyFax;
    @Column(name = "ISDELETED")
    private Boolean isDeleted;
    @Column(name = "OWNERFAX")
    private String ownerFax;
    @Column(name = "APPROVED")
    private Boolean approved;
    @Column(name = "OTHERMANUFACTURER")
    private String otherManufacturer;
    @Column(name = "OTHERBEACONMODEL")
    private String otherBeaconModel;
    @Column(name = "BEACONREGNO")
    private String beaconRegNo;
}
