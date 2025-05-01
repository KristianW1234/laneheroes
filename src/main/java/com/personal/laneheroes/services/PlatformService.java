package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Platform;
import com.personal.laneheroes.response.ResponseWrapper;

import java.util.List;

public interface PlatformService {
    ResponseWrapper<Platform> addOrUpdatePlatform(Platform callsign, boolean isUpdate);

    ResponseWrapper<Platform> deletePlatform(Long id);

    ResponseWrapper<List<Platform>> getAllPlatforms();

    ResponseWrapper<Platform> getPlatformById(Long id);

    ResponseWrapper<UploadResult> uploadPlatformsFromExcel(String excelFile);

}
