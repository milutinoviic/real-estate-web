package com.example.realestate.model;

import com.example.realestate.enums.Feeling;
import com.example.realestate.enums.TypeRealEstate;
import com.example.realestate.enums.TypeSale;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private double surfaceArea;

    private double price;

    private TypeSale typeSales;

    private TypeRealEstate typeRealEstate;

    private String picture;

    @ManyToOne
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private User agent;

    private boolean active;

}
