package com.personal.laneheroes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.repositories.CallsignRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CallsignService;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CallsignServiceImpl implements CallsignService {

    private final CallsignRepository callsignRepository;

    private final ObjectMapper objectMapper;


    @Override
    public ResponseWrapper<Callsign> addCallsign(Callsign callsign) {
        if (callsign.getCallsign() == null || callsign.getCallsign().isBlank()
        || callsign.getCallsignPlural() == null || callsign.getCallsignPlural().isBlank()){
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                    + ResponseMessages.ADD_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Callsign dbCalSn = new Callsign();
        dbCalSn.setCallsign(callsign.getCallsign());
        dbCalSn.setCallsignPlural(callsign.getCallsignPlural());
        callsignRepository.save(dbCalSn);
        return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                + ResponseMessages.ADD_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbCalSn);
    }

    @Override
    public ResponseWrapper<Callsign> updateCallsign(Callsign callsign) {
        if (callsign.getCallsign() == null && callsign.getCallsignPlural() == null){
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Optional<Callsign> callsignPresence = callsignRepository.findById(callsign.getId());
        if (callsignPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                + ResponseMessages.UPDATE_FAIL,
                ResponseMessages.FAIL_STATUS, null);
        }
        Callsign dbCalSn = callsignPresence.get();


        if (callsign.getCallsign() != null){
            dbCalSn.setCallsign(callsign.getCallsign());
        }
        if (callsign.getCallsignPlural() != null){
            dbCalSn.setCallsignPlural(callsign.getCallsignPlural());
        }
        callsignRepository.save(dbCalSn);
        return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                + ResponseMessages.UPDATE_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbCalSn);

    }

    @Override
    public ResponseWrapper<Callsign> deleteCallsign(Long id) {
        Optional<Callsign> callsignPresence = callsignRepository.findById(id);
        if (callsignPresence.isPresent()){
            Callsign dbCom = callsignPresence.get();
            callsignRepository.delete(dbCom);
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbCom);
        } else {
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }

    @Override
    public ResponseWrapper<List<Callsign>> getAllCallsigns() {
        List<Callsign> list = callsignRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.CALLSIGN_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.CALLSIGN_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<Callsign> getCallsignById(Long id) {
        Optional<Callsign> callsignPresence = callsignRepository.findById(id);
        return callsignPresence.map(
                        callsign -> new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE
                                + " " + ResponseMessages.FOUND,
                                ResponseMessages.SUCCESS_STATUS, callsign))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<UploadResult> uploadCallsignsFromExcel(String excelFile) {
        int totalAdded = 0;
        try (
                FileInputStream inputStream = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(inputStream);
        ){
            List<Callsign> callsigns = callsignRepository.findAll();
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip the header row
            Callsign cs;
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                cs = new Callsign();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    if (columnIndex == 0){
                        cs.setCallsign(nextCell.getStringCellValue());
                    } else if (columnIndex == 1){
                        cs.setCallsignPlural(nextCell.getStringCellValue());
                    }

                }

                if (!callsignCopyCheck(callsigns, cs.getCallsign())) {
                    totalAdded++;
                    callsignRepository.save(cs);
                }

            }

        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL , ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }
        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS , ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    @Override
    public void uploadInitCallsignsFromJSON(String path) throws IOException {
        if (callsignRepository.count() > 0) return;

        InputStream input = new FileInputStream(path);
        List<Callsign> callsigns = objectMapper.readValue(input, new TypeReference<>() {});
        callsignRepository.saveAll(callsigns);

    }

    private boolean callsignCopyCheck(List<Callsign> callsigns, String name) {
        for (Callsign callsign : callsigns) {
            if (callsign.getCallsign().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
