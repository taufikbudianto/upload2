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
import prosia.basarnas.model.CPNotifPersonil;
import prosia.basarnas.model.CPNotifikasi;
import prosia.basarnas.model.ResPersonnel;
import prosia.basarnas.repo.NotifikasiPersonilRepo;
import prosia.basarnas.repo.NotifikasiRepo;

/**
 *
 * @author Irfan Rofie
 */
@Service
@Transactional(readOnly = false, rollbackFor = {Exception.class})
public class NotifikasiService {

    @Autowired
    private NotifikasiRepo notifikasiRepo;
    @Autowired
    private NotifikasiPersonilRepo notifPersonilRepo;

    public void saveNotifikasi(CPNotifikasi notif, List<ResPersonnel> list) {
        list.stream().map((person) -> {
            CPNotifPersonil cpnp = new CPNotifPersonil();
            cpnp.setNotifikasi(notif);
            cpnp.setPersonnel(person);
            return cpnp;
        }).forEachOrdered((cpnp) -> {
            notifPersonilRepo.save(cpnp);
        });
        notifikasiRepo.save(notif);
    }
}
