package com.epam.smartcityhousingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HMACSecretKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String componentName;

    @Column(nullable = false)
    private String secretKey;
}