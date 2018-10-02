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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.CPPerpusPersonil;
import prosia.basarnas.model.CPPerpustakaan;
import prosia.basarnas.model.ResPersonnel;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface PerpustakaanPersonilRepo extends JpaRepository<CPPerpusPersonil, Integer>,
        JpaSpecificationExecutor<CPPerpusPersonil> {

    public List<CPPerpusPersonil> findByPerpustakaan(CPPerpustakaan perpus);
    
    @Query(value="select PERPUS.LIB_ID from CP_PERPUS_PERSONIL PERPUS where PERPUS.PERSONEL_ID=:id",
            nativeQuery = true)
    public List<Integer> getLibId(@Param("id")String PersonelId);

    public void deleteByPerpustakaanAndPersonil(CPPerpustakaan perpus, ResPersonnel personil);
}
