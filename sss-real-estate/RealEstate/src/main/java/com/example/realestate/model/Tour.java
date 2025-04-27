package com.example.realestate.model;

import com.example.realestate.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Data
@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Tour(Long id, LocalDateTime date, Status status, RealEstate realEstate, User user) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.realEstate = realEstate;
        this.user = user;
    }

    public Tour(LocalDateTime date, Status status, RealEstate realEstate, User user) {
        this.date = date;
        this.status = status;
        this.realEstate = realEstate;
        this.user = user;
    }

    public Tour() {
    }


}
