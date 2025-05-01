package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="platform")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
