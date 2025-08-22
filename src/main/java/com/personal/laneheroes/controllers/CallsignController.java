package com.personal.laneheroes.controllers;

import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CallsignService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/callsign")
public class CallsignController {
    private final CallsignService callsignService;

    public CallsignController(CallsignService callsignService) {
        this.callsignService = callsignService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<Callsign>>> getAllCallsigns() {
        ResponseWrapper<List<Callsign>> response = callsignService.getAllCallsigns();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<Callsign>> getOneCallsign(@PathVariable Long id) {
        ResponseWrapper<Callsign> response = callsignService.getCallsignById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<Callsign>> addCallsign(@Valid @RequestBody Callsign callsign) {
        ResponseWrapper<Callsign> response = callsignService.addCallsign(callsign);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseWrapper<Callsign>> updateCallsign(@RequestBody Callsign callsign) {
        ResponseWrapper<Callsign> response = callsignService.updateCallsign(callsign);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<Callsign>> deleteCallsign(@PathVariable Long id) {
        ResponseWrapper<Callsign> response = callsignService.deleteCallsign(id);
        return ResponseEntity.ok(response);
    }


}
