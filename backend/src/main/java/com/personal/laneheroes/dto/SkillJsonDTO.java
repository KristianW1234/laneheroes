package com.personal.laneheroes.dto;

import com.personal.laneheroes.enums.SkillType;

public class SkillJsonDTO {
    private String id;
    private String skillName;
    private String skillDescription;
    private Integer skillSlot;
    private String imgIcon;
    private String isPassive;
    private String isUltimate;
    private String skillTypes;
    private String heroCode;
    private String heroId;
    private String heroImgIcon;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public Integer getSkillSlot() {
        return skillSlot;
    }

    public void setSkillSlot(Integer skillSlot) {
        this.skillSlot = skillSlot;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getIsPassive() {
        return isPassive;
    }

    public void setIsPassive(String isPassive) {
        this.isPassive = isPassive;
    }

    public String getIsUltimate() {
        return isUltimate;
    }

    public void setIsUltimate(String isUltimate) {
        this.isUltimate = isUltimate;
    }

    public String getSkillTypes() {
        return skillTypes;
    }

    public void setSkillTypes(String skillTypes) {
        this.skillTypes = skillTypes;
    }

    public String getHeroCode() {
        return heroCode;
    }

    public void setHeroCode(String heroCode) {
        this.heroCode = heroCode;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getHeroImgIcon() {
        return heroImgIcon;
    }

    public void setHeroImgIcon(String heroImgIcon) {
        this.heroImgIcon = heroImgIcon;
    }
}

