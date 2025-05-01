package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.response.ResponseWrapper;

import java.util.List;

public interface CallsignService {
    ResponseWrapper<Callsign> addOrUpdateCallsign(Callsign callsign, boolean isUpdate);

    ResponseWrapper<Callsign> deleteCallsign(Long id);

    ResponseWrapper<List<Callsign>> getAllCallsigns();

    ResponseWrapper<Callsign> getCallsignById(Long id);

    ResponseWrapper<UploadResult> uploadCallsignsFromExcel(String excelFile);


}
