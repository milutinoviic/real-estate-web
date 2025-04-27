package com.example.realestate.model;

import com.example.realestate.enums.Feeling;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="like_or_dislike")
public class LikeOrDisLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "real_estate_id", referencedColumnName = "id")
    private RealEstate realEstate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Feeling feeling;

    public LikeOrDisLike(Long id, RealEstate realEstate, User user, Feeling feeling) {
        this.id = id;
        this.realEstate = realEstate;
        this.user = user;
        this.feeling = feeling;
    }

    public LikeOrDisLike(RealEstate realEstate, User user, Feeling feeling) {
        this.realEstate = realEstate;
        this.user = user;
        this.feeling = feeling;
    }

    public LikeOrDisLike() {
    }
}
