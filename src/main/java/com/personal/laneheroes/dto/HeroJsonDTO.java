package com.personal.laneheroes.dto;

import com.personal.laneheroes.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroJsonDTO {
    public String heroCode;
    public String heroName;
    public String heroTitle;
    public Gender heroGender;
    public String game; // game name as string
    public String imgIcon;
    public String heroDescription;
    public String heroLore;
    public String displayByTitle;
}

