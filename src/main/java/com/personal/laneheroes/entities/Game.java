package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="game")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Callsign getCallsign() {
        return callsign;
    }

    public void setCallsign(Callsign callsign) {
        this.callsign = callsign;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }
}
