package com.personal.laneheroes.dto;

import com.personal.laneheroes.enums.SkillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillJsonDTO {
    public String id;
    public String skillName;
    public String skillDescription;
    public Integer skillSlot;
    public String imgIcon;
    public String isPassive;
    public String isUltimate;
    public String skillTypes;
    public String heroCode;
    public String heroId;
    public String heroImgIcon;

}

