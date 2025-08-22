package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="platform")
public class Platform {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_PLATFORM_ID_S")
    @SequenceGenerator(name = "LH_PLATFORM_ID_S", allocationSize = 1, sequenceName = "LH_PLATFORM_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PLATFORM_NAME")
    @NotBlank
    private String platformName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
