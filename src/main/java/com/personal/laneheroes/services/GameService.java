package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Game;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameService {
    ResponseWrapper<Game> addOrUpdateGame(Game game, MultipartFile imgFile, boolean isUpdate);

    ResponseWrapper<Game> deleteGame(Long id);

    ResponseWrapper<List<Game>> getAllGames();

    ResponseWrapper<Game> getGameById(Long id);

    ResponseWrapper<PagedResponse<Game>> searchGames(String name, String code, Long companyId, Long platformId, Long callsignId, Pageable pageable);

    ResponseWrapper<UploadResult> uploadGamesFromExcel(String excelFile);
}
