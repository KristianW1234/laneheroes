package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="callsign")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCallsignPlural() {
        return callsignPlural;
    }

    public void setCallsignPlural(String callsignPlural) {
        this.callsignPlural = callsignPlural;
    }
}
