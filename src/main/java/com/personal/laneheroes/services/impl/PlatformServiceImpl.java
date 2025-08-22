package com.personal.laneheroes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.laneheroes.dto.UploadResult;
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
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlatformServiceImpl implements PlatformService {

    private final PlatformRepository platformRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public PlatformServiceImpl(PlatformRepository platformRepository, ObjectMapper objectMapper) {
        this.platformRepository = platformRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseWrapper<Platform> addPlatform(Platform platform) {
        if (platform.getPlatformName() == null || platform.getPlatformName().isBlank()) {
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.ADD_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

        Platform dbPlat = new Platform();
        dbPlat.setPlatformName(platform.getPlatformName());
            platformRepository.save(dbPlat);
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.ADD_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbPlat);

    }

    @Override
    public ResponseWrapper<Platform> updatePlatform(Platform platform) {
        if (platform.getPlatformName() == null || platform.getPlatformName().isBlank()) {
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

        Optional<Platform> platformPresence = platformRepository.findById(platform.getId());
        if (platformPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Platform dbPlat = platformPresence.get();
        dbPlat.setPlatformName(platform.getPlatformName());
        platformRepository.save(dbPlat);
        return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                + ResponseMessages.UPDATE_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbPlat);

    }

    @Override
    public ResponseWrapper<Platform> deletePlatform(Long id) {
        Optional<Platform> platformPresence = platformRepository.findById(id);
        if (platformPresence.isPresent()){
            Platform dbPlat = platformPresence.get();
            platformRepository.delete(dbPlat);
            return new ResponseWrapper<>(ResponseMessages.PLATFORM_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, null);
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
        try (
                FileInputStream inputStream = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(inputStream);
        ){
            List<Platform> platforms = platformRepository.findAll();
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
                    plat.setPlatformName(nextCell.getStringCellValue());
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

    @Override
    public void uploadInitPlatformsFromJSON(String path) throws IOException {
        if (platformRepository.count() > 0) return;

        InputStream input = getClass().getClassLoader().getResourceAsStream(path);

        List<Platform> platforms = objectMapper.readValue(input, new TypeReference<>() {});
        platformRepository.saveAll(platforms);

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
