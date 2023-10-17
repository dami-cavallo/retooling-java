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
            "inner join users u on f.fk_farmer = u.id " +
            "inner join roles r on u.fk_role_id = r.role_id " +
            "where r.role_name IN ('ADMIN') " +
            "AND f.capacidad_disponible >= :cantidad AND f.money >= :money " +
            "order by f.capacidad_disponible DESC " +
            "LIMIT 1", nativeQuery = true)
    Farm findFarmFromAdmin(@Param("cantidad") int cantidad, @Param("money") double money);


}
