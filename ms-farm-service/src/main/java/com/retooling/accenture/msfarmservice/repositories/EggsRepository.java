package com.retooling.accenture.msfarmservice.repositories;


import com.retooling.accenture.msfarmservice.model.Egg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EggsRepository extends JpaRepository<Egg,Integer> {

    @Query(value = "select * from eggs e " +
            "inner join farms f on e.fk_farm = f.id " +
            "inner join users u on f.fk_farmer = u.id " +
            "inner join roles r on u.fk_role_id = r.role_id " +
            "where r.role_name IN ('ADMIN') order by e.days_to_become_chicken DESC " +
            "LIMIT :cant", nativeQuery = true)
    List<Egg> findCantEggsFromAdmin(@Param("cant") int cant);

    @Query(value = "select * from eggs e " +
            "where e.fk_farm = :farmId " +
            "order by e.days_to_become_chicken ASC " +
            "LIMIT :cant", nativeQuery = true)
    List<Egg> findCantChickensFromUser(@Param("cant") int cant, @Param("farmId") int farmId);


}
