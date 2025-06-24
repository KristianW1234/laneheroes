package com.personal.laneheroes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Entity
@Table(name="company")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
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
}
