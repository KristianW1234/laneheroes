package com.personal.laneheroes.dto;

import com.personal.laneheroes.enums.SkillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillJsonDTO {
    public String skillName;
    public String skillDescription;
    public int skillSlot;
    public String imgIcon;
    public String isPassive;
    public String isUltimate;
    public String skillTypes;
    public String hero;

}

