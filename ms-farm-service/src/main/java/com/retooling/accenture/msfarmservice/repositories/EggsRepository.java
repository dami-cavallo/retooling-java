package com.retooling.accenture.msfarmservice.repositories;


import com.retooling.accenture.msfarmservice.model.Egg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EggsRepository extends JpaRepository<Egg,Integer> {

    @Query(value = "select * from eggs e " +
            "where e.fk_farm = :granjaOrigen " +
            "order by e.days_to_become_chicken DESC " +
            "LIMIT :cant", nativeQuery = true)
    List<Egg> findCantEggsFromGranjaOrigen(@Param("cant") int cant, @Param("granjaOrigen") int granjaOrigen);

    @Query(value = "select * from eggs e " +
            "where e.fk_farm = :farmId " +
            "order by e.days_to_become_chicken ASC " +
            "LIMIT :cant", nativeQuery = true)
    List<Egg> findCantChickensFromUser(@Param("cant") int cant, @Param("farmId") int farmId);


}
