package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.model.Farm;

import java.util.List;

public interface FarmService {
    Farm findById(int id);
    Farm createFarm(Farm farm, int farmerId);
    List<Farm> findByFarmerUserId(int farmerId);
}
