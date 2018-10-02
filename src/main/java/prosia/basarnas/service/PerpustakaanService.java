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
import prosia.basarnas.model.CPPerpusPersonil;
import prosia.basarnas.model.CPPerpustakaan;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.PerpustakaanPersonilRepo;
import prosia.basarnas.repo.PerpustakkanRepo;

/**
 *
 * @author Irfan Rofie
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class PerpustakaanService {

    @Autowired
    private PerpustakkanRepo perpusRepo;
    @Autowired
    private PerpustakaanPersonilRepo perpusPersonilRepo;

    public void savePerpustakaan(CPPerpustakaan perpus, List<ResPersonnel> list) {
        for (ResPersonnel person : list) {
            if (perpus.getLibId() != null) {
                perpusPersonilRepo.deleteByPerpustakaanAndPersonil(perpus, person);
            }
            CPPerpusPersonil cppp = new CPPerpusPersonil();
            cppp.setPerpustakaan(perpus);
            cppp.setPersonil(person);
            perpusPersonilRepo.save(cppp);
        }
        perpus.setDeleted(false);
        perpusRepo.save(perpus);
    }
}
