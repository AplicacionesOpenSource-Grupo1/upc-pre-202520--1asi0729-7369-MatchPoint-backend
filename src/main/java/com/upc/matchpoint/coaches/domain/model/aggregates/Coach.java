package com.upc.matchpoint.coaches.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String expertise;

    @Column(nullable = false)
    private String phone;

    public Coach(String name, String expertise, String phone) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
    }

    public void updateCoach(String name, String expertise, String phone) {
        this.name = name;
        this.expertise = expertise;
        this.phone = phone;
    }
}