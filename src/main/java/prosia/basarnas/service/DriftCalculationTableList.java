/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author Aris
 */

public @Data class DriftCalculationTableList {
    private String[] trapeziumareaid = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] deskripsi = {"","","","","","","","","","","","","","","","","","","",""};
    private Date[] waktuoperasi = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    private String[] waktuoperasitimezone = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] latlkp = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] longlkp = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] latdest = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] longdest = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] safetyfactor = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] distresserror	= {"","","","","","","","","","","","","","","","","","","",""};
    private String[] searcherror = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] luasarea = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] incidentid = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] datecreated = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] createdby = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] lastmodified = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] modifiedby = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] isdeleted = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] usersiteid = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] worksheetname	= {"","","","","","","","","","","","","","","","","","","",""};
    private String[] taskarealastpoint = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] totaltaskarealength = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] unit = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] waypoint = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] parentid = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] heading = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] width = {"","","","","","","","","","","","","","","","","","","",""};
    private String[] luasAreaTrapezium = {"","","","","","","","","","","","","","","","","","","",""};
    private int size;
    private int actualSize;
    public DriftCalculationTableList(){
        
    }
    
    public void setTabelList(String trapeziumareaid, String deskripsi, Date waktuoperasi, String latlkp, String longlkp,	
            String latdest, String longdest, String safetyfactor,	
            String distresserror, String searcherror, String luasarea,	
            String incidentid, String datecreated, String createdby,	
            String isdeleted, String usersiteid, String worksheetname, String taskarealastpoint,	
            String totaltaskarealength, String unit, String waypoint, String parentid){

        this.trapeziumareaid[size] = trapeziumareaid;
        this.deskripsi[size] = deskripsi; 
        this.waktuoperasi[size] = waktuoperasi;
//        this.waktuoperasitimezone.add(waktuoperasitimezone);
        this.latlkp[size] = latlkp;
        this.longlkp[size] = longlkp;
        this.latdest[size] = latdest;
        this.longdest[size] = longdest;
        this.safetyfactor[size] = safetyfactor;
        this.distresserror[size] = distresserror;
        this.searcherror[size] = searcherror;
        this.luasarea[size] = luasarea;
        this.incidentid[size] = incidentid;
        this.datecreated[size] = datecreated;
        this.createdby[size] = createdby;
//        this.lastmodified.add(lastmodified);
//        this.modifiedby.add(modifiedby);
        this.isdeleted[size] = isdeleted;
        this.usersiteid[size] = usersiteid;
        this.worksheetname[size] = worksheetname;
        this.taskarealastpoint[size] = taskarealastpoint;
        this.totaltaskarealength[size] = totaltaskarealength;
        this.unit[size] = unit;
        this.waypoint[size] = waypoint;
        this.parentid[size] = parentid;
//        size++;
//        actualSize = size;
    }
    
    public void UpdateTabelList(String trapeziumareaid, String deskripsi, Date waktuoperasi, String latlkp, String longlkp,	
            String latdest, String longdest, String safetyfactor,	
            String distresserror, String searcherror, String luasarea,	
            String incidentid, String datecreated, String createdby,	
            String isdeleted, String usersiteid, String worksheetname, String taskarealastpoint,	
            String totaltaskarealength, String unit, int index){
        
        this.trapeziumareaid[size] = trapeziumareaid;
        this.deskripsi[size] = deskripsi; 
        this.waktuoperasi[size] = waktuoperasi;
//        this.waktuoperasitimezone.add(waktuoperasitimezone);
        this.latlkp[size] = latlkp;
        this.longlkp[size] = longlkp;
        this.latdest[size] = latdest;
        this.longdest[size] = longdest;
        this.safetyfactor[size] = safetyfactor;
        this.distresserror[size] = distresserror;
        this.searcherror[size] = searcherror;
        this.luasarea[size] = luasarea;
        this.incidentid[size] = incidentid;
        this.datecreated[size] = datecreated;
        this.createdby[size] = createdby;
//        this.lastmodified.add(lastmodified);
//        this.modifiedby.add(modifiedby);
        this.isdeleted[size] = isdeleted;
        this.usersiteid[size] = usersiteid;
        this.worksheetname[size] = worksheetname;
        this.taskarealastpoint[size] = taskarealastpoint;
        this.totaltaskarealength[size] = totaltaskarealength;
        this.unit[size] = unit;
//        this.waypoint.set(index,waypoint);
//        this.parentid.set(index,parentid);
//        size++;
    }
    
    public void getCalculateTableList(String heading, String width, String luasArea){
        this.heading[size] = heading;
        this.width[size] = width;
        this.luasAreaTrapezium[size] = luasArea;
    }
    
    public void updateCalculateTableList(int index, String heading, String width, String luasArea){
        this.heading[index] = heading;
        this.width[index] = width;
        this.luasAreaTrapezium[index] = luasArea;
    }
    
     public void resetSize(){
        size = 0;
    }
}
