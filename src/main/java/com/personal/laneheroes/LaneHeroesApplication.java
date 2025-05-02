package com.personal.laneheroes;

import com.personal.laneheroes.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LaneHeroesApplication {

	public static void main(String[] args) {
		EnvLoader.loadEnv();
		SpringApplication.run(LaneHeroesApplication.class, args);
	}

}
