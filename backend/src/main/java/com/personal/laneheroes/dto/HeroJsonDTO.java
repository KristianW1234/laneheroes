package com.personal.laneheroes.dto;

import com.personal.laneheroes.enums.Gender;

public class HeroJsonDTO {
    private String heroCode;
    private String heroName;
    private String heroTitle;
    private Gender heroGender;
    private String game; // game name as string
    private String imgIcon;
    private String heroDescription;
    private String heroLore;
    private String displayByTitle;

    public String getHeroCode() {
        return heroCode;
    }

    public void setHeroCode(String heroCode) {
        this.heroCode = heroCode;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroTitle() {
        return heroTitle;
    }

    public void setHeroTitle(String heroTitle) {
        this.heroTitle = heroTitle;
    }

    public Gender getHeroGender() {
        return heroGender;
    }

    public void setHeroGender(Gender heroGender) {
        this.heroGender = heroGender;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getHeroDescription() {
        return heroDescription;
    }

    public void setHeroDescription(String heroDescription) {
        this.heroDescription = heroDescription;
    }

    public String getHeroLore() {
        return heroLore;
    }

    public void setHeroLore(String heroLore) {
        this.heroLore = heroLore;
    }

    public String getDisplayByTitle() {
        return displayByTitle;
    }

    public void setDisplayByTitle(String displayByTitle) {
        this.displayByTitle = displayByTitle;
    }
}

