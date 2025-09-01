package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.CountDTO;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.repositories.*;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final CompanyService companyService;

    private final CallsignService callsignService;

    private final PlatformService platformService;

    private final GameService gameService;

    private final HeroService heroService;

    private final SkillService skillService;

    private final CompanyRepository companyRepository;

    private final CallsignRepository callsignRepository;

    private final PlatformRepository platformRepository;

    private final GameRepository gameRepository;

    private final HeroRepository heroRepository;

    private final UserRepository userRepository;

    private final SkillRepository skillRepository;

    @Autowired
    public AdminServiceImpl(CompanyService companyService, CallsignService callsignService, PlatformService platformService, GameService gameService, HeroService heroService, SkillService skillService, CompanyRepository companyRepository, CallsignRepository callsignRepository, PlatformRepository platformRepository, GameRepository gameRepository, HeroRepository heroRepository, UserRepository userRepository, SkillRepository skillRepository) {
        this.companyService = companyService;
        this.callsignService = callsignService;
        this.platformService = platformService;
        this.gameService = gameService;
        this.heroService = heroService;
        this.skillService = skillService;
        this.companyRepository = companyRepository;
        this.callsignRepository = callsignRepository;
        this.platformRepository = platformRepository;
        this.gameRepository = gameRepository;
        this.heroRepository = heroRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public String uploadAllData(String companyPath, String callsignPath, String platformPath, String gamePath, String heroPath, String skillPath) {
        ResponseWrapper<UploadResult> companyResult = companyService.uploadCompaniesFromExcel(companyPath);
        int companyTotal = 0;
        if (companyResult.getData().getErrorMessage() == null && companyResult.getData().getSavedCount() > 0){
            companyTotal = companyResult.getData().getSavedCount();
        }

        ResponseWrapper<UploadResult> callsignResult = callsignService.uploadCallsignsFromExcel(callsignPath);
        int callsignTotal = 0;
        if (callsignResult.getData().getErrorMessage() == null && callsignResult.getData().getSavedCount() > 0){
            callsignTotal = callsignResult.getData().getSavedCount();
        }

        ResponseWrapper<UploadResult> platformResult = platformService.uploadPlatformsFromExcel(platformPath);
        int platformTotal = 0;
        if (platformResult.getData().getErrorMessage() == null && platformResult.getData().getSavedCount() > 0){
            platformTotal = platformResult.getData().getSavedCount();
        }

        ResponseWrapper<UploadResult> gameResult = gameService.uploadGamesFromExcel(gamePath);
        int gameTotal = 0;
        if (gameResult.getData().getErrorMessage() == null && gameResult.getData().getSavedCount() > 0){
            gameTotal = gameResult.getData().getSavedCount();
        }

        ResponseWrapper<UploadResult> heroResult = heroService.uploadHeroesFromExcel(heroPath);
        int heroTotal = 0;
        if (heroResult.getData().getErrorMessage() == null && heroResult.getData().getSavedCount() > 0){
            heroTotal = heroResult.getData().getSavedCount();
        }

        ResponseWrapper<UploadResult> skillResult = skillService.uploadSkillsFromExcel(skillPath);
        int skillTotal = 0;
        if (skillResult.getData().getErrorMessage() == null && skillResult.getData().getSavedCount() > 0){
            skillTotal = skillResult.getData().getSavedCount();
        }
        return String.format(
                "%d companies, %d callsigns, %d platforms, %d games, %d heroes, %d skills saved.",
                companyTotal, callsignTotal, platformTotal, gameTotal, heroTotal, skillTotal
        );
    }

    @Override
    public CountDTO getAllCounts() {
        CountDTO dto = new CountDTO();
        dto.setCallsigns(callsignRepository.count());
        dto.setCompanies(companyRepository.count());
        dto.setPlatforms(platformRepository.count());
        dto.setGames(gameRepository.count());
        dto.setHeroes(heroRepository.count());
        dto.setUsers(userRepository.count());
        dto.setSkills(skillRepository.count());
        return dto;
    }
}
