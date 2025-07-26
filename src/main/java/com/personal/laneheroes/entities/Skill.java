package com.personal.laneheroes.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.personal.laneheroes.enums.SkillType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name="skill")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
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

    // Getters/setters
}