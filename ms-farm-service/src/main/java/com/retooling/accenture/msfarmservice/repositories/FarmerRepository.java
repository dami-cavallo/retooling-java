package com.retooling.accenture.msfarmservice.repositories;

import com.retooling.accenture.msfarmservice.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FarmerRepository extends JpaRepository<Farmer,Integer> {
    Farmer findByUserId(int id);


}
