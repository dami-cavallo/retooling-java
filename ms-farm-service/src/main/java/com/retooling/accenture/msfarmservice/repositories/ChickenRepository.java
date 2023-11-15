package com.retooling.accenture.msfarmservice.repositories;

import com.retooling.accenture.msfarmservice.model.Chicken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChickenRepository extends JpaRepository<Chicken,Integer> {

    List<Chicken> findByFarmId(int id);

    @Query(value = "select * from chickens c " +
            "where c.fk_farm = :granjaOrigen " +
            "order by c.remaining_days_to_die ASC " +
            "LIMIT :cant", nativeQuery = true)
    List<Chicken> findByFarmIdCantChickens(@Param("cant") int cant, @Param("granjaOrigen") int granjaOrigen);

    @Query(value = "select * from chickens c " +
            "where c.fk_farm = :farmId " +
            "order by c.remaining_days_to_die DESC " +
            "LIMIT :cant", nativeQuery = true)
    List<Chicken> findCantChickensFromUser(@Param("cant") int cant, @Param("farmId") int farmId);




}
