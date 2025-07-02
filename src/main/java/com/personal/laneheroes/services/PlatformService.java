package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Platform;
import com.personal.laneheroes.response.ResponseWrapper;

import java.io.IOException;
import java.util.List;

public interface PlatformService {
    ResponseWrapper<Platform> addPlatform(Platform platform);

    ResponseWrapper<Platform> updatePlatform(Platform platform);

    ResponseWrapper<Platform> deletePlatform(Long id);

    ResponseWrapper<List<Platform>> getAllPlatforms();

    ResponseWrapper<Platform> getPlatformById(Long id);

    ResponseWrapper<UploadResult> uploadPlatformsFromExcel(String excelFile);

    void uploadInitPlatformsFromJSON(String path) throws IOException;

}
