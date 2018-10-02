/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.basarnas.repo;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.basarnas.model.Layer;

/**
 *
 * @author Irfan Rofie
 */
@Repository
public interface LayerRepo extends JpaRepository<Layer, String>,
        JpaSpecificationExecutor<Layer> {

    public List<Layer> findByStatusFalse();

    public List<Layer> findByLayerIdContaining(String layerId);

    @Query("SELECT MAX(layerId) FROM Layer WHERE layerId LIKE %?1%")
    public String findByMaxId(String id);
}
