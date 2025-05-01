package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Platform;
import com.personal.laneheroes.entities.Platform;
import com.personal.laneheroes.repositories.PlatformRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.PlatformService;
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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service("PlatformServiceImpl")
@Transactional
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    PlatformRepository platformRepository;


    @Override
    public ResponseWrapper<Platform> addOrUpdatePlatform(Platform platform, boolean isUpdate) {
        Platform dbPlat = new Platform();
        String successMsg = ResponseMessages.ADD_SUCCESS;
        String failMsg = ResponseMessages.ADD_FAIL;
        if (isUpdate) {

            successMsg = ResponseMessages.UPDATE_SUCCESS;
            failMsg = ResponseMessages.UPDATE_FAIL;
            Optional<Platform> platformPresence = platformRepository.findById(platform.getId());
            if (!platformPresence.isPresent()){
                return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }
            dbPlat = platformPresence.get();
        }
        try {
            dbPlat.setPlatformName(platform.getPlatformName());
            platformRepository.save(dbPlat);
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + successMsg,
                    ResponseMessages.SUCCESS_STATUS, dbPlat);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + failMsg,
                    ResponseMessages.FAIL_STATUS, null);
        }

    }

    @Override
    public ResponseWrapper<Platform> deletePlatform(Long id) {
        Optional<Platform> platformPresence = platformRepository.findById(id);
        if (platformPresence.isPresent()){
            Platform dbCom = platformPresence.get();
            platformRepository.delete(dbCom);
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbCom);
        } else {
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }



    @Override
    public ResponseWrapper<List<Platform>> getAllPlatforms() {
        List<Platform> list = platformRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.PLATFORM_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.PLATFORM_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<Platform> getPlatformById(Long id) {
        Optional<Platform> platformPresence = platformRepository.findById(id);
        return platformPresence.map(
                        platform -> new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE
                                + " " + ResponseMessages.FOUND,
                                ResponseMessages.SUCCESS_STATUS, platform))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<UploadResult> uploadPlatformsFromExcel(String excelFile) {
        int totalAdded = 0;
        try {
            List<Platform> platforms = platformRepository.findAll();
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip the header row
            Platform plat;
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                plat = new Platform();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            plat.setPlatformName(nextCell.getStringCellValue());
                            break;
                    }
                }
                if (!platformCopyCheck(platforms, plat.getPlatformName())) {
                    totalAdded++;
                    platformRepository.save(plat);
                }

            }

        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL , ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }
        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS , ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    private boolean platformCopyCheck(List<Platform> comps, String name) {
        for (Platform comp : comps) {
            if (comp.getPlatformName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
