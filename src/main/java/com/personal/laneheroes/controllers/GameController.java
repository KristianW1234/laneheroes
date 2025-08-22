package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Game;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.GameService;
import com.personal.laneheroes.utilities.Utility;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<Game>>> getAllGames() {
        ResponseWrapper<List<Game>> response = gameService.getAllGames();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<Game>> getOneGame(@PathVariable Long id) {
        ResponseWrapper<Game> response = gameService.getGameById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Game>> addGame(
            @RequestPart("game") Game game,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {

        ResponseWrapper<Game> result = gameService.addGame(game, imgFile);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Game>> updateGame(
            @RequestPart("game") Game game,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {

        ResponseWrapper<Game> result = gameService.updateGame(game, imgFile);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<Game>> deleteGame(@PathVariable Long id) {
        ResponseWrapper<Game> response = gameService.deleteGame(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PagedResponse<Game>>> searchGames(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long platformId,
            @RequestParam(required = false) Long callsignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        ResponseWrapper<PagedResponse<Game>> response = gameService.searchGames(name, code, companyId, platformId, callsignId, Utility.setupPageable(page, size, sortBy, sortOrder));
        return ResponseEntity.ok(response);
    }
}
