package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.model.Farm;
import com.retooling.accenture.msfarmservice.repositories.FarmRepository;
import com.retooling.accenture.msfarmservice.repositories.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("FarmService")
public class FarmServiceImpl implements FarmService{
    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    public Farm findById(int farmId) {
        return farmRepository.findById(farmId);
    }

    public List<Farm> findByFarmerUserId(int farmerId) {
        return farmRepository.findByFarmerUserId(farmerId);
    }

    public Farm createFarm(Farm farm, int farmerId){

        farm.setFarmer(farmerRepository.findByUserId(farmerId));
        farm.setChickens(new ArrayList<>());
        farm.setEggs(new ArrayList<>());
        farm.setCantChickens(0);
        farm.setCantEggs(0);
        farm.setCapacidadDisponible();
        farmRepository.save(farm);

        return farm;

    }



}
