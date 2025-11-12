package com.upc.matchpoint.courts.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courts")
@Getter
@Setter
@NoArgsConstructor
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String type;

    public Court(String name, String location, String type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public void updateCourt(String name, String location, String type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }
}