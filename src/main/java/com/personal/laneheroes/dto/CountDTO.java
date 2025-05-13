package com.personal.laneheroes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountDTO {
    private Long heroes;
    private Long games;
    private Long platforms;
    private Long callsigns;
    private Long companies;
    private Long users;

}
