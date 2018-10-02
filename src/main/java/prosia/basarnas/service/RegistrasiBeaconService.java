/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.enumeration.BeaconType;
import prosia.basarnas.model.BeaconModel;
import prosia.basarnas.model.Manufacturer;
import prosia.basarnas.model.UtiAirvessel;
import prosia.basarnas.repo.MstBeaconModelRepo;
import prosia.basarnas.repo.MstManufacturerRepo;
import prosia.basarnas.repo.RegistrasiBeaconRepo;

/**
 *
 * @author PROSIA
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class RegistrasiBeaconService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RegistrasiBeaconRepo beaconRepo;
    @Autowired
    private MstManufacturerRepo manufacturerRepo;

    public void saveRegistrasiBeacon(UtiAirvessel airvessel, String manufacturerId, String beaconModelId, String[] radio, String[] toolsCom) {
    }
}
