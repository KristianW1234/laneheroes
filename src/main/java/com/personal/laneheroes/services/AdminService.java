package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.CountDTO;

public interface AdminService {
    public String uploadAllData(String companyPath, String callsignPath, String platformPath, String gamePath, String heroPath);
    public CountDTO getAllCounts();
}
