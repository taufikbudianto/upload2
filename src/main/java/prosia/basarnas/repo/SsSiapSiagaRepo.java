/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Ss_SiapSiaga;

/**
 *
 * @author Riki
 */
@Repository
public interface SsSiapSiagaRepo extends JpaRepository<Ss_SiapSiaga, String>, JpaSpecificationExecutor<Ss_SiapSiaga> {
    @Query("SELECT a FROM Ss_SiapSiaga a where a.kode=?1")
    public List<Ss_SiapSiaga> findKodeByKode(String kode);
    
//    @Query("UPDATE Ss_SiapSiaga SET a.deleted = ?1, a.nama=?1, a.barang=?1, a.usiaHelikopter=?1, a.sisaJamTerbang=?1, a.piyad=?1, a.jumlah=?1, a.posisi=?1, a.servoeableCondition=?1, a.unserviceableCondition=?1, a.crew=?1, a.keterangan=?1 a.tglEntry=?1 WHERE a.kode = ?1")
//    public String findUpdateSSsiaga(String kode);
}
