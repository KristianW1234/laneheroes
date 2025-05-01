package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="callsign")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Callsign {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_CALLSIGN_ID_S")
    @SequenceGenerator(name = "LH_CALLSIGN_ID_S", allocationSize = 1, sequenceName = "LH_CALLSIGN_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "CALLSIGN")
    @NotBlank
    private String callsign;

    @Column(name = "CALLSIGN_PLURAL")
    @NotBlank
    private String callsignPlural;

}
