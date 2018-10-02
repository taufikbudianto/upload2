/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prosia.basarnas.model.MstKantorSar;
import prosia.basarnas.model.MstKantorSarImages;
import prosia.basarnas.repo.MstKantorSarImagesRepo;
import prosia.basarnas.repo.MstKantorSarRepo;

/**
 *
 * @author PROSIA
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class OfficeSarService {

    @Autowired
    private MstKantorSarRepo kantorSarRepo;
    
    @Autowired
    private MstKantorSarImagesRepo mstKanSarImagesRepo;
    
    public List<MstKantorSar> getOfficeSar(){
        return kantorSarRepo.findAll();
    }
    
    public List<MstKantorSarImages> getImagesKanSar(){
        return mstKanSarImagesRepo.findAll();
    }

}
