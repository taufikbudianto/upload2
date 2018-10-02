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
import prosia.basarnas.model.CPPerpustakaan;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface PerpustakkanRepo extends JpaRepository<CPPerpustakaan, Integer>,
        JpaSpecificationExecutor<CPPerpustakaan> {

    @Query(value = "select * from CP_PERPUSTAKAAN PERPUS where PERPUS.LIB_ID in :list",
            nativeQuery = true)
    public List<CPPerpustakaan> getListByLibId(@Param("list") List<Integer>data);
    public CPPerpustakaan findByLibId (Integer ID);
}
