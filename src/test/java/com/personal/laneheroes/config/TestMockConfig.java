package com.personal.laneheroes.config;

import com.personal.laneheroes.repositories.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestMockConfig {

    @Bean(name="mockHeroRepository")
    public HeroRepository heroRepository() {
        return Mockito.mock(HeroRepository.class);
    }

    @Bean(name="mockGameRepository")
    public GameRepository gameRepository() {
        return Mockito.mock(GameRepository.class);
    }

    @Bean(name="mockCompanyRepository")
    public CompanyRepository companyRepository() {
        return Mockito.mock(CompanyRepository.class);
    }

    @Bean(name="mockCallsignRepository")
    public CallsignRepository callsignRepository() {
        return Mockito.mock(CallsignRepository.class);
    }

    @Bean(name="mockPlatformRepository")
    public PlatformRepository platformRepository() {
        return Mockito.mock(PlatformRepository.class);
    }

    @Bean(name="mockUserRepository")
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    // Add more mock beans if needed
}
