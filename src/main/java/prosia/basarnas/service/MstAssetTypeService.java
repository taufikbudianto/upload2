/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.model.MstAssetType;
import prosia.basarnas.repo.MstAssetTypeRepo;

/**
 *
 * @author PROSIA
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class MstAssetTypeService {
    @Autowired
    private MstAssetTypeRepo mstAssetTypeRepo;
    
    public List<MstAssetType> getAssetName(){
        return mstAssetTypeRepo.findByIsdeletedOrderByAssetname(BigInteger.valueOf(0));
    }
}
