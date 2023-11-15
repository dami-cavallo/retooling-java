package com.retooling.accenture.msfarmservice.repositories;

import com.retooling.accenture.msfarmservice.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Integer> {
    Farm findById(int id);
    List<Farm> findByFarmerUserId(int id);

    @Query(value = "select * from farms f " +
            "where f.id = :idGranjaDestino " +
            "AND f.capacidad_disponible >= :cantidad " +
            "AND f.money >= :money ", nativeQuery = true)
    Farm findFarmDestinoVenta(@Param("cantidad") int cantidad, @Param("money") double money, @Param("idGranjaDestino") int idGranjaDestino);


}
