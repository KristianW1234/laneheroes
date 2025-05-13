package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.CountDTO;
import com.personal.laneheroes.utilities.ResponseMessages;
import com.personal.laneheroes.utilities.Utility;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/laneHeroes/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping(value = "/batch-upload-all", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<String>> uploadAllData(
            @RequestParam("companyFile") MultipartFile companyFile,
            @RequestParam("callsignFile") MultipartFile callsignFile,
            @RequestParam("platformFile") MultipartFile platformFile,
            @RequestParam("gameFile") MultipartFile gameFile,
            @RequestParam("heroFile") MultipartFile heroFile) {

        try {
            File company = Utility.saveTempFile(companyFile);
            File callsign = Utility.saveTempFile(callsignFile);
            File platform = Utility.saveTempFile(platformFile);
            File game = Utility.saveTempFile(gameFile);
            File hero = Utility.saveTempFile(heroFile);

            String resultMessage = adminService.uploadAllData(
                    company.getAbsolutePath(),
                    callsign.getAbsolutePath(),
                    platform.getAbsolutePath(),
                    game.getAbsolutePath(),
                    hero.getAbsolutePath()
            );

            return ResponseEntity.ok(new ResponseWrapper<>("Upload Complete", ResponseMessages.SUCCESS_STATUS, resultMessage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("Upload failed: " + e.getMessage(), ResponseMessages.FAIL_STATUS, null));
        }
    }

    @GetMapping(value = "/getStats")
    public ResponseEntity<ResponseWrapper<CountDTO>> getStats() {
        CountDTO dto = adminService.getAllCounts();
        return ResponseEntity.ok(new ResponseWrapper<>("Stat Search complete", ResponseMessages.SUCCESS_STATUS, dto));

    }
}

