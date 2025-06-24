package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.response.ResponseWrapper;

import java.io.IOException;
import java.util.List;

public interface CallsignService {
    ResponseWrapper<Callsign> addCallsign(Callsign callsign);

    ResponseWrapper<Callsign> updateCallsign(Callsign callsign);

    ResponseWrapper<Callsign> deleteCallsign(Long id);

    ResponseWrapper<List<Callsign>> getAllCallsigns();

    ResponseWrapper<Callsign> getCallsignById(Long id);

    ResponseWrapper<UploadResult> uploadCallsignsFromExcel(String excelFile);

    void uploadInitCallsignsFromJSON() throws IOException;
}
