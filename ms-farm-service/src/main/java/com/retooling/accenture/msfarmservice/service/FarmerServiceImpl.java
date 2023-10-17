package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.model.Farmer;
import com.retooling.accenture.msfarmservice.model.UserBean;
import com.retooling.accenture.msfarmservice.repositories.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("FarmerService")
public class FarmerServiceImpl implements FarmerService {
    @Autowired
    private FarmerRepository farmerRepository;
    public Farmer findByUserId(int id){
        return farmerRepository.findByUserId(id);
    }

    public Farmer saveFarmer(UserBean user) {
        Farmer farmerSaved = new Farmer(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail());
        farmerRepository.save(farmerSaved);
        return farmerSaved;

    }
}
