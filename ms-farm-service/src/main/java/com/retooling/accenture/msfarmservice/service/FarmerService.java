package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.model.*;

public interface FarmerService {
    Farmer findByUserId(int id);
    Farmer saveFarmer(UserBean user);


}
