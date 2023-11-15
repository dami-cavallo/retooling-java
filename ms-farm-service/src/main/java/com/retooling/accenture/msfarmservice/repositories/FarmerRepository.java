package com.retooling.accenture.msfarmservice.repositories;

import com.retooling.accenture.msfarmservice.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FarmerRepository extends JpaRepository<Farmer,Integer> {
    Farmer findByUserId(int id);

    @Query(value = "select r.role_name from farmers f " +
            "inner join users u on f.user_id = u.id " +
            "inner join roles r on u.fk_role_id = r.role_id " +
            "where f.user_id = :farmerId" , nativeQuery = true)
    String findRoleByFarmerId(@Param("farmerId") int farmerId);

}
