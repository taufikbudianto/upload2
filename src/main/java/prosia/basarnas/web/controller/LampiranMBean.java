/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Data;
import prosia.basarnas.web.model.LampiranEntity;


/**
 *
 * @author Ismail
 */
@ManagedBean
@ViewScoped
public @Data class LampiranMBean implements Serializable {

    private List<LampiranEntity> lampiranEntitys;

    public LampiranMBean() {
        lampiranEntitys = new ArrayList<>();
        setPob();
    }

    private void setPob() {
        LampiranEntity le = new LampiranEntity();
        le.setDeskripsi("Laporan Perencanaan Rescue 009");
        le.setJenisFile("Doc");
        le.setDtUpload(new Date());
        le.setUploadBy("Aryyo");
        lampiranEntitys.add(le);
        le = new LampiranEntity();
        le.setDeskripsi("Foto lokasi");
        le.setJenisFile("Zip");
        le.setDtUpload(new Date());
        le.setUploadBy("Aryyo");
        lampiranEntitys.add(le);
        le = new LampiranEntity();
        le.setDeskripsi("Denah Lokasi");
        le.setJenisFile("JPG");
        le.setDtUpload(new Date());
        le.setUploadBy("Aryyo");
        lampiranEntitys.add(le);
    }
}
