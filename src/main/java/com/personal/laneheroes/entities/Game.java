package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="game")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_GAME_ID_S")
    @SequenceGenerator(name = "LH_GAME_ID_S", allocationSize = 1, sequenceName = "LH_GAME_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "GAME_NAME")
    private String gameName;

    @ManyToOne
    @JoinColumn(name="CALLSIGN_ID",
            foreignKey=@ForeignKey(name="FK_CALLSIGN_ID"))
    private Callsign callsign;

    @ManyToOne
    @JoinColumn(name="COMPANY_ID",
            foreignKey=@ForeignKey(name="FK_COMPANY_ID"))
    private Company company;

    @ManyToOne
    @JoinColumn(name="PLATFORM_ID",
            foreignKey=@ForeignKey(name="FK_PLATFORM_ID"))
    private Platform platform;

    @Column(name = "GAME_CODE")
    private String gameCode;

    @Column(name = "IMG_ICON")
    private String imgIcon;
}
