package com.personal.laneheroes.entities;

import com.personal.laneheroes.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name="hero")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
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

    @Column(name = "HERO_GENDER")
    @Enumerated(EnumType.STRING)
    private Gender heroGender;

    @Column(name = "HERO_TITLE")
    private String heroTitle;


    @ManyToOne
    @JoinColumn(name="GAME_ID",
            foreignKey=@ForeignKey(name="FK_GAME_ID"))
    private Game game;


    @Column(name = "ALTERNATE_NAME")
    private String alternateName;

    //@Lob
    @Column(name = "HERO_DESCRIPTION", columnDefinition = "TEXT")
    private String heroDescription;

    //@Lob
    @Column(name = "HERO_LORE", columnDefinition = "TEXT")
    private String heroLore;

    @Column(name = "IMG_ICON")
    private String imgIcon;

    @Column(name= "TITLE_DISPLAY")
    private String displayByTitle;
}
