/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public class PersonelWs implements Serializable {

    @JsonProperty(value = "personel_id")
    private String personelId;
    @JsonProperty(value = "nrp")
    private String personelNip;
    @JsonProperty(value = "name")
    private String personelName;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "username")
    private String username;
//    @JsonProperty(value = "password")
//    private String password;
    @JsonProperty(value = "address")
    private String alamat;
    @JsonProperty(value = "identity_number")
    private String noKtp;
    @JsonProperty(value = "telephone_1")
    private String noTelp1;
    @JsonProperty(value = "telephone_2")
    private String notelp2;
    @JsonProperty(value = "birthdate")
    //@Temporal(TemporalType.TIMESTAMP)
    private String tanggalLahir;
//    @JsonProperty(value = "tempat_lahir")
//    private String tempatLahir;
    @JsonProperty(value = "sar_office")
    private String kantorsar;
    @JsonProperty(value = "status")
    private Integer status;
    @JsonProperty(value = "blood_type")
    private String golDarah;
    @JsonProperty(value = "information")
    private String keterangan;
    @JsonProperty(value = "religion")
    private String agama;
//    @JsonProperty(value = "mulai_masuk")
//    private Date mulaiMasuk;
//    @JsonProperty(value = "tanggal_valid")
//    private Date dValid;

    @JsonProperty(value = "division")
    private String divisi;
    @JsonProperty(value = "position")
    private String jabatan;
    @JsonProperty(value = "class")
    private String golongan;
    @JsonProperty(value = "gender")
    private String gender;
    @JsonProperty(value = "passport_number")
    private String noPaspor;
    @JsonProperty(value = "image")
    private String picture;

}
