package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.repositories.CallsignRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CallsignService;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service("CallsignServiceImpl")
@Transactional
public class CallsignServiceImpl implements CallsignService {

    @Autowired
    CallsignRepository callsignRepository;


    @Override
    public ResponseWrapper<Callsign> addOrUpdateCallsign(Callsign callsign, boolean isUpdate) {
        Callsign dbCalSn = new Callsign();
        String successMsg = ResponseMessages.ADD_SUCCESS;
        String failMsg = ResponseMessages.ADD_FAIL;
        if (isUpdate){

            successMsg = ResponseMessages.UPDATE_SUCCESS;
            failMsg = ResponseMessages.UPDATE_FAIL;

            Optional<Callsign> callsignPresence = callsignRepository.findById(callsign.getId());
            if (callsignPresence.isEmpty()){
                return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }
            dbCalSn = callsignPresence.get();
        }

        try {
            if (!isUpdate || callsign.getCallsign() != null){
                dbCalSn.setCallsign(callsign.getCallsign());
            }
            if (!isUpdate || callsign.getCallsignPlural() != null){
                dbCalSn.setCallsignPlural(callsign.getCallsignPlural());
            }
            callsignRepository.save(dbCalSn);
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                    + successMsg,
                    ResponseMessages.SUCCESS_STATUS, dbCalSn);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                    + failMsg,
                    ResponseMessages.FAIL_STATUS, null);
        }

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
        try {
            List<Callsign> callsigns = callsignRepository.findAll();
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
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
                    switch (columnIndex) {
                        case 0:
                            cs.setCallsign(nextCell.getStringCellValue());
                            break;
                        case 1:
                            cs.setCallsignPlural(nextCell.getStringCellValue());
                            break;
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

    private boolean callsignCopyCheck(List<Callsign> callsigns, String name) {
        for (Callsign callsign : callsigns) {
            if (callsign.getCallsign().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
