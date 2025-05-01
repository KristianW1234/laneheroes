package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="hero")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hero {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_HERO_ID_S")
    @SequenceGenerator(name = "LH_HERO_ID_S", allocationSize = 1, sequenceName = "LH_HERO_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HERO_CODE")
    private String heroCode;

    @Column(name = "HERO_NAME")
    private String heroName;

    @Column(name = "HERO_TITLE")
    private String heroTitle;


    @ManyToOne
    @JoinColumn(name="GAME_ID",
            foreignKey=@ForeignKey(name="FK_GAME_ID"))
    private Game game;


    @Column(name = "ALTERNATE_NAME")
    private String alternateName;

    @Column(name = "IMG_ICON")
    private String imgIcon;
}
