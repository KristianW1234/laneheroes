package com.personal.laneheroes.controllers;

import com.personal.laneheroes.entities.Platform;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.PlatformService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/platform")
@RequiredArgsConstructor
public class PlatformController {
    private final PlatformService platformService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<Platform>>> getAllPlatforms() {
        ResponseWrapper<List<Platform>> response = platformService.getAllPlatforms();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<Platform>> getOnePlatform(@PathVariable Long id) {
        ResponseWrapper<Platform> response = platformService.getPlatformById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<Platform>> addPlatform(@Valid @RequestBody Platform platform) {
        ResponseWrapper<Platform> response = platformService.addPlatform(platform);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseWrapper<Platform>> updatePlatform(@Valid @RequestBody Platform platform) {
        ResponseWrapper<Platform> response = platformService.updatePlatform(platform);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<Platform>> deletePlatform(@PathVariable Long id) {
        ResponseWrapper<Platform> response = platformService.deletePlatform(id);
        return ResponseEntity.ok(response);
    }
}
