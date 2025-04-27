package com.example.realestate.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Agency {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;


    public Agency(Long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public Agency(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public Agency() {
    }
}
