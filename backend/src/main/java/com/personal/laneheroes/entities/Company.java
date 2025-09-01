package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="company")
public class Company {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_COMPANY_ID_S")
    @SequenceGenerator(name = "LH_COMPANY_ID_S", allocationSize = 1, sequenceName = "LH_COMPANY_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPANY_NAME")
    @NotBlank
    private String companyName;

    @Column(name = "IMG_ICON")
    private String imgIcon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }
}
