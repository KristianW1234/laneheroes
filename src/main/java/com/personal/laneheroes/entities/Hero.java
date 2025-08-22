package com.personal.laneheroes.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.personal.laneheroes.enums.Gender;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hero")
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

    @OneToMany(mappedBy = "hero", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Skill> skills = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Gender getHeroGender() {
        return heroGender;
    }

    public void setHeroGender(Gender heroGender) {
        this.heroGender = heroGender;
    }

    public String getHeroTitle() {
        return heroTitle;
    }

    public void setHeroTitle(String heroTitle) {
        this.heroTitle = heroTitle;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
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

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getDisplayByTitle() {
        return displayByTitle;
    }

    public void setDisplayByTitle(String displayByTitle) {
        this.displayByTitle = displayByTitle;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
