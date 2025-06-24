package com.personal.laneheroes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameJsonDTO {
    public String gameCode;
    public String gameName;
    public String heroTitle;
    public String company;
    public String platform;
    public String callsign;
    public String imgIcon;
}

