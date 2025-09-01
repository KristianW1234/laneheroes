package com.personal.laneheroes.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.personal.laneheroes.enums.SkillType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="skill")
public class Skill {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_SKILL_ID_S")
    @SequenceGenerator(name = "LH_SKILL_ID_S", allocationSize = 1, sequenceName = "LH_SKILL_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SKILL_NAME")
    private String skillName;

    @Column(name = "SKILL_DESCRIPTION", columnDefinition = "TEXT")
    private String skillDescription;

    @Column(name = "SKILL_SLOT")
    private Integer skillSlot;

    @Column(name = "IMG_ICON")
    private String imgIcon;

    @Column(name = "IS_PASSIVE", nullable = false)
    private String isPassive;

    @Column(name = "IS_ULTIMATE", nullable = false)
    private String isUltimate;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "SKILL_TYPES")
    private List<SkillType> skillTypes; // e.g., [OFFENSIVE, DEBUFF]

    @ManyToOne
    @JoinColumn(name = "HERO_ID")
    @JsonBackReference
    private Hero hero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<SkillType> getSkillTypes() {
        return skillTypes;
    }

    public void setSkillTypes(List<SkillType> skillTypes) {
        this.skillTypes = skillTypes;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}