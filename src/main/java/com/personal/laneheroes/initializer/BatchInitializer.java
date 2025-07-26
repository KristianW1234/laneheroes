package com.personal.laneheroes.initializer;

import com.personal.laneheroes.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Profile("!test")
public class BatchInitializer implements CommandLineRunner {

    private final SkillService skillService;
    private final HeroService heroService;
    private final GameService gameService;
    private final CompanyService companyService;
    private final PlatformService platformService;
    private final CallsignService callsignService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    @Override
    public void run(String... args) throws Exception {
        if ("none".equalsIgnoreCase(ddlMode)) {
            companyService.uploadInitCompaniesFromJSON("data/initCompanies.json");
            platformService.uploadInitPlatformsFromJSON("data/initPlatforms.json");
            callsignService.uploadInitCallsignsFromJSON("data/initCallsigns.json");
            gameService.uploadInitGamesFromJSON("data/initGames.json");
            heroService.uploadInitHeroesFromJSON("data/initHeroes.json");
            skillService.uploadInitSkillsFromJSON("data/initSkills.json");

            
        }


    }
}